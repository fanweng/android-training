# Configuration Changes

When a config change occurs, Android system shuts down the [[Activity]], calling [[Lifecycle#Paused state onPause]], [[Lifecycle#Stopped state onStop]], and [[Lifecycle#Destroyed state onDestroy]]. Then system restarts the activity from the beginning, calling [[Lifecycle#Created state onCreate]], [[Lifecycle#Started state onStart]], and [[Lifecycle#Resumed Running state onResume]].

Common config change forms:
+ rotate the device, landscape layout is switched on
+ change in locale, e.g. system language
+ enters multi-window mode, resizeable app needs to recreate the layout definition


