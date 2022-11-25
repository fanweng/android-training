# Share data using `ShareCompat.IntentBuilder`

With the `ShareCompat.IntentBuilder` class we don't need to create or send an [[Implicit Intent]] for the share action.
```java
ShareCompat.IntentBuilder
    .from(this)         // information about the calling activity
    .setType(mimeType)  // mime type for the data
    .setChooserTitle("Share this text with: ") //title for the app chooser
    .setText(txt)       // intent data
    .startChooser();    // send the intent
```
