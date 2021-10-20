# View

The `View` class represents the basic building block for all UI components.

A view's -
+ **location:** the coordinate pair (eft and top)
+ **dimensions:** width and height
+ unit: *density-independent pixel* (dp)

### Subclases of `View` class
+ `TextView`
+ `EditText`
+ `Button`
+ `ScrollView`
+ ... ...

### Using a `View`

Identify a `View`, use `+` symbol to indicate you are creating a new `id`:
```
android:id="@+id/button1"
```

Reference a `View`, omit the `+` symbol:
```
android:layout_toLeftOf="@id/show_count"
```