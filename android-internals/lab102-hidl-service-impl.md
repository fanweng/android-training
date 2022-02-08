# Vendor HIDL Lab 102: HIDL Service Implementation

**Objective:** add an HIDL service implementation that would implement the stub class from the Lab 101 HIDL interface.

HIDL Service Implementation:
+ runs in its own process
+ runs code that interfaces with the `cpufreq` kernel driver
+ registered with the `hwservicemanager`
+ communication is done via the `hwbinder` kernel driver
+ started at boot by the `init` process
+ runs as a persistent service that can't be killed or disabled by the user

1. Define a new `Cpu` struct extending the `ICpu`

In the Lab 101, *soong* build system should create a new C++ header file `ICpu.h`.

Src: [out/soong/.intermediates/vendor/windfan/interfaces/cpu/1.0/vendor.windfan.cpu@1.0_genc++_headers/gen/vendor/windfan/cpu/1.0/ICpu.h](./lab1/out/soong/.intermediates/vendor/windfan/interfaces/cpu/1.0/vendor.windfan.cpu@1.0_genc++_headers/gen/vendor/windfan/cpu/1.0/ICpu.h)

```c++
struct ICpu : public ::android::hidl::base::V1_0::IBase {
//... ...
    virtual ::android::hardware::Return<void> getScalingGovernor(getScalingGovernor_cb _hidl_cb) = 0;

    virtual ::android::hardware::Return<bool> setScalingGovernor(const ::android::hardware::hidl_string& governor) = 0;
//... ...
}
```

The `getScalingGovernor` and `setScalingGovernor` are pure virtual functions in the `ICpu.h` so we need to implement them.

First, create a new `Cpu.h` defining a new `Cpu` struct that extends the `ICpu`.

Src: [vendor/windfan/interfaces/cpu/1.0/default/Cpu.h](./lab1/vendor/windfan/interfaces/cpu/1.0/default/Cpu.h)
```c++
struct Cpu : public ICpu {
public:
    Return<void> getScalingGovernor(getScalingGovernor_cb _hidl_cb) override;
    Return<bool> setScalingGovernor(const hidl_string& governor) override;
};
```
