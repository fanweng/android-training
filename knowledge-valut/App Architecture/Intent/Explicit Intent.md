# Explicit Intent

Specify the receiving activity using the activity's full qualified class name. Usually the components in your own app because you know the package and class name.

## Start an activity with an explicit intent
+ Argument 1 - an application context
+ Argument 2 - specific component to start
```java
Intent messageIntent = new Intent(this, ShowMessageActivity.class);
startActivity(messageIntent);
```
The new `Activity` will appear on the screen and the originating `Activity` is paused.





