# Spiritual Companion Native Android Preview

This directory contains a separate, native Android implementation. It is intentionally isolated from the Expo application at the repository root, so either route can evolve without overwriting the other.

## Included foundation

The preview uses **Kotlin**, **Jetpack Compose**, **Room**, **Hilt**, **Gradle**, and **Media3/ExoPlayer**. It provides a native devotional dashboard, on-device daily-practice persistence, and a Media3 playback service foundation. The existing Expo application remains the more feature-complete release path.

## Build an APK from GitHub

Open **Actions** in the repository, choose **Native Android APK**, and select **Run workflow**. When the run completes, download the `spiritual-companion-native-debug-apk` artifact. The artifact is an unsigned debug APK intended for device testing; it is not a Play Store release.

## Build locally

```bash
cd native-android
./gradlew :app:assembleDebug
```

The APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

## Scope note

The native module deliberately uses a new package identity, `com.edu6tron.spiritualcompanion.nativepreview`, so it can be installed beside the Expo version during migration and testing. It does not yet replace the release-ready Expo app or its native Android ritual-alarm module.
