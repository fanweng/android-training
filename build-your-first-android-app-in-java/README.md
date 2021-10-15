# Build Your First Android App in Java

https://developer.android.com/codelabs/build-your-first-android-app#0

## Layout Editor

Each screen in the app is associated with one or more **fragments**.

Each visible fragment in the app has a **layout** that defines the user interface of the fragment.

Each element in a layout is a **view**.

Layout is defined in the XML file under the `res/layout/` directory. In the layout editor, we can define and modify the the layout either by coding the XML or by using the interactive visual editor.

Every layout must have a **root view** that contains all the other views. The root view is always a **view group**.

In the [fragment_first.xml](./app/src/main/res/layout/fragment_first.xml) file, `ConstraintLayout` is the view group, which contains a `TextView` and a `Button`.
