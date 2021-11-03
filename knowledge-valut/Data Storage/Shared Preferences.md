# Shared Preferences

+ small amounts of primitive data as *key/value* pairs in a file
+ `SharedPreferences` class
+ file is accessible to all the components of your app but not to other apps



## Shared Preferences vs. Saved Instance State

| **Shared preferences** | **Saved instance state** |
|------------------------|--------------------------|
| Persists *across user sessions*, even if your app is stopped and restarted, or if the device is rebooted | Preserves state data *across activity instances* in the same user session |
| Used for data that should be remembered across user sessions, such as a user's preferred settings or their game score | Used for data that should not be remembered across sessions, such as the currently selected tab, or any current state of an activity |
| Represented by a small number of key/value pairs | Same |
| Data is private to the app | Same |
| Common use is to store user preferences | Common use is to recreate state after the device has been rotated |



## Usage

### Create a shared preferences file

The shared preferences file is customarily named with the *package name* of your app, which is unique and easily associated with your app.

Create the file in the [[Activity#onCreate]] method of the main activity:
```java
private String sharedPrefFile = "com.example.android.hellosharedprefs";
mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
```

### Save shared preferences

Save preferences in the [[Activity#onPause]] state using the `SharedPreferences.Editor` interface.
```java
@Override
protected void onPause() {
  super.onPause();
  SharedPreferences.Editor preferencesEditor = mPreferences.edit();
  preferencesEditor.putInt("count", mCount);
  preferencesEditor.putString("color", mCurrentColor);
  preferencesEditor.apply();
}
```

`apply()` writes out the changes asynchronously. `commit()` synchronously saves the preferences but it is not ideal to use because it can block other operations.