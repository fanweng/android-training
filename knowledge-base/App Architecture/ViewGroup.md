# ViewGroup

A `ViewGroup` acts as a container, grouping multiple [[View]] elements.

+ Parent: `ViewGroup`
+ Child: `View` or another `ViewGroup`

### Layout hierarchy

![[viewgroup-hierarchy.png]]

1. The root `ViewGroup`: contains the layout of the entire screen
2. The first set of child `View` elements and `ViewGroup` whose parent is the root

### Common `ViewGroup`
+ `ConstraintLayout`: child UI elements are placed using constraint connections to other elements and to the layout edges
+ `ScrollView`: contains one other child `View` element and enables scrolling the child `View` element
+ `RecyclerView`: contains a list of other `View` or `ViewGroup` and enables scrolling them by adding/removing `View` dynamically from the screen

### Root `ViewGroup` sometimes is a layout

Some `ViewGroup` are designated as [[Layout]] because they organize child `View` elements in a specific way and typically used as the **root** `VireGroup`.

+ `ConstraintLayout`
+ `LinearLayout`
+ ... ...