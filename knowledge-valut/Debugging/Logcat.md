# Logcat

`Log.(e/w/i/d)` sends log messages to the `Logcat` server.

By convention, the log tag is defined as a constant of the `activity` name.

```java
private static final String LOG_TAG = MainActivity.class.getSimpleName();
Log.d(LOG_TAG, "Hello World");
```