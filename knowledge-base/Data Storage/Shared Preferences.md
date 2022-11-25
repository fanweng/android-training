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

### Restore shared preferences

Restore shared preferences in the [[Activity#onCreate]]. The `get*()` method takes two arguments: `key` and a default value if the `key` cannot be found.
```java
mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
if (savedInstanceState != null) {
    mCount = mPreferences.getInt("count", 1);
    mCurrentColor = mPreferences.getString("color", "white");
}
```

### Clear shared prereferences

Calls to `put()` and `clear()` can be combined. But when `apply()`, the `clear()` is *ALWAYS* done first, regardless of whether you called `clear()` before or after the `put()` method in the editor.
```java
SharedPreferences.Editor preferencesEditor = mPreferences.edit();
preferencesEditor.putInt("number", 42);
preferencesEditor.clear();
preferencesEditor.apply();
```

### Listen for preference changes

Register the `SharedPreferences.OnSharedPreferenceChangeListener` in the [[Lifecycle#Resumed Running state onResume]]. Unregister it in the [[Lifecycle#Paused state onPause]] callback.
```java
@Override
protected void onResume() {
    super.onResume();
    getPreferenceScreen().getSharedPreferences()
            .registerOnSharedPreferenceChangeListener(this);
}

@Override
protected void onPause() {
    super.onPause();
    getPreferenceScreen().getSharedPreferences()
            .unregisterOnSharedPreferenceChangeListener(this);
}
```

The `SharedPreferences.OnSharedPreferenceChangeListener` interface only has one callback method `onSharedPreferenceChanged()`. Implement the interface as a part of your activity.
```java
public class SettingsActivity extends PreferenceActivity
                              implements OnSharedPreferenceChangeListener {
    public static final String KEY_PREF_SYNC_CONN = "pref_syncConnType";

    // ...

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_PREF_SYNC_CONN)) {
            Preference connectionPref = findPreference(key);
            connectionPref.setSummary(sharedPreferences.getString(key, ""));
        }
    }
}
```

When registering the callback, the preference manager doesn't currently store a reference to the listener. We must hold onto a reference to the listener, or it will be susceptible to garbage collection.

Keep a reference to the listener as a class memeber variable in an object such as an activity that will exist as long as you need the listener.
```java
SharedPreferences.OnSharedPreferenceChangeListener listener =
    new SharedPreferences.OnSharedPreferenceChangeListener() {
       public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
          // listener implementation
       }
};

prefs.registerOnSharedPreferenceChangeListener(listener);
```