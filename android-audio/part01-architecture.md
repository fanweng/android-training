# Part 1 - Android Audio Architecture

## 1. Hardware - Sound Card

Computer hardware for audio, e.g. sound card contains:
+ connectors for audio accessories
+ audio circuits on card
+ interfaces to computer bus

To check sound card installed on Ubuntu:
```sh
$ cat /proc/asound/cards
```

## 2. Kernel Space - Device Driver Architecture

#### Open Sound System (OSS)

It is based on standard Unix devices system calls, i.e. POSIX read, write, ioctl, etc.

OSS is old and has limitations:
+ insufficient support for newer audio features
+ lack of support for the latest kernel features

#### Advanced Linux Sound Architecture (ALSA)

ALSA (https://git.alsa-project.org/) succeeds the older OSS in the Linux world to provide an API for sound card device drivers.

ALSA main nodes are (`Cx` for Card X, `Dy` for Device Y):
+ information interface: `/proc/asound`
+ control interface: `/dev/snd/controlCx`
+ mixer interface: `/dev/snd/mixerCxDy`
+ pcm interface: `/dev/snd/pcmCxDy`
+ raw MIDI interface: `/dev/snd/midiCxDy`
+ sequencer interface: `/dev/snd/seq`
+ timer interface: `/dev/snd/timer`

#### TinyALSA

TinyALSA (https://github.com/tinyalsa/tinyalsa) is a stripped down version of ALSA in the Linux kernel.

It aims to:
+ provide a basic pcm and mixer API
+ avoid supporting complex and unnecessary operations
+ provide comprehensive documentation

## 3. User Space - Audio Framework

#### Audio HAL

HAL is directly accessed by *AudioFlinger* to talk to the device driver in the kernel space.

#### Framework

Android provides useful classes and services for controlling audio system.

#### Application

Application that users are interacting with.
