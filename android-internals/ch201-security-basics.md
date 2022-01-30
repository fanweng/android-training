# Security Basics

## User ID and Discretionary Access Control

#### Linux User ID

+ Android assigns each app/package an arbitrary but distinct ID at its installation
+ UID won't change during the app/package's life on the device until it is uninstalled
+ UID is given with an offset from **10000**, e.g. `UID=10101` has an alias of `u0_101`
+ To find the UID:

```sh
$ adb shell dumpsys package your_package_name | grep userId
$ adb shell ps -A | grep your_package_name
```

#### Profile User ID

+ Android supports separate **user profiles** on the device
+ each human user is assigned with a unique ID: 0, 10, 11, 12...
+ each app runs in a separate process for each human user

| Profile User ID | User ID alias | Linux UID |
|-----------------|---------------|-----------|
| 0 (main)        | u0_a101       | 10101     |
| 10              | u10_a101      | 1010101   |
| 12              | u12_a101      | 1210101   |

## Mandatory Access Control - SE Linux

#### SE Linux

+ controls the ability of **subjects** to access **objects** via **policy** rules
+ every component has a label applied known as **security context**
+ **context** label with the form: `user:role:type:level`
+ Android only uses `type` which shows the unique identifier for a group or processes (domains) or files
+ **subjects** are running processes requesting resource access: `ps -AZ`
+ **objects** are files/sockets/processes protected by a security context: `ls -lZ /path`
+ application verifies with the kernel's **security server**
+ to reduce lookup overhead, policy decisions are cached in memory via the **Access Vector Cache (AVC)**

#### Mandatory Access Control (MAC)

+ a system-wide security policy that applies to all processes, data, and system operations
+ based on security labels (domains)
+ confines flawed/malicious apps as well as system processes (including those that run as root!)
    * prevents privilege escalation
+ centralized/manageable device-wide policy
