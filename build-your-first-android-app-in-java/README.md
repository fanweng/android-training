# Build Your First Android App in Java

https://developer.android.com/codelabs/build-your-first-android-app#0

## `AndroidManifest.xml`

This file describes all the components of the Android app.

The content will be read by the Android runtime system when the app is executed.

## Layout Editor

Each screen in the app is associated with one or more **fragments**.

Each visible fragment in the app has a **layout** that defines the user interface of the fragment.

Each element in a layout is a **view**.

Layout is defined in the XML file under the `res/layout/` directory. In the layout editor, we can define and modify the the layout either by coding the XML or by using the interactive visual editor.

Every layout must have a **root view** that contains all the other views. The root view is always a **view group**.

In the [fragment_first.xml](./app/src/main/res/layout/fragment_first.xml) file, `ConstraintLayout` is the view group, which contains a `TextView` and a `Button`.

All views must have **layout_width** and **layout_height** properties:
+ `match_parent`: the size of the screen
+ `wrap_content`: just big enough to enclose the content inside the view
+ `match_constraint`: as big as whatever it's constrained to

## Resource

### `values/colors.xml`

A color can be defined as 3 hex numbers (0x00 to 0xFF) representing the red, blue and green components.

Three hex numbers can be also led by an alpha value (0x00 to 0xFF) representing the transparency (0 fully transparent to 100% fully opaque). 

## Interact with Views

#### 1. View binding

Enable the feature in the module's `build.gradle` file.

```gradle
android {
... ...
    buildFeatures {
        viewBinding true
    }
}
```

Binding only does a single pass over the view hierarchy instead of calling `findViewById()` multiple times.

#### 2. View `findViewById()`

It may introduce bugs in Android, e.g. passing an invalid ID in the current layout which leads to a `null` or a crash.

