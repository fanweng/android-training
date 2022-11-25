# Explicit Intent

Specify the receiving activity using the activity's full qualified class name. Usually the components in your own app because you know the package and class name.

> An explicit Intent is always delivered to its target, regardless of any Intent filters the component declares. Conversely, if an Activity does not include Intent filters, it can only be launched with an explicit Intent.

## Start an activity with an explicit intent
+ Argument 1 - an application context
+ Argument 2 - specific component to start
```java
Intent messageIntent = new Intent(this, ShowMessageActivity.class);
startActivity(messageIntent);
```
The new `Activity` will appear on the screen and the originating `Activity` is paused.



## Pass data to another activity

#### Add intent data
```java
// A web page URL
messageIntent.setData(Uri.parse("http://www.google.com")); 
// a Sample file URI
messageIntent.setData(Uri.fromFile(new File("/sdcard/sample.jpg")));
// Custom URI 
messageIntent.setData(Uri.parse("custom:" + dataID + buttonId));
... ...
```

#### Add intent extras

The extra keys are usually defined as a costants that begins with `EXTRA_`. And use the `putExtra()` to assign the *value* to the each *key*.
```java
public final static String EXTRA_MESSAGE = "com.example.mysampleapp.MESSAGE";
public final static String EXTRA_POSITION_X = "com.example.mysampleapp.X";
messageIntent.putExtra(EXTRA_MESSAGE, "this is my message");
messageIntent.putExtra(EXTRA_POSITION_X, 100);
```

Alternatively, use a new [[Bundle]] and populate that `Bundle` with your `Intent` extras.
```java
Bundle extras = new Bundle();
extras.putString(EXTRA_MESSAGE, "this is my message");
extras.putInt(EXTRA_POSITION_X, 100);
messageIntent.putExtras(extras);
```

#### Retrieve the data from the intent in the started activity

```java
Intent intent = getIntent();
Uri locationUri = intent.getData();

// extras retrieval 1
String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE); 
int positionX = intent.getIntExtra(MainActivity.EXTRA_POSITION_X);

// extras retrieval 2
Bundle extras = intent.getExtras();
String message = extras.getString(MainActivity.EXTRA_MESSAGE);
```



## Get data back from an activity

#### Use `startActivityForResult()` to launch the activity

Instead of using `startActivity()`, call `startActivityForResult()`. The request code is an integer to identify the request results returned by the activity.
```java
public static final int TEXT_REQUEST = 3;
startActivityForResult(messageIntent, TEXT_REQUEST);
```

#### Return a response from the launched activity

Create a new return `Intent` rather than reusing the origial `Intent` object sent by the previous activity.
```java
Intent returnIntent = new Intent();
```

The response codes are defined by the `Activity` class, also can be customized in the originateing `Activity`.
```java
public final static String EXTRA_RETURN_MESSAGE = "com.example.mysampleapp.RETURN_MESSAGE";
messageIntent.putExtra(EXTRA_RETURN_MESSAGE, mMessage);
setResult(RESULT_OK, returnIntent);
```

Call `finish()` to close the `Activity` and resume the originating `Activity`.
```java
finish();
```

#### Read response data in the originating activity

Implement the `onActivityResult()` callback method to handle the return data.
```java
public void onActivityResult(int requestCode, int resultCode,  Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == TEXT_REQUEST) {
        if (resultCode == RESULT_OK) {
            String reply = data.getStringExtra(SecondActivity.EXTRA_RETURN_MESSAGE);
               // process data
        }
    }
}
```





