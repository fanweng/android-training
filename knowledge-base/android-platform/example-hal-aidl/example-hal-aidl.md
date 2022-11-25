# Creating the CPU-related HAL API using Vendor AIDL

+ Application UI - `vendor/app/AppName/`
+ Java Application - `vendor/app/AppName/`
  + android_app (`.apk`)
  + signed with the platform build key
+ AIDL Java Client - `vendor/lib/vendor.interfacename.featurename-V1-java.jar`
  + part of `classes.dex` - `apk:classes.dex:vendor.interfacename.featurename.featureInterfaceClass`
+ ------------ /dev/vndbinder ------------
+ AIDL Service Implementation - `vendor/bin/hw/vendor.interfacename.featurename-service`
  + cc_bianry (executable)
  + registered with the `vndbinder`
+ AIDL Interface - `vendor/lib/vendor.interfacename.featurename-V1-ndk_platform.so`
  + aidl_interface (`.so`)
  + introduced in Android 11
+ Linux Kernel Driver/Service

## Create AIDL HAL Interfaces

Some of the service are being converted to AIDL and some are written from scratch. Along with the AIDL for HAL, `hidl2aidl` tool was introduced in Android 11.

Here, we convert the HIDL service created in the [HIDL for HAL Example](../example-hal-hidl/example-hal-hidl.md) to AIDL.

The output will be a new [Android.bp](vendor/windfan/interfaces/cpu/Android.bp) file, and a new `vendor` folder with an auto-generated [ICpu.aidl](vendor/windfan/interfaces/cpu/vendor/windfan/cpu/ICpu.aidl).

```sh
# 1. Convert HIDL to AIDL
$ hidl2aidl -o vendor/windfan/interfaces/cpu/ -r vendor.windfan:vendor/windfan/interfaces vendor.windfan.cpu@1.0

# 2. Add the module to custom makefile
$ cat device/google/cuttlefish/shared/windfan.mk
PRODUCT_PACKAGES += vendor.windfan.cpu
```

## Hardware Service Implementation

### Implement AIDL Service

The AIDL service framework [service.cpp](vendor/windfan/services/cpu/service.cpp) basically contains:
+ `initWithDriver("/dev/vndbinder")`
  + vendor process links against the `libbinder` userspace library, which opens the binder driver
+ `startThreadPool()`
  + every instance of `libbinder` in a process maintains one thread pool

### Define the service module

Create an [Android.bp](vendor/windfan/services/cpu/Android.bp) for the AIDL service, naming `vendor.windfan.cpu-service`.

Define the service with `init` process so it can start as the HAL class is started - [vendor.windfan.cpu-service.rc](vendor/windfan/services/cpu/vendor.windfan.cpu-service.rc).

Also, define a new *VINTF* AIDL object in a new [vendor.windfan.cpu-service.xml](vendor/windfan/services/cpu/vendor.windfan.cpu-service.xml).

Lastly, add the *VINTF* AIDL object into the [device_framework_matrix.xml](device/google/cuttlefish/shared/device_framework_matrix.xml).

### Create Vendor SE Linux Policies

+ Add a new object label in the [file_contexts](device/google/cuttlefish/shared/sepolicy/vendor/file_contexts)
+ Create a new `cpu_service` label in the [service.te](device/google/cuttlefish/shared/sepolicy/vendor/service.te)
+ Assign the interface in the [service_contexts](device/google/cuttlefish/shared/sepolicy/vendor/service_contexts)
+ Define a new [hal_cpu_service.te](device/google/cuttlefish/shared/sepolicy/vendor/hal_cpu_service.te)

### Extend the Binder Native

The service [Android.bp](vendor/windfan/services/cpu/Android.bp) includes `vendor.windfan.cpu-V1-ndk_platform` library, which source is located at `out/soong/.intermediates/vendor/windfan/interfaces/cpu/` folder.

In the `vendor.windfan.cpu-V1-ndk_platform-source` folder:
+ `ICpu.h` is generated with a pure virtual function
  + `virtual ::ndk::ScopedAStatus setScalingGovernor(const std::string& in_governor, bool* _aidl_return) = 0;`
+ `BnCpu.h` extends from `ICpu.h`

Create a new [`Cpu.h`](vendor/windfan/services/cpu/Cpu.h) and [`Cpu.cpp`](vendor/windfan/services/cpu/Cpu.cpp) to override the `BnCpu.h`.

Update the `srcs` in the [Android.bp](vendor/windfan/services/cpu/Android.bp) to include the `Cpu.cpp` class.

### Add to the Service Manager

`Cpu` class needs to be instantiated in the AIDL service framework [service.cpp](vendor/windfan/services/cpu/service.cpp), and added itself to the Service Manager `vndservicemanager`.

```c++
std::shared_ptr<Cpu> cpu = ndk::SharedRefBase::make<Cpu>();
CHECK_EQ(STATUS_OK, AServiceManager_addService(cpu->asBinder().get(), name.c_str()));
```

### Add the Service to Makefile

```sh
$ cat device/google/cuttlefish/shared/windfan.mk
PRODUCT_PACKAGES += vendor.windfan.cpu-service
```

## Create AIDL Java Client

Android app needs to access the Java proxy class in the AIDL Java client process.

### Make the AIDL Interface Library

In the [Android.bp](vendor/windfan/apps/WindfanServices/Android.bp) of AIDL Java Client, the AIDL interface library `vendor.windfan.cpu-V1-java.jar` needs to be added into the `static_libs` as a dependent.

### Add the AIDL Instance in the Java Client Service

Create a new AIDL `vendor.windfan.cpu.ICpu` instance in the [WindfanServices.java](vendor/windfan/apps/WindfanServices/src/java/com/windfan/WindfanServices.java).

```java
private static vendor.windfan.cpu.ICpu cpuAJ;
public void onCreate() {
    IBinder binder = ServiceManager.getService("vendor.windfan.cpu.ICpu/default");
    cpuAJ = vendor.windfan.cpu.ICpu.Stub.asInterface(binder);
    cpuAJ.setScalingGovernor("conservative");
}
public static vendor.windfan.cpu.ICpu getCpuAJ() {
    return cpuAJ;
}
```

## Create Java Application

### Define UI Activity

Similar to HIDL example, we invoke the AIDL Java client in the [CpuActivity.java](vendor/windfan/apps/WindfanServices/src/java/com/windfan/CpuActivity.java).

```java
public void onClick(View view) {
    String setValue = ((Button)view).getText().toString();
    setSuccess(javaAIDLRetTV, WindfanServices.getCpuAJ().setScalingGovernor(setValue));
}
```

### Add Security Rules

In the [windfanservices.te](device/google/cuttlefish/shared/sepolicy/vendor/windfanservices.te), add two rules:
+ to find `cpu_service` in the `service_manager`
+ to call `hal_cpu_service` via the binder

```
allow windfanservices cpu_service:service_manager find;
allow windfanservices hal_cpu_service:binder call;
```
