# Spiritual Companion Native Android Parity Roadmap

## Purpose

This module is a **separate native Android build** of Spiritual Companion. It must not replace the managed Expo application while it is being developed. The native app uses the application ID `com.edu6tron.spiritualcompanion.nativepreview`, so it can be installed beside the Expo APK during validation.

## Functional parity contract

| Area | Native Android deliverable | Storage / delivery |
|---|---|---|
| Home | Live devotional clock, Panchang summary, Brahma Muhurta, sunrise/sunset, saved alarms | Local calculation and Room-backed alarm records |
| Aartis | Offline search, category and location suggestions, favourites, readable verses | Bundled catalogue and Room favourites |
| Festivals | Offline Hindu-month filter and festival details | Bundled catalogue |
| Temples | Government-source directory search with source links; no GPS or maps | Bundled verified records |
| Practice | Japa counter, reset action, three daily-practice check-offs | Room database |
| Media | Local Media3 audio playback with verse progression; provider media only on explicit user action | Local files and intentional external handoff |
| Alarms | Editable repeating schedules, Android exact alarm scheduling, foreground looping sound, full-screen Snooze/Stop controls | AlarmManager, foreground service, Room |

## Delivery sequence

1. Replace the proof screen with a polished four-tab application and room-backed offline content.
2. Complete the Aarti, festival, temple, practice, and Japa flows with accessible detail screens.
3. Add local Media3 playback, file selection, and clear constraints for provider content.
4. Add Android-native alarm creation, receiver/service delivery, and full-screen controls.
5. Validate each milestone with tests and a GitHub Actions APK artifact before presenting it as feature-complete.

## Non-negotiable constraints

- No GPS, maps, or automatic online search in the temple directory.
- Online devotional discovery must remain user initiated and must never turn provider video into an alarm ringtone.
- The fallback alarm sound must remain local and must work without a network connection.
- The GitHub Actions workflow produces debug APK artifacts only; a signed store release requires separate signing credentials.
