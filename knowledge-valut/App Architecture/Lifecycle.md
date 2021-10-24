# Activity Lifecycle

[[Activity]] lifecycle is the set of states an activity can be in during its entire lifetime.

Lifecycle callback methods are hooks that you can override in each `Activity` class. Or simply don't implement them if not needed.

![[activity-lifecycle.png]]



#### Created state: `onCreate()`
System calls `onCreate()` method to initialize the `Activity` which is either launched by tapping the app icon or started by another `Activity` with [[Intent]].

This is the only callback method that you **MUST** implement in the `Activity` class, to perform basic app startup logic, such as setting up the UI, assigning class-scope variables, or setting up background tasks.

```java
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // The activity is being created.
}
```



#### Started state: `onStart()`
+ *Created* state can move to the *Started* state
+ [[Navigation#Back Navigation]] and [[Navigation#Up Navigation]] can also move from *Stopped* to the *Started* state

When an `Activity` is in the *Started* state and visible on the screen, the user **cannot** interact with it until `onResume()` is called.

```java
@Override
protected void onStart() {
    super.onStart();
    // The activity is about to become visible.
}
```



#### Resumed/Running state: `onResume()`
The *Resumed* state is actually the running state, and the `Activity` is in the foreground.

Typically only implement `onResume()` as a counterpart to `onPause()`. For example, if in the `onPause()` method you halt any animations, you would start those animations again in `onResume()`.

```java
@Override
protected void onResume() {
    super.onResume();
    // The activity has become visible (it is now "resumed").
}
```



#### Paused state: `onPause()`
Indicating the user *may be* leaving the `Activity`:
+   The `Activity` is going into the background, but has not yet been fully stopped. This is the first indication that the user is leaving your `Activity`.
+   The `Activity` is only partially visible on the screen, because a dialog or other transparent `Activity` is overlaid on top of it.
+   In multi-window or split screen mode (API 24), the `Activity` is displayed on the screen, but some other `Activity` has the user focus.

Useful to:
+ stop animation or video playback
+ release any hardware-intensive resources
+ commit unsaved `Activity` changes, e.g. draft emal)

Requirements:
+ execute quickly, not for CPU-intensive operations, e.g. write persistent data to database
+ because the app may still be visible on screen as it passes through the *Paused* state, any delay can slow the transistion to the next `Activity`

```java
@Override
protected void onPause() {
    super.onPause();
    // Another activity is taking focus 
    // (this activity is about to be "paused").
}
```



#### Stopped state: `onStop()`
`Activity` is no longer visible on the screen but system retains it in the [[Navigation#Back Stack]].

If the resources are low, system might kill the *Stopped* activities.

Useful to:
+ operations that are too heavy for `onPause()`
+ save persistent data
+ release resources
```java
@Override
protected void onStop() {
    super.onStop();
    // The activity is no longer visible (it is now "stopped")
}
```



#### Restarted state: `onRestart()`
Only occurs if a stopped `Activity` is started again.

If you have resources that need to be stopped or started you typically implement that behavior in `onStop()` or `onStart()` rather than `onRestart()`.
```java
@Override
protected void onRestart() {
    super.onRestart();
    // The activity is about to be restarted.
}
```



#### Destroyed state: `onDestroy()`
*Destroyed* state means the activity is shutdown completely.

It can happen when:
+ `finish()` called in the `Activity` to manually shut it down
+ user navigate back to the previous `Activity`
+ in low memory situation in which system would reclaim any *Stopped* `Activity` to free up the resources
+ a device configuration change occurs

`onDestroy()` should NOT be used to save any required data. Use `onPause()` and `onStop()` instead.

```java
@Override
protected void onDestroy() {
    super.onDestroy();
    // The activity is about to be destroyed.
}
```