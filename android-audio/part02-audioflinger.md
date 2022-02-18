# AudioFlinger

*AudioFlinger* is the core of the entire audio system in Android.
+ provide the access interface for the upper layer for using audio
+ use the HAL to manage the audio devices

## *AudioFlinger* vs. *AudioPolicyService*

+ AudioPolicyService
    * policy maker
    * when to open the audio interface device
    * what kind of *stream-type* audio corresponds to what device etc.

+ AudioFlinger
    * executor of the strategy defined and decided
    * how to communicate specifically with the audio device
    * how to maintain the audio state sanity in the existing system
    * how to deal with the mixture of multiple audio stream

## Starting AudioFlinger

`init.rc` boot script starts the `mediaserver`.

`mediaserver` starts all **native layer services**, including `AudioFlinger`, `AudioPolicyService`, etc.

`AudioFlinger` is instantiated through the `BinderService` class that adds it to the `ServiceManager` entry. Other processes need to access the `ServiceManager` interface to drive `AudioFlinger` to perform audio processing.
