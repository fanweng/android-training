# AudioTrack

## 1. `MediaPlayer`
+ play audio source in multiple formats: mp3, flac, wma, ogg, wav, etc.
+ will create an audio decoder and and `AudioTrack` in the native layer
+ convenient to use for a wide range of applications

https://developer.android.com/reference/android/media/MediaPlayer.html

## 2. `AudioTrack`
+ only play decoded PCM data streams
+ useful in some applications that require very demanding audio delay

https://developer.android.com/reference/android/media/AudioTrack.html

### 2.1 Java API

#### Transfer Mode
+ `MODE_STATIC`
+ `MODE_STREAM`

#### Stream Type

AudioPolicyService will use stream type to do volume management and select appropriate output device.

+ `STREAM_VOICE_CALL`
+ `STREAM_SYSTEM`
+ `STREAM_RING`
... ...

#### getMinBufferSize()

Smallest buffer = lowest num of frames * num of channels * sampling depth in bytes

> Frame: a complete sound unit. For example, dual channel has 2 samples, 5.1 channels has 6 samples.

### 2.2 Native API

#### Transfer Mode
+ `TRANSFER_CALLBACK`
+ `TRANSFER_OBTAIN`
+ `TRANSFER_SYNC`
+ `TRANSFER_SHARED`

#### Stream Type
+ `AUDIO_STREAM_VOICE_CALL`
+ `AUDIO_STREAM_SYSTEM`
+ `AUDIO_STREAM_RING`
... ...

#### Output Flag
+ `AUDIO_OUTPUT_FLAG_DIRECT`
    * directly output to the audio device without SW mixing, e.g. HDMI
+ `AUDIO_OUTPUT_FLAG_PRIMARY`
    * output to the main output device, e.g. ringtone
+ `AUDIO_OUTPUT_FLAG_FAST`
    * quickly output to the audio device, e.g. key press
+ `AUDIO_OUTPUT_FLAG_DEEP_BUFFER`
    * can accept a large delay
+ `AUDIO_OUTPUT_FLAG_COMPRESS_OFFLOAD`
    * need to be output to a HW decoder for decoding

### 2.2 Output Stream Devices

| AudioTrack | AudioFlinger | AudioHAL | ALSA/ASoc | Device |
|------------|--------------|----------|-----------|--------|
| AUDIO_OUTPUT_FLAG_PRIMARY          | MixerThread             | primary_out | PCM Driver | Audio Codec |
| AUDIO_OUTPUT_FLAG_FAST             | MixerThread (FastMixer) | low_latency | PCM Driver | Audio Codec |
| AUDIO_OUTPUT_FLAG_DEEP_BUFFER      | MixerThread             | deep_buffer | PCM Driver | Audio Codec |
| AUDIO_OUTPUT_FLAG_COMPRESS_OFFLOAD | OffloadThread           | compress_offload | Offload Driver | Audio DSP (then to Codec) |
| AUDIO_OUTPUT_FLAG_DIRECT           | DirectOutputThread      | multi_channel | HDMI Driver | n/a |

## 3. AudioTrack Data Writing

+ Ring FIFO
    * abnormal states
        - **Block:** AudioFlinger doesn't read data for a long time, AudioTrack can't have free space to write
        - **Underrun:** writing data cannot keep up with reading speed, AudioFlinger cannot obtain data in time

+ AudioTrack
    * finds a free space in the FIFO: `obtainBuffer()`
    * writes the audio data: `memcpy()`
    * updates the writing position: `releaseBuffer()`

+ AudioFlinger
    * finds a readable data block in the FIFO
    * copies the data to the destination buffer
    * updates the reading position
