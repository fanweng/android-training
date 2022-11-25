# Android Manifest

Android system reads the app's `AndroidManifest.xml` file, so it knows the `activity` exists so as to start that app component.

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.android.helloworld">

    <application
        ... ...
        <activity android:name=".MainActivity">
            <intent-filter>
               <action android:name="android.intent.action.MAIN" />
               <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```



## Activity Attributes
```java
<activity
   android:name=".SecondActivity"
   android:label="@string/activity2_name"
   android:parentActivityName=".MainActivity"
   android:launchMode="standard">
   android:taskAffinity="com.example.android.myapp.newtask">
   <!-- More attributes ... -->
</activity>
```

+ Task affinity
Indicate which task an `Activity` prefers to belong to when that `Activity` instance is launched. By default, each `Activity` belongs to the app that launched it.
