# Activity Navigation

Two navigation strategies:
+ Back (temporal) navigation: Back button with **back stack**
+ Up (ancestral) navigation: provided by developer as an option in the app bar



## Back Navigation

#### Back Stack

A set of each [[Activity]] that the user has visited and that can be returned to by the user with the back button.

A new `Activity` starts -> push it onto the back stack and take user focus -> previous `Activity` stops and stays in the stack -> user presses the Back button -> current `Activity` is popped from stack and destroyed -> previous `Activity` resumes



## Up Navigation

Up navigation, sometimes referred to as **ancestral** or **logical** navigation, is used to navigate within an app based on the explicit hierarchical relationships between screens.

With Up navigation, each `Activity` is arranged in a hierarchy, and each "child" `Activity` shows a left-facing arrow in the app bar ![ Up button (in app bar) ](https://google-developer-training.github.io/android-developer-fundamentals-course-concepts-v2/images/2-1-c-activities-and-intents/ic_up-button.png " Up button (in app bar) ") that returns the user to the "parent" `Activity`.

The topmost `Activity` in the hierarchy is usually `MainActivity`, and the user cannot go up from there.

#### Implement up navigation with a parent acitivity

```xml
<application
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:roundIcon="@mipmap/ic_launcher_round"
    android:supportsRtl="true"
    android:theme="@style/AppTheme">
    <!-- The main activity (it has no parent activity) -->
    <activity android:name=".MainActivity">
       <intent-filter>
            <action android:name="android.intent.action.MAIN" />

            <category android:name="android.intent.category.LAUNCHER" />
       </intent-filter>
    </activity>
    <!-- The child activity) -->
    <activity android:name=".SecondActivity"
       android:label = "Second Activity"
       android:parentActivityName=".MainActivity">
       <meta-data
          android:name="android.support.PARENT_ACTIVITY"
          android:value="com.example.android.twoactivities.MainActivity" />
       </activity>
</application>
```