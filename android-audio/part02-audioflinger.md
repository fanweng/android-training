# AudioFlinger

*AudioFlinger* is the core of the entire audio system in Android.
+ provide the access interface for the upper layer for using audio
+ use the HAL to manage the audio devices

## *AudioFlinger* vs. *AudioPolicyService*

+ AudioPolicyService
    * policy maker
    * when to open the audio interface device
    * what kind of *stream-type* audio corresponds to what device etc.
    * guide AudioFlinger to load device interfaces according to user configs

+ AudioFlinger
    * executor of the strategy defined and decided
    * how to communicate specifically with the audio device
    * how to maintain the audio state sanity in the existing system
    * how to deal with the mixture of multiple audio stream

## Starting AudioFlinger

`init.rc` boot script starts the `mediaserver` (`frameworks/av/media/mediaserver/main_mediaserver.cpp`).

`mediaserver` starts all **native layer services**, including `AudioFlinger`, `AudioPolicyService`, etc.

`AudioFlinger` is instantiated through the `BinderService` class that adds it to the `ServiceManager` entry. Other processes need to access the `ServiceManager` interface to drive `AudioFlinger` to perform audio processing.

## AudioFlinger Opens the Output Channels

AudioFlinger supports three `audio_interfaces`:
+ `AUDIO_HARDWARE_MODULE_ID_PRIMARY`
+ `AUDIO_HARDWARE_MODULE_ID_A2DP`
+ `AUDIO_HARDWARE_MODULE_ID_USB`

`audio_policy.conf` -> `AudioPolicyManagerBase()` -> `AudioFlinger::openOutput()` -> `AudioFlinger::findSuitableHwDev_l()` -> `AudioFlinger::loadHwModule()` -> `AudioHwDevice::hwDevice()` -> `AudioHwDevice::openOutputStream()` -> create `PlaybackThread` (`OffloadThread`/`DirectOutputThread`/`MixerThread`)

[Supported Audio Devices](https://android.googlesource.com/platform/system/media/+/android-6.0.1_r81/audio/include/system/audio.h#578)

## AudioFlinger Puts Audio in the Channel

Take the `MixerThread` as an example:

create an `AudioMixer` object -> check number of channels to mix -> create NBAIO sink `AudioStreamOutSink` -> enter `threadLoop()` at first reference

[AudioFlinger::MixerThread::MixerThread()](https://android.googlesource.com/platform/frameworks/av/+/49dd5cf3469cd755321f8ec10013ad4fcfaf723d/services/audioflinger/AudioFlinger.cpp#2180)

#### How `PlaybackThread` enters thread loop?

Inheritance: `PlaybackThread` <- `ThreadBase` <- `Thread` <- `RefBase`

`Thread` interface class has a method `run()` that must be implemented.

[Thread.h](https://android.googlesource.com/platform/frameworks/native/+/android-4.2.2_r1/include/utils/Thread.h#48)

A strong pointer of `PlaybackThread` will call `onFirstRef()` when it is referenced where the `run()` is invoked. It thus will enter the `threadLoop()`.

```c++
void AudioFlinger::PlaybackThread::onFirstRef()
{
    run(mName, ANDROID_PRIORITY_URGENT_AUDIO);
}
```
