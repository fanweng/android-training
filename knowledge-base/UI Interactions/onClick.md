# onClick

A *click* event can occur on the clickable [[View]], such as `Button`, `ImageButton`, `ImageView`, etc.

### Implementation
1. Associate the event-handler method to the `View`
The following XML code sets the `onClick` event handler to `showToast()`.
```
<Button
    android:id="@+id/button_toast"
    android:onClick="showToast"
```

2. Write a Java method to perform specific actions as the event handler
In the above XML file, click the `onClick` attribute statement, press **Alt+Enter** to select **Create onClick event handler**. Select the `Activity` associated with the [[layout]] file, Android Studio will create the placeholder as shown below.
```
public void showToast(View view) {
        // Do something in response to the button click.
}
```