# Mandatory Access Control - SE Linux

## SE Linux

+ controls the ability of **subjects** to access **objects** via **policy** rules
+ every component has a label applied known as **security context**
+ **context** label with the form: `user:role:type:level`
+ Android only uses `type` which shows the unique identifier for a group or processes (domains) or files
+ **subjects** are running processes requesting resource access: `ps -AZ`
+ **objects** are files/sockets/processes protected by a security context: `ls -lZ /path`
+ application verifies with the kernel's **security server**
+ to reduce lookup overhead, policy decisions are cached in memory via the **Access Vector Cache (AVC)**

> The `.te` security policy configs are stored under `/system/sepolicy`.

## Mandatory Access Control (MAC)

+ a system-wide security policy that applies to all processes, data, and system operations
+ based on security labels (domains)
+ confines flawed/malicious apps as well as system processes (including those that run as root!)
	+ prevents privilege escalation
+ centralized/manageable device-wide policy

```sh
$ adb shell
vsoc_x86:/ $ id
uid=2000(shell) ... context=u:r:shell:s0
```

## Checking the Missing Permissions

Run your process, and check the logs using `logcat` or `dmesg`:

```sh
$ adb shell logcat
avc: denied { bind } for pid=512 comm="test_proc" scontext=u:r:test_proc:s0 tcontext=u:r:test_proc:s0 tclass=tcp_socket permissive=0
avc: denied { connectto } for pid=512 comm="test_proc" scontext=u:r:test_proc:s0 tcontext=u:r:netd:s0 tclass=unix_stream_socket permissive=0
```
