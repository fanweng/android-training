# Activity Instance State

Normally, when an [[Activity]] is recreated, the state of the `Activity` and any user progress in that `Activity` is lost.

But exceptions:
+ some state info is automatically saved by default, e.g. the state of [[View]] elements in your layout with a unique ID (`android:id` attribute). For example, user input values in the `EditText`
+ [[Intent]] that was used to start the `Activity`, and the info stored in the data or extras. They are available to that `Activity` when it is [[Lifecycle#Restarted state onRestart]].

## Save activity instance state
The `Activity` state is stored as a set of *key/value pairs* in a [[Bundle]] object.

`onSaveInstanceState()` is not a lifecycle callback, but it is called when user is leaving the `Activity` (sometimes before the [[Lifecycle#Stopped state onStop]]).
```java
@Override
public void onSaveInstanceState(Bundle savedInstanceState) {
    super.onSaveInstanceState(savedInstanceState);
    // save your state data to the instance state bundle
    savedInstanceState.putInt("score", mCurrentScore);
    savedInstanceState.putInt("level", mCurrentLevel);
}
```

Don't forget to call the superclass, to make sure the state of the `View` hierarchy is also saved to the `Bundle`.

## Restore activity instance state
 Two places to restore the saved `Activity` instance state:
 + in the [[Lifecycle#Created state onCreate]] callback (better)
 + in the `onRestoreInstanceState()` callback which is called after [[Lifecycle#Started state onStart]] after the `Activity` is created
 ```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    // Always call the superclass first
    super.onCreate(savedInstanceState); 

    // Check if recreating a previously destroyed instance.
    if (savedInstanceState != null) {
        // Restore value of members from saved state.
        mCurrentScore = savedInstanceState.getInt("score");
        mCurrentLevel = savedInstanceState.getInt("level");
    } else {
        // Initialize members with default values for a new instance...
    }
    // ... Rest of code
}
```
 
 