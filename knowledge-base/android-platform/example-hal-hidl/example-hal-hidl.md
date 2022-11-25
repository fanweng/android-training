# Creating the CPU-related HAL API using Vendor HIDL

+ Application UI - `vendor/app/AppName/`
+ Java Application - `vendor/app/AppName/`
  + android_app (`.apk`)
  + signed with the platform build key
+ Java SDK Library - `vendor/framework/com.sdkname.jar`
  + java_sdk_library (`.jar`)
+ HIDL Native Client - `vendor/lib/libname.so`
  + cc_library_shared (`.so`)
  + exposed using JNI
+ ------------ /dev/hwbinder ------------
+ HIDL Service Implementation - `vendor/bin/hw/vendor.intfacename.featurename@1.0-service`
  + cc_bianry (executable)
  + registered with the `hwbinder`, started by `init`
  + can't be disabled or killed
+ HIDL Interface - `vendor/lib/vendor.intfacename.featurename@1.0.so`
  + hidl_interface (`.so`)
  + generate Binder Proxy (*Bp*) and Binder Native (*Bn*)
+ Linux Kernel Driver/Service

## Create HIDL HAL Interfaces

```sh
# 1. Create vendor interface folder
$ mkdir -p vendor/windfan/interfaces
$ vi vendor/windfan/interfaces/Android.bp

# 2. Create a CPU version folder
$ mkdir -p vendor/windfan/interfaces/cpu/1.0

# 3. Create an API interface .hal with CPU-related APIs
$ vi vendor/windfan/interfaces/cpu/1.0/ICpu.hal

# 4. Create a .bp with hidl_interface module
$ vi vendor/windfan/interfaces/cpu/1.0/Android.bp

# 5. Build the module
$ source build/envsetup.sh
$ cd vendor/windfan/interfaces/cpu/1.0
$ mm -j8
```

### Generated Binder Proxy and Binder Stub/Native

#### Generated `CpuAll.cpp`

After building the module, [`vendor/windfan/interfaces/cpu/1.0/vendor.windfan.cpu@1.0_genc++/gen/vendor/windfan/cpu/1.0/CpuAll.cpp`](vendor/windfan/interfaces/cpu/1.0/vendor.windfan.cpu@1.0_genc++/gen/vendor/windfan/cpu/1.0/CpuAll.cpp) will be generated.

+ Binder Proxy Function `BpHwCpu::_hidl_setScalingGovernor()`
    + the auto generated *Binder Proxy* function, used by the calling process (client) in order to marshall (serialize) the call's parameters and send it via the binder to the CPU service

+ Write `governor` string to parcel buffer `_hidl_data.writeBuffer()`

`Parcel` object ([source](https://android.googlesource.com/platform/system/libhwbinder/+/refs/heads/master/include/hwbinder/Parcel.h#291)) holds a buffer object `mData` that is used for marshalling the input variables.

+ `transact()` call

`transact()` is a binder call that will appear in `BnHwCpu::onTransact` on the service process IPC.

+ Service Binder Native call `BnHwCpu::_hidl_setScalingGovernor`
    + read the `_hidl_data` from the calling client process
    + callback with reply buffer. The binder kernel driver will copy the reply back to the calling proxy running process `BpHwCpu`

### Include the Custom HAL Module in the Android Makefile

```sh
# 1. Create a makefile for the custom HAL module
$ cat device/google/cuttlefish/shared/windfan.mk
PRODUCT_PACKAGES += \
    vendor.windfan.cpu@1.0

# 2. Include the custom makefile in the original Android build
$ cat device/google/cuttlefish/shared/phone/device_vendor.mk
$(call inherit-product, device/google/cuttlefish/shared/windfan.mk) # add this line

# Run AOSP build and check outputs
/aosp/out/target/product/vsoc_x86$ find ./vendor | grep windfan
./lib/vendor.windfan.cpu@1.0.so
```

## Hardware Service Implementation

### Implement HIDL service for HAL interface

*soong* build system should had created a new C++ header file `ICpu.h`.

```sh
$ cat vendor/windfan/interfaces/cpu/1.0/vendor.windfan.cpu@1.0_genc++_headers/gen/vendor/windfan/cpu/1.0/ICpu.h
struct ICpu : public ::android::hidl::base::V1_0::IBase {
    virtual ::android::hardware::Return<bool> setScalingGovernor(const ::android::hardware::hidl_string& governor) = 0;
};
```

The `setScalingGovernor` is a pure virtual function in the `ICpu.h` so we need to implement them in a new `Cpu.h` and `Cpu.cpp`.

```sh
# 1. Define a new Cpu struct extending the ICpu
$ cat vendor/windfan/interfaces/cpu/1.0/default/Cpu.h
struct Cpu : public ICpu {
public:
    Return<bool> setScalingGovernor(const hidl_string& governor) override;
};

# 2. Implement the API by using the CPU functionalities exposed by kernel driver
# /sys/devices/system/cpu/cpuo/cpufreq/scaling_governor
$ cat vendor/windfan/interfaces/cpu/1.0/default/Cpu.cpp
Return<bool> Cpu::setScalingGovernor(const hidl_string& governor) {
    return write_value("/sys/devices/system/cpu/cpuo/cpufreq/scaling_governor", governor.c_str()) == 0;
}
```

### Register as a Hardware Service

Create a service program under `vendor/windfan/interfaces/cpu/1.0/default/service.cpp`, where `Cpu` struct needs to be:
+ instantiated and registered as a hardware service with the `hwservicemanager`
    + `service = new Cpu()`
    + `service->registerAsService()`
+ running in a daemon process that never exits
    + `configureRpcThreadPool()`
    + `joinRpcThreadpool()`

### Create `.bp` for the Service

```sh
$ vi vendor/windfan/interfaces/cpu/1.0/default/Android.bp
```

#### Vendor Interface (VINTF)

`vendor/windfan/interfaces/cpu/1.0/default/vendor.windfan.cpu@1.0-service.xml` defines the Vendor Interface Object.

In that, the HAL format is set to `hidl`, which automatically sets the IPC binder driver to the `/dev/hwbinder` transport driver.

| IPC Domain      | Description |
|-----------------|-------------|
| `/dev/binder`    | IPC between framework/app processes with **AIDL** interfaces |
| `/dev/hwbinder`  | IPC between framework/vendor or vendor/vendor processes with **HIDL** interfaces |
| `/dev/vndbinder` | IPC between vendor/vendor processes with **AIDL** interfaces |

#### Init Daemon

The `windfan_cpu_hal_service` daemon needs to be defined in the `vendor/windfan/interfaces/cpu/1.0/default/vendor.windfan.cpu@1.0-service.rc`, so the HAL service will start at system boot-up.

### Include the Hardware Service in the Android Makefile

```sh
$ cat device/google/cuttlefish/shared/windfan.mk
PRODUCT_PACKAGES += \
    vendor.windfan.cpu@1.0-service

/aosp/out/target/product/vsoc_x86$ find ./vendor | grep windfan
./bin/hw/vendor.windfan.cpu@1.0-service
./lib/vendor.windfan.cpu@1.0-adapter-helper.so
./etc/vintf/manifest/vendor.windfan.cpu@1.0-service.xml
./etc/init/vendor.windfan.cpu@1.0-service.rc
```

## Create Vendor SE Linux Policies

```sh
# Define the policy
$ cat device/google/cuttlefish/shared/sepolicy/vendor/file_contexts
/vendor/bin/hw/vendor\.windfan\.cpu@1\.0-service u:object_r:hal_cpu_default_exec:s0
```

```sh
# Define the hal_cpu_default SELinux type
$ cat device/google/cuttlefish/shared/sepolicy/vendor/hal_cpu_default.te
type hal_cpu_default, domain;
type hal_cpu_default_exec, exec_type, vendor_file_type, file_type;
init_daemon_domain(hal_cpu_default);
...
```

Use `audit2allow` tool to convert SELinux audit messages into SELinux allow rules.

```sh
# Add a new type in the hwservice.te
$ cat device/google/cuttlefish/shared/sepolicy/vendor/hwservice.te
type hal_cpu_hwservice, hwservice_manager_type;

# Define vendor.windfan.cpu::ICpu is a hal_cpu_hwservice object tyep in hwservice_contexts
$ cat device/googl/cuttlefish/shared/sepolicy/vendor/hwservice_contexts
vendor.windfan.cpu::ICpu u:object_r:hal_cpu_hwservice:s0
```

## Create HIDL Test App

```sh
# 1. Create a test folder for source file
$ cat vendor/windfan/interfaces/cpu/1.0/test/CpuTest.cpp
...
android::sp<ICpu> cpuService = ICpu::getService();
bool result = cpuService->setScalingGovernor(argv[1]);

# 2. Create .bp file
# Resulting binary will be in /vendor/bin/hw/windfan_cpu_tester
$ vi vendor/windfan/interfaces/cpu/1.0/test/Android.bp

# 3. Add the binary module to the original makefile
$ cat device/google/cuttlefish/shared/windfan.mk
PRODUCT_PACKAGES_DEBUG += windfan_cpu_tester

# 4. Run AOSP build and check outputs
/aosp/out/target/product/vsoc_x86$ find ./vendor | grep windfan
./bin/hw/windfan_cpu_tester
```

## Create JNI for HIDL Native C++ Client

### Define the JNI Functions and Register them to JAVA Class

```sh
# 1. Create a libs/jni folder
$ mkdir -p vendor/windfan/libs/jni

# 2. Create JNI functions for HIDL client in java class
$ vi vendor/windfan/libs/jni/com_windfan_Cpu.cpp
static void nativeInit(JNIEnv* env, jobject) {...}
static jboolean setScalingGovernor(JNIEnv* env, jclass, jstring governor) {}

# 3. In the same file, register above native methods to java class
$ vi vendor/windfan/libs/jni/com_windfan_Cpu.cpp
int register_windfan_Cpu(JNIEnv* env) {
    registerNativeMethods(env, classPathName, methods, ...);
}
```

### Load the Native Library

```sh
# Add above CPU library when system loading
$ vi vendor/windfan/libs/jni/onload.cpp
cpu::register_windfan_Cpu(env);
```

### Define the Module

Define the `libwindfan` module in the [`vendor/windfan/libs/jni/Android.bp`](vendor/windfan/libs/jni/Android.bp). And add the module to the custom HAL makefile [`windfan.mk`](device/google/cuttlefish/shared/windfan.mk).

```sh
# Run AOSP build and check outputs
/aosp/out/target/product/vsoc_x86$ find ./vendor | grep windfan
./lib/libwindfan.so
```

## Create Java SDK Library

In Java, calling JNI functions needs to preload the native shared library. And then we can create a Java SDK library wrapping the JNI, and reuse it in different vendor apps.

```sh
# 1. Create java library folder and source file
$ mkdir -p vendor/windfan/libs/java/com/windfan/
$ cat vi vendor/windfan/libs/java/com/windfan/Cpu.java
public class Cpu {
    private static native boolean nativeSetScalingGovernor(String governor);
    public boolean setScalingGovernor(String governor) {
        return nativeSetScalingGovernor(governor);
    }
}

# 2. Define the module
$ vi vendor/windfan/libs/Android.bp
java_sdk_library {
    name: "com.widnfan",
    ...
}

# 3. Add the module to custom HAL makefile
$ cat device/google/cuttlefish/shared/windfan.mk
PRODUCT_PACKAGES += com.windfan

# 4. Run AOSP build and check outputs
/aosp/out/target/product/vsoc_x86$ find ./vendor | grep windfan
./etc/permissions/com.windfan.xml
./framework/oat/x86/com.windfan.vdex
./framework/oat/x86/com.windfan.odex
./framework/oat/x86/com.windfan.impl.vdex
./framework/oat/x86/com.windfan.impl.vdex
./framework/oat/com.windfan.jar
./framework/oat/com.windfan.impl.jar
```

## Create Java Application

### Define the Service Class

```sh
$ cat ./vendor/windfan/apps/WindfanServices/src/java/com/windfan/WindfanServices.java
public class WindfanSerivces extends Application {
    private static Cpu cpu; // java -> jni -> hidl
    public void onCreate() {
        cpu = new Cpu();
        cpu.setScalingGovernor("performance");
        ... ...
    }
}
```

### Define Broadcast Receiver

`BroadcastReceiver` class registers in the `ActivityManagerService` with a string `IntentFilter`.

```sh
$ cat ./vendor/windfan/apps/WindfanServices/src/java/com/windfan/WindfanServices.java
public class WindfanSerivces extends Application {
    WindfanBroadcastReceiver windfanBroadcastReceiver = new WindfanBroadcastReceiver();
    public void onCreate() {
        ... ...
        IntentFilter filter = new IntentFilter("com.windfan.CPU");
        registerReceiver(windfanBroadcastReceiver, filter);
    }
}

$ cat ./vendor/windfan/apps/WindfanServices/src/java/com/windfan/WindfanBroadcastReceiver.java
public class WindfanBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {...}
}
```

### Build the Android App

```sh
# Describe the app
$ vi ./vendor/windfan/apps/WindfanServices/src/java/com/windfan/AndroidManifest.xml
# Create string resource
$ vi ./vendor/windfan/apps/WindfanServices/res/values/strings.xml
# Define the app build
$ vi ./vendor/windfan/apps/WindfanServices/Android.bp
```

## Design Application UI (Activity)

### Create Layout along with Resource

```sh
$ cat ./vendor/windfan/apps/WindfanServices/res/layout/activity_cpu.xml
    <Button
        android:id="@+id/performanceButton"
        android:onClick="onClick"
        android:text="performance"

$ vi ./vendor/windfan/apps/WindfanServices/res/values/styles.xml
$ vi ./vendor/windfan/apps/WindfanServices/res/values/colors.xml
```

### Define UI Activity

```sh
$ cat ./vendor/windfan/apps/WindfanServices/src/java/com/windfan/CpuActivity.java
public void onClick(View view) {
    String setValue = ((Button)view).getText().toString();
    setSuccess(..., WindfanServices.getCpu().setScalingGovernor(setValue));
    ... ...
}
```

### Build the Android App

```sh
# Define the app activity
$ cat ./vendor/windfan/apps/WindfanServices/src/java/com/windfan/AndroidManifest.xml
    <activity android:name=".CpuActivity"
        android:label="@string/app_name"
        android:exported="true" >
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>

# Add static libs to the the app build file
$ cat ./vendor/windfan/apps/WindfanServices/Android.bp
static_libs: [
    ... ...
```
