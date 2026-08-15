# Spiritual Companion for Android v1.0.1-rc.2

This is a **functional repair release**. It is not a documentation-only or repository-presentation update.

## Fixed and improved

- **Per-alarm local tone selection:** create or edit an alarm, choose a local audio file, preview it, replace it, or return to the bundled devotional fallback tone. The selected URI is persisted with that alarm.
- **Reliable alarm fallback:** if Android cannot read the selected local tone when an alarm fires, the foreground alarm service uses the bundled offline devotional chime instead of failing silently.
- **Better fired-alarm controls:** the lock-screen alarm surface identifies the current ritual and offers explicit 5-minute and 10-minute Snooze actions plus Stop.
- **In-app devotional discovery:** online devotional searches open inside the app only after the user requests them. Provider results remain unavailable as alarm tones, preserving an offline fallback for alarms.
- **Playback feedback:** local devotional playback now reports loading, playing, stopped, and failed states instead of leaving a silent or ambiguous player control.
- **Android 8 compatibility:** the lock-screen alarm flow now supports the app’s Android 8.0 minimum version, and the bundled player uses a stable Android resource URI.

## Verification

The source commit passed native unit tests, debug APK assembly, and Android lint locally. GitHub Actions independently passed its unit-test and debug-APK build workflow for commit `7b122ac`.

## Installation

Download `spiritual-companion-native-v1.0.1-rc.2-debug.apk`, then allow your browser or file manager to install unknown apps when Android requests permission. This is a debug-signed pre-release for direct device testing, not a Play Store-signed production build.
