# HIDL Service

HIDL Service Implementation:
+ runs in its own process
+ runs code that interfaces with the kernel driver
+ registered with the `hwservicemanager`
+ communication is done via the `hwbinder` kernel driver
+ started at boot by the `init` process
+ runs as a persistent service that can't be killed or disabled by the user
