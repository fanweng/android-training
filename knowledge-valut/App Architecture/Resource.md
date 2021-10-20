# Resource Files

Common resources include:
+ `drawable`: images and icons
+ `layout`: [[Layout]] resource files
+ `menu`: menu items
+ `mipmap`: pre-calculated/optimized collections of app icons used by the Launcher
+ `values`: colors, dimensions, strings, styles, themes

To reference a resource in an XML layout:
```
@(package_name:)resource_type/resource_name
```

+ `package_name`: in which package the resource is located. It is not required if the resource is in the same project
+ `resource_type`: the `R` subclass for the resource type
+ `resource_name`: resource filename without the extension, or attribute value in the XML element