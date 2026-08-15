# Changelog

All notable user-facing changes are documented here.

## v1.0.1-rc.2 — unreleased

This repair build changes functional Android behavior rather than repository presentation. The ritual-alarm editor now lets the user select, persist, replace, preview, and clear a local audio tone per alarm. Tone-preview state is reported in the Aarti library, and unreadable local alarm audio falls back to the bundled offline devotional chime. The fired-alarm activity now identifies the active time and tone and offers 5- or 10-minute Snooze controls.

Online devotional searches now appear inside the app only after an explicit user action; provider media remains unavailable as an alarm tone. The build also exposes clear playback status and error feedback for local devotional audio.

## v1.0.0-rc.1 — 15 August 2026

This native Kotlin release candidate establishes the complete Android-first delivery path. It includes the devotional clock and Panchang-style dashboard, expanded offline Aarti, festival, and temple catalogues, optional saved-city personalization, searchable government-source temple data without GPS, user-triggered provider discovery, local audio playback, Room-backed daily practice and Japa persistence, and Media3 playback.

The ritual-alarm system now includes exact-alarm capability guidance, Android foreground playback, bundled offline fallback audio, persistent local-tone selection, full-screen Snooze/Stop actions, selectable temporary pauses, separate snooze scheduling, and recovery after reboot, application update, device time change, or time-zone change.

### Verification

The native debug unit tests and debug APK assembly both pass in GitHub Actions for the release commit.
