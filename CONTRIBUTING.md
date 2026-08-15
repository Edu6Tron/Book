# Contributing to Spiritual Companion for Android

Thank you for improving the native Android application. Keep each contribution local-first, respectful of devotional content, and safe for offline use.

## Local setup

Use Java 17 and Android SDK Platform 35. From the repository root, execute:

```bash
./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug
```

## Engineering expectations

New behavior should be implemented in Kotlin and Compose, with no network request triggered automatically at application launch. Do not add GPS/map access to the temple directory. Do not place API credentials, keystores, or user-provided media in the repository. Native alarm changes must preserve a bundled offline fallback tone, Snooze/Stop control, and safe cancellation behavior.

## Pull requests

Describe the user-facing behavior, test coverage, and any Android permissions affected. Keep commits focused. Before requesting review, run the unit-test task and confirm that the debug APK assembles successfully.
