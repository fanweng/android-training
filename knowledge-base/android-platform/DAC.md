# User ID and Discretionary Access Control (DAC)

DAC is based on the Linux file UID/GID, including the read/write/execute permissions.

## Linux User ID

+ Android assigns each app/package an arbitrary but distinct ID at its installation
+ UID won't change during the app/package's life on the device until it is uninstalled
+ UID is given with an offset from **10000**, e.g. `UID=10101` has an alias of `u0_101`
+ To find the UID:

```sh
$ adb shell dumpsys package your_package_name | grep userId
$ adb shell ps -A | grep your_package_name
$ adb shell cat /proc/your_pid/status | grep -i uid
```

## Profile User ID

+ Android supports separate **user profiles** on the device
+ each human user is assigned with a unique ID: 0, 10, 11, 12...
+ each app runs in a separate process for each human user

| Profile User ID | User ID alias | Linux UID |
|-----------------|---------------|-----------|
| 0 (main)        | u0_a101       | 10101     |
| 10              | u10_a101      | 1010101   |
| 12              | u12_a101      | 1210101   |

## Grant Permissions to App

In the `AndroidManifest.xml`, add `<uses-permission android:name="android.permission.XXX"`.

The associated low-level GIDs with permission names are defined in the `frameworks/base/data/etc/platform.xml`.

```xml
<permission name="android.permission.BLUETOOTH_STACK" >
	<group gid="bluetooth" />
	<group gid="wakelock" />
	<group gid="uhid" />
</permission>

<permission name="android.permission.INTERNET" >
	<group gid="inet" />
</permission>
```

And the GID numbers are defined in the `system/core/libcutils/include/private/android_filesystem_config.h`.

```c++
#define AID_NET_BT 3002       /* bluetooth: create sco, rfcomm or l2cap sockets */
#define AID_INET 3003         /* can create AF_INET and AF_INET6 sockets */
#define AID_WAKELOCK 3010     /* Allow system wakelock read/write access */
#define AID_UHID 3011         /* Allow read/write to /dev/uhid node */
```


The above GIDs can be used in the `init.rc` to start your application.

```
service your_app /system/bin/your_app
	class core
	group system inet
	writepid /dev/cpuset/system-background/tasks
```

