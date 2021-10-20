# Update a View

1. Instantiate an object from the [[View]]
Look for the `View` based on the resource `id`, then `mShowCount` is representing the `TextView` from now on.
```
mShowCount = (TextView) findViewById(R.id.show_count);
```

2. Update the object which also updates the screen
Assume the `countUp()` is associated with the `onClick` event. When `setText()` for the `mShowCount` object, the screen will be updated with the new text.
```
public void countUp(View view) {
        mCount++;
        if (mShowCount != null)
            mShowCount.setText(Integer.toString(mCount));
}
```