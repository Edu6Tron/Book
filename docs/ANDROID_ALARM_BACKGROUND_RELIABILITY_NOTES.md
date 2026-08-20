# Android Alarm and Background Reliability Notes

## Scope

These notes guide the native alarm path for a user-configured devotional routine. They apply to alarms that must fire while the application process is not visible and the device may be idle or have its display turned off.

## Official Android guidance

Android documents that `AlarmManager` alarms can trigger an event even when the application is not running and the device is asleep. It identifies alarm-clock style timing as an appropriate exact-alarm use case. For a user-visible alarm, `setExactAndAllowWhileIdle()` delivers in Doze when the app has the required Alarms & reminders special access. If exact scheduling access is unavailable, an inexact `setAndAllowWhileIdle()` fallback can be delayed by system policy. [1]

Android further states that an exact alarm invoked to complete a user-requested action is exempt from Android 12+ foreground-service background-start restrictions. The receiver should start only a suitable foreground service; this app uses `mediaPlayback` and does not request location, camera, microphone, or other while-in-use permissions. [2]

The `SCHEDULE_EXACT_ALARM` access can change over time. When it is granted, Android emits `ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED`; the app should confirm access and schedule its enabled alarms again, in the same manner as boot recovery. [1]

## Implementation implications

1. Use wake-up alarms (`RTC_WAKEUP`) and `setExactAndAllowWhileIdle()` only for a specific user-enabled devotional alarm.
2. Keep boot, package-replace, time-change, time-zone-change, and exact-alarm-access recovery idempotent and asynchronous.
3. Start alarm audio in a declared `mediaPlayback` foreground service promptly from the alarm broadcast. Retain audio with a partial wake lock only while active; release it when the person stops or snoozes the alarm.
4. Keep an offline bundled chime as the fallback if a chosen local URI cannot be opened. Never depend on network access or online provider media at trigger time.
5. Treat device-specific battery managers as a final user-controlled reliability aid rather than silently changing battery-optimization policy. The app should explain that an alarm must be enabled, Alarms & reminders must be allowed, and device battery restrictions may need an explicit user choice.

## Sources

1. Android Developers, [Schedule alarms](https://developer.android.com/develop/background-work/services/alarms), accessed 20 August 2026.
2. Android Developers, [Restrictions on starting a foreground service from the background](https://developer.android.com/develop/background-work/services/fgs/restrictions-bg-start), accessed 20 August 2026.
