# Activity Navigation

Two navigation strategies:
+ Back (temporal) navigation: Back button with **back stack**
+ Up (ancestral) navigation: provided by developer as an option in the app bar

#### Back Stack
A set of each `Activity` that the user has visited and that can be returned to by the user with the back button.

A new `Activity` starts -> push it onto the back stack and take user focus -> previous `Activity` stops and stays in the stack -> user presses the Back button -> current `Activity` is popped from stack and destroyed -> previous `Activity` resumes

