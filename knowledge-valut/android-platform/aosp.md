# Android Open Source Project (AOSP)

AOSP is the official source code for the Android OS. All the projects can be found at: https://android.googlesource.com/

To learn the AOSP source, use the site: https://source.android.com/

To view the developer documentation, use the site: https://developer.android.com/

AOSP doesn't include *Google Mobile Services (GMS)*, so no Google Maps or Google Play source code there
+ non Android-certified devices, e.g. Quest, Kindle, can't run Google Play
	+ but Android apps that don't rely on GMS can be run on those AOSP devices
+ if a device vendor wishes to use Google Play Services, they must run CTS and pass

AOSP doesn't include OEM source code, so to build the Pixel 6, we need OEM driver binaries at: https://developers.google.com/android/drivers

## AOSP Project Structure

After repo syncing the `android-12.0.0_r15` for example, the AOSP project will have following folders:

+ `device/`: contains device customizations for different companies
	+ `device/company_name/`: contains supported devices models
	+ `device/google/`: Google devices supporting Android 12, phones are encoded in fish names
+ `vendor/`: contains non-open source drivers from 3rd-parties
	+ For example, specific phone driver binaries from https://developers.google.com/android/drivers
	+ Extract `tar -xvzf <filename.tgz>`, run the extracted script, move to the `~/aosp/vendor/`

## Virtual Android Devices vs. Android Emulator vs. Physical Device

### Virtual Android device

Ideal with a full fidelity to represent the customized Android platform/framework interactions (better than Android Emulator).

Different primarily in the HAL with physical devices, e.g. hardware-specific implementations.

[Cuttlefish](https://source.android.com/setup/create/cuttlefish) (`device/google/cuttlefish/`) is a configurable virtual Android device that can run *locally* on Linux x86 machine or *remotely* on a cloud service like [Google Cloud Compute Engine](https://cloud.google.com/compute/docs/general-purpose-machines#n1_machines)

Cuttlefish usages: CTS, CI testing, compliance checking for custom platform code and frameworks

### Android Emulator

Similar to Cuttlefish, but it is designed for application development which has many functional hooks that **alter** the behavior of the system.

### Physical Device
