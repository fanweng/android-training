# Vendor HIDL Lab 101: HIDL HAL

**Objective:** creating the HAL API using HIDL

Android build system will run `hidl-gen` ([doc](https://android.googlesource.com/platform/system/tools/hidl/+/master/README.md)) on the `.hal` file(s) to generate IPC classes:
+ **proxy** or its native name - **Bp (Binder Proxy)** - for calling process
+ **stub** or its native name - **Bn (Binder native)** - for service process

1. Create vendor interface folder

Src: [vendor/windfan/interfaces/Android.bp](./lab1/vendor/windfan/interfaces/Android.bp)

```sh
$ mkdir -p vendor/windfan/interfaces
$ cd vendor/windfan/interfaces
$ vi Android.bp
```

2. Create a CPU version folder

```sh
$ mkdir -p vendor/windfan/interfaces/cpu/1.0
```

3. Create an API interface to get/set CPU scaling governor running in the kernel driver

Src: [vendor/windfan/interfaces/cpu/1.0/ICpu.hal](./lab1/vendor/windfan/interfaces/cpu/1.0/ICpu.hal)

```sh
$ vi vendor/windfan/interfaces/cpu/1.0/ICpu.hal
```

3. Create a `.bp` with `hidl_interface` module

Src: [vendor/windfan/interfaces/cpu/1.0/Android.bp](./lab1/vendor/windfan/interfaces/cpu/1.0/Android.bp)

```sh
$ vi vendor/windfan/interfaces/cpu/1.0/Android.bp
```

4. Build the module

```sh
$ source build/envsetup.sh
$ cd vendor/windfan/interfaces/cpu/1.0
$ mm -j8
```



## Generated Binder Proxy (BpHwCpu) and Binder Stub/Native (BnHwCpu)

#### Generated `CpuAll.cpp`

Src: [out/soong/.intermediates/vendor/windfan/interfaces/cpu/1.0/vendor.windfan.cpu@1.0_genc++/gen/vendor/windfan/cpu/1.0/CpuAll.cpp](./lab1/out/soong/.intermediates/vendor/windfan/interfaces/cpu/1.0/vendor.windfan.cpu@1.0_genc++/gen/vendor/windfan/cpu/1.0/CpuAll.cpp)

+ Function `BpHwCpu::_hidl_setScalingGovernor`

This is the auto generated *Binder Proxy* function, used by the calling process (client) in order to marshall (serialize) the call's parameters and send it via the binder to the CPU service.
```c++
::android::hardware::Return<bool> BpHwCpu::_hidl_setScalingGovernor(
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

+ Service binder call `BnHwCpu::_hidl_setScalingGovernor`

1. read the `_hidl_data` from the calling client process
2. callback with reply buffer. The binder kernel driver will copy the reply back to the calling proxy running process `BpHwCpu`

```c++
::android::status_t BnHwCpu::_hidl_setScalingGovernor(
    ::android::hidl::base::V1_0::BnHwBase* _hidl_this,
    const ::android::hardware::Parcel &_hidl_data,
    ::android::hardware::Parcel *_hidl_reply,
    // the callback method to send back in the _hild_reply
    TransactCallback _hidl_cb) {
//... ...
    // the 1st object unmarshalled from the _hidl_data is the descriptor
    if (!_hidl_data.enforceInterface(BnHwCpu::Pure::descriptor)) {
        _hidl_err = ::android::BAD_TYPE;
        return _hidl_err;
    }
//... ...
    // the next object unmarshalled is the governor string
    const ::android::hardware::hidl_string* governor;
    _hidl_err = _hidl_data.readBuffer(sizeof(*governor), &_hidl_governor_parent, reinterpret_cast<const void **>(&governor));
//... ...
    // in the ICpu.hal file, the return type is defined as a bool success
    bool _hidl_out_success = static_cast<ICpu*>(_hidl_this->getImpl().get())->setScalingGovernor(*governor);
    // the 1st object written to the _hidl_reply is the Status::ok
    ::android::hardware::writeToParcel(::android::hardware::Status::ok(), _hidl_reply);
    // the 2nd object written to the reply buffer is the _hidl_out_success bool
    _hidl_err = _hidl_reply->writeBool(_hidl_out_success);
//... ...
    // using TransactCallback to send the reply buffer
    _hidl_cb(*_hidl_reply);
    return _hidl_err;
}
```



## Include the Custom HAL Module in the Android Makefile

1. Create a makefile for the module above

Src: [device/google/cuttlefish/shared/windfan.mk](./lab1/device/google/cuttlefish/shared/windfan.mk)
```makefile
PRODUCT_PACKAGES += \
    vendor.windfan.cpu@1.0
```

2. Include the custom makefile in the original Android build

Src: [device/google/cuttlefish/shared/phone/device_vendor.mk](./lab1/device/google/cuttlefish/shared/phone/device_vendor.mk)

```makefile
$(call inherit-product, device/google/cuttlefish/shared/device.mk)
$(call inherit-product, device/google/cuttlefish/shared/windfan.mk) // add this line
```

3. Run AOSP build and check outputs

```sh
/aosp/out/target/product/vsoc_x86$ find . | grep  windfan
./product/lib/vendor.windfan.cpu@1.0.so
./symbols/product/lib/vendor.windfan.cpu@1.0.so
./obj/SHARED_LIBRARIES/vendor.windfan.cpu@1.0_intermediates
./obj/SHARED_LIBRARIES/vendor.windfan.cpu@1.0_intermediates/vendor.windfan.cpu@1.0.so
```



## Appendix

#### Package prefixes and locations

| Package Prefix | Location | Interface Types |
|----------------|----------|-----------------|
| `android.hardware.*` | `hardware/interfaces/*` | HAL |
| `android.frameworks.*` | `frameworks/hardware/interfaces/*` | frameworks/ related |
| `android.system.*` | `system/hardware/interfaces/*` | system/ related |
| `android.hidl.*` | `system/libhidl/transport/*` | core |

#### Interface definition

The `.hal` file defines an interface. If the interface is without an explicit `extends` declaration, it implicitly extends from the `android.hidl.base@1.0::IBase`.

THe `IBase` interface contains reserved methods that cannot be redeclared in user-defined interface: `ping`, `interfaceChain`, `linkToDeath`, `debug`, ...
