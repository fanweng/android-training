# Security

+ First check DAC
+ Then check MAC

```sh
$ adb shell setenforce 0 # stop SEAndroid security
$ adb shell setenforce 1 # start
$ adb shell getenforce   # check the security enforcement status
```

## User ID and Discretionary Access Control (DAC)

[DAC](DAC.md) is based on the Linux file UID/GID, including the read/write/execute permissions.

## Mandatory Access Control - SE Linux

[MAC](MAC.md) 

## Private Application Files

`/data/data` is used by different apps to access their private files and is using DAC to enforce access.
