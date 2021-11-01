# Internal Storage

You don't need any permissions to save files on the internal storage. Your app always has permission to read and write files in its internal storage directory.



## Create files in two directories

### 1. Permanant storage: `getFileDir()`

```java
File file = new File(context.getFilesDir(), filename);
```

Alternatively,
```java
String filename = "myfile";
String string = "Hello world!";
FileOutputStream outputStream;

try {
  outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
  outputStream.write(string.getBytes());
  outputStream.close();
} catch (Exception e) {
  e.printStackTrace();
}
```

### 2. Temporary storage: `getCacheDir()`

Recommended for small, temporary files < 1MB.

> Note: System may delete temporary files if it runs low memory.

```java
public File getTempFile(Context context, String url) {
    File file;
    try {
        String fileName = "myfile";
        file = File.createTempFile(fileName, null, context.getCacheDir());
    } catch (IOException e) {
        // Error while creating file
    }
    return file;
}
```



## Delete files
For files on the internal storage, ask the `Context` to locate and delete the file.
```java
myContext.deleteFile(fileName);
```