# Spiritual Companion for Android

[![Native Android APK](https://github.com/Edu6Tron/Book/actions/workflows/build-apk.yml/badge.svg)](https://github.com/Edu6Tron/Book/actions/workflows/build-apk.yml)

**Spiritual Companion** is a privacy-first, local-first Android application for devotional practice. It is a fully native **Kotlin** application built with **Jetpack Compose**, not a web wrapper. The application is designed to remain useful without a network connection while keeping online devotional exploration explicitly user initiated.

> The app's primary experience is on-device: devotional content, daily practice state, favourites, Japa counts, ritual alarms, selected local audio, and optional city preferences are persisted locally.

## Download

The recommended installation path is the [GitHub Releases page](https://github.com/Edu6Tron/Book/releases), which contains versioned APK files, release notes, and SHA-256 checksums. Temporary debug builds are also available as GitHub Actions artifacts for development testing.

| Current release track | Package ID | Android support | Distribution |
|---|---|---|---|
| `v1.0.1-rc.2` | `com.edu6tron.spiritualcompanion.nativepreview` | Android 8.0+ (API 26) | APK attached to GitHub Release |

## What is included

The application provides a devotional dashboard with a live clock, Panchang-style timing information, Brahma Muhurta alarm creation, daily practice tracking, and a Room-backed Japa counter. Its Aarti library is offline first, searchable, filterable, and supports favourites, lyrics, and user-owned local audio playback through Media3/ExoPlayer.

The festival and temple experience provides an offline festival calendar, a searchable no-GPS temple directory, city filtering, and source-oriented listings. City personalization can suggest locally relevant Aartis without reading device GPS. Online discovery is always a user action: it opens a provider search and never makes provider media an alarm tone.

Ritual alarms use Android `AlarmManager`, a foreground audio service, a lock-screen alarm activity, a bundled fallback devotional chime, Snooze and Stop actions, local-tone fallback handling, selectable 1/3/7-day pauses, and boot/time-change recovery. Exact timing still depends on the user allowing Android's **Alarms & reminders** permission.

## Native architecture

| Area | Implementation |
|---|---|
| UI | Kotlin and Jetpack Compose with Material 3 |
| Storage | Room local database |
| Dependency injection | Hilt |
| Audio | Media3/ExoPlayer for in-app playback and Android MediaPlayer service for alarms |
| Scheduling | AlarmManager with exact-alarm capability checks and recovery receiver |
| Tests | JUnit and Kotlin coroutine test support |
| Build | Gradle 8.9 with Java 17 |

## Build locally

Install Android SDK Platform 35 and Java 17, then run the following from the repository root:

```bash
./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug
```

The test APK is written to `app/build/outputs/apk/debug/app-debug.apk`. It is unsigned for store distribution and should be used only for direct device testing. See [RELEASES.md](RELEASES.md) for installation and verification guidance.

## Repository layout

```text
app/                 Native Android application module
app/src/main/        Kotlin, Compose UI, Room, audio, and alarm code
app/src/test/        Native unit tests
gradle/              Gradle wrapper files
.github/workflows/   APK build automation
RELEASES.md          Version and APK-verification guidance
CONTRIBUTING.md      Local development and pull-request expectations
```

## Safety and privacy boundaries

The temple directory intentionally uses offline, source-labelled data and does not request GPS or map access. A saved city is optional and stored only on the device. The app does not download, cache, or use online provider media as a ringtone; users may instead import audio they are permitted to use for continuous offline playback or an alarm tone.

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) before opening a pull request. Every Android change should pass the native unit-test task and preserve the offline-first behavior.
