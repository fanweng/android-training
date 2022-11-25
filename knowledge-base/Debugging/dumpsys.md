# dumpsys

`dumpsys` gets a diagnostic output for all system services.

It would output:
+ Activity Manager
  + Settings: activity_manager_constants, etc.
  + Pending Intents
  + Broadcast States
  + Content Providers
  + App Services
  + Foreground Apps
  + Recent Tasks
  + Last ANR

## Options

```sh
# Show process memory by different category
$ dumpsys meminfo

# Show app's memory
$ dumpsys meminfo your_app
```
