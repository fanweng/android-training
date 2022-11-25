# Gradle

+ `build.gradle` is the Android build file name.

When making changes to the `buidl.gradle`, it requires to **sync** the project files, which imports the build configuration changes and runs checks if build errors.

#### `build.gradle (Project: AppName)`

This file is the *top-level* build file for the entire project, located in the root project folder, which defines build configurations that apply to all modules in the project.

#### `build.gradle (Module: app)`

This build file is for each individual module, providing customization at the *app-level*, such as declaring dependencies. It can also override settings specified in the top-level `build.gradle`.
