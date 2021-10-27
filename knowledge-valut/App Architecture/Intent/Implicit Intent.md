# Implicit Intent

Not Specify the activity or other component to receive the intent. Instead, declare a general action to perform, and the Android system matches your request to an activity or other component that can handle the requested action.

+ If only one activity matches, system will launch that
+ If multiple activities match, user is presented with an app chooser



## How system knows which activity is a match?

An `Activity` registers itself with the system as being able to handle an implicit `Intent` with **Intent filters**, declared in the Android [[Manifest]].



## Intent objects and fields
+ `Intent` data: URI of an object to act on
+ `Intent` extras: bits of additional data
+ `Intent` flags
+ `Intent` action: action for the receving activity to perform
+ `Intent` category: optional, category of component that should handle the `Intent`
+ data type: MIME type of data the activity should operate on



## Start an activity with an implicit intent
1. Create implicit intent object
```java
Intent sendIntent = new Intent();
sendIntent.setAction(Intent.ACTION_SEND);
sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
sendIntent.setType("text/plain");
```

2. Use `createChooser()` to invoke a chooser dialog for user to select the app
```java
// Always use string resources for UI text.
String title = getResources().getString(R.string.chooser_title);
// Create the wrapper intent to show the chooser dialog.
Intent chooser = Intent.createChooser(sendIntent, title);
```

3. Resolve the activity before starting it
If sending the `Intent` and there is no appropriate match, the app will crash. To verify if there is an activity:
```java
if (sendIntent.resolveActivity(getPackageManager()) != null) {
    startActivity(chooser);
}
```



## Receive an implict intent

### Define `Intent` filter
`<intent-filter>` elements in the `AndroidManifest.xml` tells the system what `Intent` can be accepted by this `Activity`.

An `Intent` filter may contain the follwoing elements:
+ `<action>`: action that the activity accepts
+ `<data>`: type of data accepted, e.g. MIME or URI
+ `<category>`

```xml
<activity android:name="ShareActivity">
    <intent-filter>
        <action android:name="android.intent.action.SEND"/>
        <category android:name="android.intent.category.DEFAULT"/>
        <data android:mimeType="text/plain"/>
    </intent-filter>
</activity>
```

### Handle the received `Intent`
Same as they way of [[Explicit Intent#Retrieve the data from the intent in the started activity]]