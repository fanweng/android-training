# AudioFlinger

*AudioFlinger* is the core of the entire audio system in Android.
+ provide the access interface for the upper layer for using audio
+ use the HAL to manage the audio devices

`init.rc` boot script starts the `mediaserver`.

`mediaserver` starts all **native layer services**, including `AudioFlinger`, `AudioPolicyService`, etc.

`AudioFlinger` is instantiated through the `BinderService` class that adds it to the `ServiceManager` entry. Other processes need to access the `ServiceManager` interface to drive `AudioFlinger` to perform audio processing.
