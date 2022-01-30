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
