# File Storage

Android uses a file system that is similar to disk-based file system. The file-based operations are based on Linux file I/O or the `java.io` package.

File storage areas
+ [[Internal Storage]]
+ [[External Storage]]

| Internal | External |
|----------|----------|
| always available | not always available, user can mount/remove the storage |
| only your app can access the file, unless you explicitly set it r/w | world-reable |
| app uninstallation will remove all app's files from the internal storage | if app is uninstalled, system removes the files from external storage only if they are saved in the directory from `getExternalFileDir()` |
| good to make sure neither user nor other apps to access the file | good for sharing the files without access restrictions |
