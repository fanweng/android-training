# External Storage

### Obtain permission
Must request permission in the Android [[Manifest]].
`WRITE_EXTERNAL_STORAGE`: read/write
`READ_EXTERNAL_STORAGE`: read only
```xml
<manifest ...>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    ...
</manifest>
```



### Check if external storage is mounted
Because the external storage may be unavailable, we should *ALWAYS* verify the volume is available before accessing it using `getExternalStorageState()`.
```java
/* Checks if external storage is available to at least read */
public boolean isExternalStorageReadable() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state) ||
        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
        return true;
    }
    return false;
}
```



### Get file descriptor
Two file trees specific to your app:
+ public directory: `getExternalStoragePublicDirectory()`
+ private directory: `getExternalFilesDir()`
```java
/* access public external storage directory */
File path = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES);
File file = new File(path, "DemoPicture.jpg");

/* access private external storage directory */
File file = new File(getExternalFilesDir(null), "DemoFile.jpg");
```



### Query storage space
+ try writing the file and prepare to catch an `IOException` if no space left
+ call `getFreeSpace()` or `getTotalSpace()` to check before writing a file



### Delete files
*ALWAYS* delete files that you no longer need, e.g. regularly delete cached files that is created with `getCacheDir()`.

Have the opened file reference to call `delete()` on itself.
```java
myFile.delete();
```

