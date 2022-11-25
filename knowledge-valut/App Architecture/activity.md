# Activity

An `Activity` is a single, focused screen that users can interact with.

Example:
An email app has one activity that shows a list of new emails;
Another activity to compose an email;
Even enables a camera app to start its activity to take a picture;
... ...

Typically it has a [[Layout]] associated with.

Defined in a Java class, e.g. `MainActivity.java`.

```java
package com.example.android.helloword; // package statement defines the app package
import ...							   // import needed libraries

public class MainActivity extends AppCompatActivity {
    // ... Rest of the code for the class.
}
```

https://developer.android.com/guide/components/activities/intro-activities.html

## Activity lifecycle

https://developer.android.com/guide/components/activities/activity-lifecycle

A new activity starts -> previous activity is stopped -> system preserves the activity in the **[[Navigation#Back Stack]]** -> current activity is done -> current activity is popped from stack and destroyed -> previous stack resumes

The activity will be notified by activity lifecycle callback methods `onCreate()`, `onStart()`, `onResume()`, `onPause()`, `onStop()`.

#### `onCreate()`

This callback is a must of the `Activity`.

All essential components of the `Activity` should be initialized here. Most importantly, `setContentView()` to create the primary layout for the `Activity`.

+ **Inflating** the layout: System creates all the initial [[View]]s from the specified [[Layout]] and adds them to the `Activity`.

#### `onPause()`

This callback indicates user is leaving the `Activity`.

Some changes should be committed to the persistent storage beyond the current user session.

## Declare in the manifest

Each `Activity` in the app must be declared in the [[Manifest]] - `AndroidManifest.xml`.

The `<action>` element specifies that this is the "main" entry point to the app. The `<category>` element specifies that this `Activity` should be listed in the system's app launcher

```xml
<activity android:name=".MainActivity" >
   <intent-filter>
      <action android:name="android.intent.action.MAIN" />
      <category android:name="android.intent.category.LAUNCHER" />
   </intent-filter>
</activity>
```