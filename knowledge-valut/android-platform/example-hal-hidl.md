# Creating the CPU-related HAL API using Vendor HIDL

## Create a HAL interface

```sh
# 1. Create vendor interface folder
$ mkdir -p vendor/windfan/interfaces
$ vi vendor/windfan/interfaces/Android.bp

# 2. Create a CPU version folder
$ mkdir -p vendor/windfan/interfaces/cpu/1.0

# 3. Create an API interface .hal with CPU-related APIs
$ vi vendor/windfan/interfaces/cpu/1.0/ICpu.hal

# 4. Create a .bp with hidl_interface module
$ cat vendor/windfan/interfaces/cpu/1.0/Android.bp
hidl_interface {
    name: "vendor.windfan.cpu@1.0",
    root: "vendor.windfan",
    srcs: [
        "ICpu.hal",
    ],
...
}

# 5. Build the module
$ source build/envsetup.sh
$ cd vendor/windfan/interfaces/cpu/1.0
$ mm -j8
```  

## Generated Binder Proxy and Binder Stub/Native

### Generated `CpuAll.cpp`

After building the module, `vendor/windfan/interfaces/cpu/1.0/vendor.windfan.cpu@1.0_genc++/gen/vendor/windfan/cpu/1.0/CpuAll.cpp` will be generated.

+ Binder Proxy Function `BpHwCpu::_hidl_XXGovernor`
	+ the auto generated *Binder Proxy* function, used by the calling process (client) in order to marshall (serialize) the call's parameters and send it via the binder to the CPU service

```c++
::android::hardware::Return<bool> BpHwCpu::_hidl_XXGovernor(
    // BpHwCpu inherits from IInterface
    ::android::hardware::IInterface *_hidl_this,
    // used for instrumentation (debug only)
    ::android::hardware::details::HidlInstrumentor *_hidl_this_instrumentor,
    // first input (governor)
    const ::android::hardware::hidl_string& governor) {
//... ...
}
```

+ Write `governor` string to `_hidl_data` parcel buffer

`Parcel` object ([source](https://android.googlesource.com/platform/system/libhwbinder/+/refs/heads/master/include/hwbinder/Parcel.h#291)) holds a buffer object `mData` that is used for marshalling the input variables.

```c++
const char* ICpu::descriptor("vendor.windfan.cpu@1.0::ICpu");

::android::hardware::Parcel _hidl_data;
::android::hardware::Parcel _hidl_reply; // reply buffer
::android::status_t _hidl_err;

// the 1st object written to the buffer is the fully qualified interface name
_hidl_err = _hidl_data.writeInterfaceToken(BpHwCpu::descriptor);
// the 2nd object is the governor string
_hidl_err = _hidl_data.writeBuffer(&governor, sizeof(governor), &_hidl_governor_parent);
```

+ `transact()` call

`transact()` is a binder call that will appear in `BnHwCpu::onTransact` on the service process IPC.

```c++
_hidl_transact_err = ::android::hardware::IInterface::asBinder(_hidl_this)->transact(
    // function index, getScalingGovernor is 1
    2 /* setScalingGovernor */,
    // Parcel object that will be copied to the service via the binder
    _hidl_data,
    // empty Parcel object to be filled by the binder from service reply
    &_hidl_reply,
    0 /* flags */);
```

+ Service Binder Native call `BnHwCpu::_hidl_XXGovernor`
	+ read the `_hidl_data` from the calling client process
	+ callback with reply buffer. The binder kernel driver will copy the reply back to the calling proxy running process `BpHwCpu`

```c++
::android::status_t BnHwCpu::_hidl_XXGovernor(
    ::android::hidl::base::V1_0::BnHwBase* _hidl_this,
    const ::android::hardware::Parcel &_hidl_data,
    ::android::hardware::Parcel *_hidl_reply,
    // the callback method to send back in the _hild_reply
    TransactCallback _hidl_cb) {
... ...
}
```


## Include the Custom HAL Module in the Android Makefile

```sh
# 1. Create a makefile for the custom HAL module
$ cat device/google/cuttlefish/shared/windfan.mk
PRODUCT_PACKAGES += \
    vendor.windfan.cpu@1.0

# 2. Include the custom makefile in the original Android build
$ cat device/google/cuttlefish/shared/phone/device_vendor.mk
$(call inherit-product, device/google/cuttlefish/shared/device.mk)
$(call inherit-product, device/google/cuttlefish/shared/windfan.mk) # add this line

# Run AOSP build and check outputs
/aosp/out/target/product/vsoc_x86$ find . | grep  windfan
./product/lib/vendor.windfan.cpu@1.0.so
./symbols/product/lib/vendor.windfan.cpu@1.0.so
./obj/SHARED_LIBRARIES/vendor.windfan.cpu@1.0_intermediates
./obj/SHARED_LIBRARIES/vendor.windfan.cpu@1.0_intermediates/vendor.windfan.cpu@1.0.so
```  

## Implemente HIDL service for HAL interface

*soong* build system should had created a new C++ header file `ICpu.h`.

```sh
$ cat vendor/windfan/interfaces/cpu/1.0/vendor.windfan.cpu@1.0_genc++_headers/gen/vendor/windfan/cpu/1.0/ICpu.h
struct ICpu : public ::android::hidl::base::V1_0::IBase {
//... ...
    virtual ::android::hardware::Return<bool> XXGovernor(const ::android::hardware::hidl_string& governor) = 0;
}

```

The `XXGovernor` is a pure virtual function in the `ICpu.h` so we need to implement them.

```sh
# 1. Define a new Cpu struct extending the ICpu
$ cat vendor/windfan/interfaces/cpu/1.0/default/Cpu.h
struct Cpu : public ICpu {
public:
    Return<bool> XXGovernor(const hidl_string& governor) override;
};
```
