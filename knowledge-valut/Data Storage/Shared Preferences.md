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
