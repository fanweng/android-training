# Android Open Source Project (AOSP)

AOSP is the official source code for the Android OS. All the projects can be found at: https://android.googlesource.com/

To learn the AOSP source, use the site: https://source.android.com/

To view the developer documentation, use the site: https://developer.android.com/

AOSP doesn't include *Google Mobile Services (GMS)*, so no Google Maps or Google Play source code there
+ non Android-certified devices, e.g. Quest, Kindle, can't run Google Play
    * but Android apps that don't rely on GMS can be run on those AOSP devices
+ if a device vendor wishes to use Google Play Services, they must run CTS and pass

AOSP doesn't include OEM source code, so to build the Pixel 6, we need OEM driver binaries at: https://developers.google.com/android/drivers
