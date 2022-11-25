# Intent

Intent is a *message object* that makes a request to the Android Runtime to start/activate an [[Activity]] or other app component in your app or in some other app.



## Types
+ [[Explicit Intent]]
+ [[Implicit Intent]]



## Intent objects and fields
+ `Activity` class name: who should receive the intent
+ `Intent` data: URI of an object to act on
+ `Intent` extras: bits of additional data
+ `Intent` flags



## Handle a new intent

When Android routes an `Intent` to an *existing* `Activity` instance, the system calls the `onNewIntent()` callback method, usually just before the `onResume()` method.

If not `setIntent()` in the `onNewIntent()`, the `getIntent()` method will always retains the original `Intent` that launched the `Activity` instance.
```java
@Override 
public void onNewIntent(Intent intent) { 
    super.onNewIntent(intent); 
    // Use the new intent, not the original one
    setIntent(intent); 
}
```