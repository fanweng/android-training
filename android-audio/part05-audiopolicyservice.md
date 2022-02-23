# Audio Policy Service

*AudioPolicyService* is also started by the *mediaserver*, the same as *AudioFlinger*.

A large part of *AudioPolicyService* management work is done by *AudioPolicyManager*:
+ input and output device connection status
+ system's audio strategy switching
+ volume/audio parameter settings

## `AudioPolicyService::onFirstRef()`

#### Input and output device management

[Supported Audio Devices](https://android.googlesource.com/platform/system/media/+/android-6.0.1_r81/audio/include/system/audio.h#578)

```c++
enum {
    AUDIO_DEVICE_NONE                     = 0x0,
    /* reserved bits */
    AUDIO_DEVICE_BIT_IN                   = 0x80000000,
    AUDIO_DEVICE_BIT_DEFAULT              = 0x40000000,
    /* output devices */
    AUDIO_DEVICE_OUT_EARPIECE             = 0x1,
    AUDIO_DEVICE_OUT_SPEAKER              = 0x2,
    /* input devices */
    AUDIO_DEVICE_IN_COMMUNICATION         = AUDIO_DEVICE_BIT_IN | 0x1,
    AUDIO_DEVICE_IN_AMBIENT               = AUDIO_DEVICE_BIT_IN | 0x2,
    // ... ...
}

newOutputDevice = DEVICE_OUT_SPEAKER | DEVICE_OUT_WIRED_HEADPHONE;
setOutputDevice(mHardwareOutput, newOutputDevice);
```

#### Audio Policy Service Operations

[Audio Policy Service Operations](https://android.googlesource.com/platform/frameworks/av/+/android-6.0.1_r81/services/audiopolicy/service/AudioPolicyService.cpp#1127)

```c++
namespace {
    struct audio_policy_service_ops aps_ops = {
        .open_output           = aps_open_output,
        .open_duplicate_output = aps_open_dup_output,
        .close_output          = aps_close_output,
        // ... ...
    };
};
```

This implementation allows audio HW manufacturers to develop their own audio strategy. A policy will be generated through the audio policy device below.

```c++
rc = mpAudioPolicyDev->create_audio_policy(mpAudioPolicyDev, &aps_ops, this, &mpAudioPolicy);
```

#### Audio Policy Effects

This loads default AudioEffects from the config file: `/system/etc/audio_effects.conf`

```c++
sp<AudioPolicyEffects>audioPolicyEffects = new AudioPolicyEffects();
```

## Audio Policy Configuration File

An example of [audio_policy.conf](https://android.googlesource.com/device/lge/bullhead/+/android-6.0.1_r81/audio_policy.conf)

```c++
udio_hw_modules {
  primary {
    outputs {
      primary {
        sampling_rates 44100|48000
        channel_masks AUDIO_CHANNEL_OUT_STEREO
        formats AUDIO_FORMAT_PCM_16_BIT
        devices AUDIO_DEVICE_OUT_EARPIECE|AUDIO_DEVICE_OUT_SPEAKER|AUDIO_DEVICE_OUT_WIRED_HEADSET|AUDIO_DEVICE_OUT_WIRED_HEADPHONE|AUDIO_DEVICE_OUT_LINE|AUDIO_DEVICE_OUT_ALL_SCO
        flags AUDIO_OUTPUT_FLAG_FAST|AUDIO_OUTPUT_FLAG_PRIMARY
      }
      raw {
      // ... ...
```

## How AudioPolicyManger interfaces with AudioFlinger?

`AudioPolicyManagerBase` calls `mpClientInterface->loadHwModule()` (i.e. `AudioPolicyCompatClient::loadHwModule()`) -> `mServiceOps->load_hw_module()` (i.e. `aps_ops->load_hw_module`) -> `aps_load_hw_module()` -> `af->loadHwModule()` (methods of `AudioFlinger`)
