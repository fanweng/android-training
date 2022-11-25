# strace

`strace` is a tool that logs all the system calls a user space process makes to the kernel.

```sh
# Attach to a running process and begin logging
$ strace -p your_pid
strace: Process xxx attached
... 
```
