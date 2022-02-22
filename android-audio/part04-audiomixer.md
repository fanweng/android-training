# AudioMixer

Audio mixing is a process that multiple sounds are combined into one or more channels.

+ `setParameter()`
+ `setResampler()`
+ `adjustVolumeRamp()`
+ `setBufferProvider()`
+ `getTrackName()`

The core of `MixThread()` is an `mState` variable (`state_t`). All mixing work will be reflected in this variable - [state_t](https://android.googlesource.com/platform/frameworks/av/+/android-6.0.1_r81/services/audioflinger/AudioMixer.h#284).

`track_t` is a description of each track - [track_t](https://android.googlesource.com/platform/frameworks/av/+/android-6.0.1_r81/services/audioflinger/AudioMixer.h#173) - using `setParameter()`.

#### `threadLoop_mix()`

`threadLoop_mix()` -> `mAudioMixer->process(pts)` -> `mState.hook()`

`hook()` is the function pointer to the next operation in the mixing process:
+ `process__validate()`
+ `process__nop()`
+ `process__genericNoResampling()`
+ `process__genericResampling()`
+ `process__OneTrack16BitsStereoNoResampling()`
