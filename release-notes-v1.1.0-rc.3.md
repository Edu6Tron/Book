# Spiritual Companion for Android v1.1.0-rc.3

This candidate is a substantive native Kotlin feature update built from commit `139f162`.

## Devotional dashboard and personalisation

The dashboard now calculates a transparent offline devotional context from the optional saved no-GPS city. It includes local sunrise, sunset, moonrise, moonset, Brahma Muhurta, Tithi, Nakshatra, Paksha, Hindu-month, and Saka-calendar context. These are clearly labelled as offline devotional estimates rather than astronomical authority data.

## Aarti, media, temples, and discovery

Local Aarti audio now has a full-screen read-along view with playback state, real progress, and proportional lyric highlighting. City choices are available without GPS; temple and festival selections now remain visibly selected while users browse offline data. Online devotional discovery is a deliberate in-app action only, and provider media cannot be assigned as an alarm tone.

## Practice, alarms, and accessibility

Japa includes grouped mala controls and durable local progress. Ritual alarms use faster horizontally scrollable time selectors and clearer screen-reader descriptions for time and weekday controls, while retaining local-tone selection, fallback audio, Snooze/Stop actions, temporary pause controls, and reboot/time-change recovery.

## Verification

GitHub Actions run `31881174161` passed native unit tests and debug APK assembly. This is a debug-signed APK for direct testing; Android may require permission to install it from the downloading browser or file manager.
