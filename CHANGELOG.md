# Changelog

All notable user-facing changes are documented here.

## v1.1.2-rc.5 — 15 August 2026

This performance-focused candidate removes avoidable scroll-time work from the Aarti library, festival calendar, temple directory, dashboard, and daily-practice lists. Playback progress is now isolated from the static offline content state and updates at a calmer cadence, while filters, location suggestions, quick choices, and reusable list content are cached rather than recalculated during a swipe.

The offline library now contains 24 curated Aartis, 26 festival guides, and 23 address-only temple records. Temple records remain source-labelled and no-GPS; the Panchang calculator also adds bundled offline support for Madurai, Shimla, Kochi, and Patna, and prioritises an exact city selection over broad state-name matching.

## v1.1.1-rc.4 — 15 August 2026

This device-hardening candidate makes native startup, local playback, saved state, and alarm scheduling failures visible through privacy-preserving, non-blocking in-app notices. Sanitised diagnostic markers deliberately omit city names, media file names, devotional content, user input, and device identifiers.

The Android manifest now blocks clear-text network traffic and Android lint is a mandatory GitHub Actions gate before a debug APK artifact is assembled. The project also includes a protected, manually invoked release-signing workflow that expects a Base64 keystore and passwords only through GitHub Actions secrets; no signing material is committed to source control.

## v1.1.0-rc.3 — unreleased

This feature-completion candidate adds a fully offline, city-aware devotional dashboard. The dashboard resolves the saved no-GPS city, provides transparent calculated sunrise, sunset, moonrise, moonset, Brahma Muhurta, Tithi, Nakshatra, Paksha, Hindu-month, and Saka-calendar context, and clearly labels the calculation as an offline devotional estimate.

The Aarti experience now includes a full-screen local read-along mode with actual playback position, approximate proportional lyric highlighting for user-selected recordings, clear media-state feedback, and safe in-app user-initiated devotional discovery. Festival and temple filter states are visibly selected, while temple data remains offline and does not request GPS or maps.

The ritual-alarm editor now exposes fast horizontally scrollable hour and minute selectors with screen-reader descriptions. The daily Practice experience adds grouped mala controls and clearer local Japa progress. The candidate includes regression tests for location-aware Panchang calculations and lyric timing, together with native unit tests, Android lint, and debug APK assembly validation.

## v1.0.1-rc.2 — unreleased

This repair build changes functional Android behavior rather than repository presentation. The ritual-alarm editor now lets the user select, persist, replace, preview, and clear a local audio tone per alarm. Tone-preview state is reported in the Aarti library, and unreadable local alarm audio falls back to the bundled offline devotional chime. The fired-alarm activity now identifies the active time and tone and offers 5- or 10-minute Snooze controls.

Online devotional searches now appear inside the app only after an explicit user action; provider media remains unavailable as an alarm tone. The build also exposes clear playback status and error feedback for local devotional audio.

## v1.0.0-rc.1 — 15 August 2026

This native Kotlin release candidate establishes the complete Android-first delivery path. It includes the devotional clock and Panchang-style dashboard, expanded offline Aarti, festival, and temple catalogues, optional saved-city personalization, searchable government-source temple data without GPS, user-triggered provider discovery, local audio playback, Room-backed daily practice and Japa persistence, and Media3 playback.

The ritual-alarm system now includes exact-alarm capability guidance, Android foreground playback, bundled offline fallback audio, persistent local-tone selection, full-screen Snooze/Stop actions, selectable temporary pauses, separate snooze scheduling, and recovery after reboot, application update, device time change, or time-zone change.

### Verification

The native debug unit tests and debug APK assembly both pass in GitHub Actions for the release commit.
