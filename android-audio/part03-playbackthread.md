# PlaybackThread

When `PlaybackThread::onFirstRef()` is called, it starts the thread and keeps running the `threadLoop()` function.

[PlaybackThread::threadLoop()](https://android.googlesource.com/platform/frameworks/av/+/android-6.0.1_r81/services/audioflinger/Threads.cpp#2718)

+ `threadLoop_standby()`

If the current condition meets *Standby*, `threadLoop()` will call

+ `prepareTracks_l()`

Get the number of currently active tracks, loop through each track.

Prepare data for buffering is the most important work for each track.

Determine whether to remove a track.

Return mixer status.

+ `threadLoop_mix()`

If data preparation is ready `MIXER_TRACKS_READY`, a real mix operation will be executed.

+ `threadLoop_sleepTime()`

If data is not yet ready, AudioFlinger will call this to calculate and perform sleep.

+ `threadLoop_write()`

This writes data to HAL and writes it in chunks to the HW:
1. If an NBAIO sink is present, write to the normal mixer's submix
2. Otherwise, use HAL/AudioStreamOut directly
