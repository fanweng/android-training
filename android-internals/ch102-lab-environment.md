# Lab Environment

## Virtual Machine

VMWare for Windows: https://www.vmware.com/products/workstation-player.html

VMWare for Mac: https://www.vmware.com/ca/products/fusion/fusion-evaluation.html

Ubuntu 20.04 64-bit PC (AMD64) desktop image: https://releases.ubuntu.com/20.04/

## Setting up Linux Build Environment

1. Install required tools.

Remove `lld` if installation has any issue.

```sh
$ sudo apt-get update
$ sudo apt-get install -y python git zip unzip curl wget llvm git-core gnupg flex bison gperf build-essential make zlib1g-dev gcc-multilib g++-multilib libc6-dev-i386 lib32ncurses5-dev x11proto-core-dev libx11-dev lib32z-dev libgl1-mesa-dev libxml2-utils xsltproc libssl-dev libbz2-dev libreadline-dev libsqlite3-dev libncursesw5-dev xz-utils tk-dev libffi-dev liblzma-dev python-openssl libncurses5 libelf-dev clang lld ssh rsync
```

2. Install `repo` tool. It is used to manage the AOSP git repositories.

```sh
$ mkdir ~/bin
$ vi ~/.bashrc
export PATH=~/bin:$PATH # add to the end of file
$ curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo
$ chmod a+x ~/bin/repo
```

## Downloading / Building the Android Source Code

1. Download AOSP with `android-12` branch.

```sh
$ mkdir ~/aosp
$ cd ~/aosp
$ repo init -u https://android.googlesource.com/platform/manifest -b android-12.0.0_r15
$ repo sync -j4
# in case the download fails, use fewer build threads like "-j1" or "-j2"
```

List of supported devices and corresponding branches/tags: https://source.android.com/setup/start/build-numbers#source-code-tags-and-builds

All possible branches in the `android/platform/manifest.xml`: https://android.googlesource.com/platform/manifest/+refs

2. Build AOSP for specific device

Build the cuttlefish images.

```sh
$ cd ~/aosp
$ source build/envsetup.sh
$ lunch aosp_cf_x86_phone-userdebug
$ m -j8
```

After the build succeeds, a [*super.img*](https://source.android.com/devices/tech/ota/dynamic_partitions/implement) should be generated inside `out/target/product/vsoc_x86/` folder. The *super.img* contains:
+ *system.img*
+ *vendor.img*
+ *product.img*

3. Boot the cuttlefish image

Install the *acloud* command. And **reboot** the Ubuntu after the installation.

```sh
$ acloud setup --host
("y" to all...)
```

Log in back to Ubuntu, and start the cuttlefish image with *acloud*.

```sh
$ cd ~/aosp
$ source build/envsetup.sh
$ lunch aosp_cf_x86_phone-userdebug
$ printconfig
...
TARGET_PRODUCT=aosp_cf_x86_phone

$ acloud create --local-instance 1 --local-image
(Phone interface should show up later...)
```

#### Some other useful commands

AOSP:

```sh
$ source build/envsetup.sh
$ hmm           # list out all available commands
$ lunch         # list out all images/build can be built
$ gomod <mod>   # navigate to the directory of a module
$ outmode <mod> # return the output file for the module
$ croot         # navigate to the aosp directory
```

[acloud](https://android.googlesource.com/platform/tools/acloud/+/refs/heads/master/README.md):

```sh
$ acloud create --local-instance 1 --local-image
$ acloud list
[1]device serial: 0.0.0.0:6520 (local-instance-1) elapsed time: None
$ acloud delete --instance-names local-instance-1
```
