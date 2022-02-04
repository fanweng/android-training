# Vendor HIDL Lab 101: HIDL HAL

**Objective:** creating the HAL API using HIDL

Android build system will run `hidl-gen` ([doc](https://android.googlesource.com/platform/system/tools/hidl/+/master/README.md)) on the `.hal` file(s) to generate IPC classes, named **proxy** and **stub**, or in their native names - **Bp (Binder Proxy)** and **Bn (Binder native)**. 

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
