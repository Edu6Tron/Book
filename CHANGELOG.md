# Changelog

All notable user-facing changes are documented here.

## v1.3.2-rc.16 — 16 August 2026

The Today dashboard now leads to a focused, fully offline **My routines** experience for the app’s core daily rhythm. It presents the requested **Evening Prarthana** in the sequence *Shubham Karoti → Vakratunda Mahakaya → Sukhkarta Dukhharta → Shirdi Majhe Pandharpur*, anchored transparently to the selected city’s local sunset estimate. A companion **Brahma Muhurta start** routine is anchored to the existing offline pre-sunrise calculation.

Each routine can be saved as enabled or disabled on the device, retains the existing exact-alarm and bundled fallback-tone path for user-created reminders, and clearly separates personal routine settings from formal ritual authority. Short local recitation views are included for Shubham Karoti and Vakratunda Mahakaya; catalogue-backed steps open the existing offline lyric-led Aarti interface without background loading or provider auto-playback.

When the local Panchang estimate identifies Ekadashi or Purnima, the routine screen provides optional, clearly labelled suggestions that link only to already bundled local Aartis. Routine preferences, timing estimates, guidance, and lyrics remain local to the device. Regression tests cover the requested evening order, offline recitation text, Brahma Muhurta anchoring, and both special-day suggestion paths.

## v1.3.1-rc.15 — 16 August 2026

The Practice tab’s original offline soundscape library has doubled from three to **six** pieces. New device-bundled additions are **Temple bells & stillness**, **Monsoon reflection**, and **Lamp-flame stillness**, complementing Sacred dawn, Focused japa, and Evening lamp.

Each addition has a distinct title and purpose-forward description in the existing local playback card. The collection is designed for a broad range of quiet moments, including a dawn reset, counted japa, a rain-sheltered pause, evening reflection, or a simple period of silence. Playback remains deliberately separate from ritual alarms and provider-discovery media.

All six files are original bundled audio assets. They work offline, do not require a sign-in, GPS, provider account, tracking, or background download, and retain the player’s existing preparation, stop, completion, and recovery feedback. The soundscape catalogue test now verifies six unique local resource IDs and the new entries.

## v1.3.0-rc.14 — 16 August 2026

The Practice tab now has a more immersive **Guided practice journeys** collection. Three original, device-bundled devotional scenes introduce a Dawn sankalpa, an Evening deepa pause, and a Japa garden focus. The horizontally scrolling cards keep the main practice feed light, while a calm full detail sheet opens only when a user selects a journey.

Each journey provides a short suggested duration, a single intention, and four flexible reflection prompts. The content is labelled as optional guidance rather than ritual instruction, can be adapted to the user’s own tradition and circumstances, and remains entirely offline. The visual assets are original bundled artwork with no network download, sign-in, GPS, tracking, or background refresh path.

Deterministic tests verify the bundled journeys have distinct identities, usable artwork resources, and stable date-based featuring. The existing Practice soundscapes, scripture reflection, japa counter, checklist, and reading-comfort controls remain available in the same tab.

## v1.2.3-rc.13 — 16 August 2026

The Today dashboard now shows a compact **Ritual alarm readiness** card before the detailed alarm editor. It reports only aggregate, local status: whether no alarm is active, active alarms are paused, Android exact-alarm access needs attention, or one or more alarms are ready. It deliberately does not repeat an alarm label, selected media filename, or other private user choice.

When exact-alarm access is not available, the card opens Android’s own **Alarms & reminders** page. On returning to the app, it refreshes its local readiness state. The card also makes the existing reboot, app-update, device-time, and time-zone recovery behaviour visible, while the alarm editor remains the place to create, edit, pause, resume, preview, or delete a specific alarm.

The GitHub debug-build and protected signing workflows now use current upstream action major versions. The automated path remains test → Android lint → APK assembly → artifact upload, while the signing workflow continues to read keystore material only from protected GitHub secrets.

## v1.2.2-rc.12 — 16 August 2026

The Today dashboard now has a compact, screen-reader-labelled **Settings** action. It opens a dedicated, lightweight Settings screen rather than adding a sixth crowded bottom-navigation item. The new screen lets the user persist a Light, Dark, or device-controlled colour treatment and choose Compact, Standard, or Large reading comfort in one place.

Settings also explains the local notification and ritual-alarm boundary, then opens Android’s own app notification settings when the user chooses to manage permissions or channels. This app does not proxy those settings through a server or send reminder details off the device. The About and Privacy sections make the offline-first, no-GPS, user-initiated-discovery boundaries visible in the interface.

Theme preference parsing is isolated and deterministically tested, including safe fallback for unknown saved values. The release continues to use Room-backed local preferences and the existing warm devotional Material theme.

## v1.2.1-rc.11 — 16 August 2026

The offline catalogue now contains **30 Aartis**, **35 festival guides**, and **35 address-only temple records**. The new devotional material adds concise Tamil, Telugu, Kannada, Bengali, Sanskrit, and Hindi/Marathi-oriented entries, while the festival calendar now includes spring Navratri, selected Ekadashi guidance, Ganga Dussehra, Shravan Somvar, Durga Puja, Kartik Purnima, and Vaikuntha Ekadashi. All observances remain indicative and explicitly ask users to verify time-sensitive local dates with a published Panchang.

The temple directory now adds twelve clearly named Himachal Pradesh acquired institutions from the state Temples Trust listing. Every one retains an address-style description, authority label, and user-opened source link; no map, GPS, background lookup, or location inference was added.

The Practice tab also adds a compact seven-day **Scripture reflection** cycle. It presents a plain-language reflection, source reference, and small practical action; it is explicitly labelled as a reflection rather than an authoritative translation or ritual instruction. The cycle is deterministic, device-bundled, and has no network, sign-in, or background-refresh requirement.

## v1.2.0-rc.10 — 15 August 2026

The Practice tab now includes an original, device-bundled offline soundscape library: **Sacred dawn**, **Evening lamp**, and **Focused japa**. Each soundscape is playable without a network connection, has clear Play and Stop controls, is visually presented with an original devotional artwork, and remains distinct from both local Aarti files and ritual-alarm tones.

The multimedia resources are local Android assets rather than streamed provider media, so they do not require GPS, sign-in, background network work, or a provider account. Playback retains clear preparation, playing, completion, stop, and recovery feedback; the original soundscape library also has deterministic checks for distinct bundled resource identifiers.

## v1.1.6-rc.9 — 15 August 2026

Ritual alarms now show an explicit **Next** date and time in the alarm list, using the same offline repeat-day calculation as the exact AlarmManager scheduling path. A paused alarm clearly shows its resume time, and a disabled alarm clearly reports that it is turned off. This makes it easier to verify an alarm before putting the phone away.

The next-occurrence calculation is isolated as a deterministic timing model shared by the schedule path and interface, with tests for same-day, future-day, paused, and disabled states. It does not add network work, GPS access, user-data logging, or recurring background activity.

## v1.1.5-rc.8 — 15 August 2026

Online devotional discovery no longer embeds a full provider website inside the app. A search now opens only when the user taps the action, and it uses a browser-managed secure app tab with a visible toolbar. This prevents account menus, provider overlays, and page layouts from being clipped by the app’s navigation or covering the bottom tabs. Closing the provider tab returns the user directly to Discover.

The Discover screen now has clear privacy and offline guidance, a recoverable no-browser state, and deterministic URL-policy coverage. The Today dashboard also has a direct Discover action so online search is reachable as an intentional next step without hiding the existing offline Aarti and festival journeys.

## v1.1.4-rc.7 — 15 August 2026

The Practice tab now includes a lightweight offline daily reflection with a practical action for the day. It has no network dependency, no location tracking, and no background refresh work.

Reading comfort is now controlled directly from the Practice tab. Choose Compact, Standard, or Large text; the preference is persisted locally and adjusts the app’s devotional, festival, and practice reading typography without adding a new navigation tab or expensive scrolling behaviour.

Deterministic tests now cover daily-guidance rotation and safe restoration of the saved reading-comfort preference.

## v1.1.3-rc.6 — 15 August 2026

The ritual-alarm editor now uses a proper, touch-friendly analogue clock picker instead of long horizontal hour and minute strips. The selected time is prominent, opens the clock in one tap, and changes only after the user confirms it. The editor retains practical one-tap actions for the current time and the 04:30 Brahma Muhurta preset, together with an accessible description of the selected time.

The picker uses the project’s bundled Material implementation, keeps 24-hour alarm scheduling, and adds deterministic coverage for zero-padded time display, the current-time action, and the Brahma Muhurta preset.

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
