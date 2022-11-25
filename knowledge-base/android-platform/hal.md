# HAL Interface

Android build system will run `hidl-gen` ([doc](https://android.googlesource.com/platform/system/tools/hidl/+/master/README.md)) on the `.hal` file(s) to generate IPC classes:
+ **proxy** or its native name - **Bp (Binder Proxy)** - for calling process
+ **stub** or its native name - **Bn (Binder native)** - for service process

## Package prefixes and locations

| Package Prefix | Location | Interface Types |
|----------------|----------|-----------------|
| `android.hardware.*` | `hardware/interfaces/*` | HAL |
| `android.frameworks.*` | `frameworks/hardware/interfaces/*` | frameworks/ related |
| `android.system.*` | `system/hardware/interfaces/*` | system/ related |
| `android.hidl.*` | `system/libhidl/transport/*` | core |

## Interface definition

The `.hal` file defines an interface. If the interface is without an explicit `extends` declaration, it implicitly extends from the `android.hidl.base@1.0::IBase`.

The `IBase` interface contains reserved methods that cannot be redeclared in user-defined interface: `ping`, `interfaceChain`, `linkToDeath`, `debug`, ...
