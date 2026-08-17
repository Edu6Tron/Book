# Changelog

All notable user-facing changes are documented here.

## v1.4.3-rc.27 — 17 August 2026

The devotional clock can now refresh **sunrise, sunset, moonrise, and moonset** on demand from the U.S. Naval Observatory’s documented Sun and Moon timing service for the user’s already selected supported place. A visible **Refresh online timings** action downloads a bounded 31-day window, stays responsive while progress is shown, and stores the resulting timing values locally for future offline use.

The dashboard, personal routine guidance, and selected-day Maharashtra calendar present the saved official-source rise/set values whenever available. Brahma Muhurta is recalculated from the refreshed next-day sunrise. If a day is missing, the source cannot be reached, the response has an unexpected date, or the selected place is not supported by the accuracy source, the app keeps its existing local Panchang estimate instead of blocking the screen or fabricating a timing.

The refresh is strictly user initiated: it performs no automatic, scheduled, or background network work. The on-device cache has no city name, free-text address, URL, GPS permission, user account, or diagnostic logging. It contains only the opaque supported-place key, date, timing values, source label, and refresh timestamp needed to decide whether a stored value is valid. The source selection and safeguards are recorded in `docs/ONLINE_TIMING_SOURCE_RESEARCH.md`.

Focused tests cover official-response parsing without live network access, wrong-date rejection, place-bound cache lookup, absence of city labels from the encoded cache, coverage dates, and safe offline overlays. Native unit tests, Android lint, a debug APK assembly, and a live response-shape check against the documented official endpoint passed before this candidate was prepared.

## v1.4.2-rc.26 — 17 August 2026

The Maharashtra calendar now presents an original, selected-day **rich-event feed** instead of mixing government observances and calculated devotional cues without visible provenance. Each event has a title, category, source label, optional source URL, estimate flag, and clear source-tier disclosure. The four tiers are **Government-published date**, **Curated devotional guide**, **Local Panchang estimate**, and **Personal plan**, enabling future expansion without representing a personal suggestion as an official calendar declaration.

Bundled 2026 Maharashtra public holidays continue to appear as non-estimated government-published entries. Ekadashi, Purnima, Amavasya, Chaturthi, and Shravana cues are now independent local Panchang estimates with an explicit prompt to confirm ritual-critical timing locally. The selected-day view uses distinct visual treatments for each tier and repeats the source and disclosure at the event itself rather than hiding this context in a general disclaimer.

The release adds focused unit coverage for government event provenance, estimate flags, Ekadashi/Purnima/Chaturthi cue generation, and source-tier labels. Native unit tests and Android lint passed before this candidate was prepared; it introduces no GPS, network lookup, background refresh, or logging of private timing or location inputs.

## v1.4.1-rc.25 — 17 August 2026

The offline Aarti reader now distinguishes short guided reading excerpts from a **verified whole-text** inclusion. **Om Jai Shiv Omkara** is bundled as a complete, source-attributed text adapted only for accessible line breaks from Sanskrit Wikisource’s reviewed permanent revision. Its reader card identifies the CC BY-SA 4.0 licence, contributor attribution, stable source URL, and licence URL; both links open only after a user taps them.

This release introduces an explicit source policy for devotional texts and recordings. It allows complete offline text only when a public-domain rationale, compatible open licence, or recorded permission has been verified. Current catalogue entries without a verified work-level source remain clearly labelled as short guided reading excerpts rather than being represented as full authorised transcriptions. Commercial lyric sites, scans without a reusable licence, third-party recordings, and provider media remain excluded from offline ingestion.

The Aarti library continues to support user-chosen local audio, but it does not bundle third-party recordings or synchronise text to online provider media. This preserves the app’s offline-first and privacy-first boundaries while making provenance visible at the point of reading.

## v1.4.0-rc.24 — 17 August 2026

The Festivals tab now opens with an original, richer **Maharashtra calendar** built for the app rather than a copied almanac. It offers a Sunday-first month grid, accessible day selection, selected-day sunrise and sunset, moonrise and moonset, Brahma Muhurta, Tithi, Paksha, Nakshatra, lunar-month estimate, and Saka date. All timing content comes from the app’s existing offline Panchang calculation for the selected saved place and is explicitly identified as a personal estimate, not ritual authority.

The first bundled government-observance layer includes 24 source-labelled 2026 Maharashtra public-holiday markers. Each selected day identifies any included official observance and its MMRDA public-holiday source. The calendar retains the existing 35 local devotional guides and source-labelled offline temple directory in dedicated sections, with no GPS, automatic network work, copied third-party calendar content, or tracking.

The release also adds an original calendar-design record and focused tests for official-date data, Sunday-first month grids, and personal-practice markers. Native unit tests and Android lint passed before publication.

## v1.3.9-rc.23 — 17 August 2026

**My routines** can now hand a timing-aware personal suggestion directly into the established exact-alarm editor. A Brahma Muhurta action pre-fills today’s offline Brahma Muhurta start, while an Evening Prarthana action pre-fills today’s offline sunset estimate. The hand-off gives each alarm a clear purpose label and then leaves the time, repeat days, sound, and final save entirely under the user’s control.

The editor states that the supplied value is a current offline estimate rather than ritual authority. A saved exact alarm remains a fixed clock-time reminder, so the flow explicitly asks the user to review it as seasonal timings change. The feature stays fully offline-first: it does not request GPS, add network work, create background refresh, or log timing context, city names, labels, or other private inputs.

Focused unit coverage now verifies Brahma Muhurta and sunset prefill values, correct purpose labels, and safe suppression when an estimate is unavailable. Android unit tests and lint have passed for this candidate.

## v1.3.8-rc.22 — 17 August 2026

This quality candidate makes the app’s in-app media behaviour easier to trust. Preview, local Aarti, and offline-soundscape playback are now explicitly governed by one local player contract, while exact ritual alarms remain separate in their foreground alarm service. The player is released when the app’s activity-scoped dashboard owner is destroyed, guarded against later use, and stops its progress work before releasing resources. No playback URI, city, alarm label, or user input is written to diagnostics.

Whenever in-app audio is playing, every main tab now retains a compact **Playing on this device** control directly above navigation, with a clear **Stop playback** action. This means local audio started in one tab cannot become unreachable after changing tabs or closing an Aarti lyrics view. The Aarti library and Practice screen also make their local stop state more explicit.

The candidate adds screen-reader labels and state descriptions for daily-practice checks, routine steps, routine switches, alarm switches, and Japa progress. All eleven devotional palettes now have regression coverage that verifies readable foreground contrast in their light and dark control assignments. The update remains offline-first and does not add network work, GPS, tracking, or background refresh.

## v1.3.7-rc.21 — 16 August 2026

The Today dashboard now offers an original, timing-aware **NOW / NEXT / PERSONAL PLAN** focus cue. It changes locally according to the selected city’s offline Panchang estimate, the current device time, and the user’s enabled Brahma Muhurta or Evening Prarthana routine. It can therefore point into an active Brahma Muhurta window, gently prepare the evening sequence ahead of the local sunset estimate, or make an unconfigured personal routine visible without fabricating an alarm state. The focus cue is a personal aid, not ritual authority, and neither the city, routine choices, nor current focus leave the device.

This candidate also adds Android Baseline Profile packaging through the supported ProfileInstaller dependency, together with a small startup-focused rule set. The optimization is only applied by Android for production-style releases, while the development build remains fast to install. The source-attributed study and original product blueprint added under `docs/` distinguish lawful pattern learning from copied code, assets, or branding; they guide the next quality work on player ownership, reminder hand-off, and accessible interactions.

Unit coverage now confirms the active Brahma Muhurta, next Evening Prarthana, and unconfigured-plan focus states. The native debug APK assembly succeeds with the profile dependency and no network, GPS, background refresh, or private-data logging has been added.

## v1.3.6-rc.20 — 16 August 2026

This release completes a reconciliation pass on the timing-aware routine work and restores a clean native Android build path. The dashboard now has one clear **My routines** entry point, preserving the richer daily-rhythm card, local progress flow, lyric handoffs, special-day suggestions, and exact-reminder access introduced across the routine releases.

The included `docs/PLAY_STORE_BENCHMARK_NOTES.md` and `docs/NATIVE_QUALITY_IMPLEMENTATION_PLAN.md` record the public product and Android guidance review that now informs subsequent work: a calm daily path, trustworthy offline-first timing, readable controls, accessible labels, measured Compose state, and repeatable quality gates. These documents are planning and source records; no user data, provider content, or device location is collected for the review.

## v1.3.5-rc.19 — 16 August 2026

The native app now offers **eleven user-selectable devotional colour palettes**: the original Sacred saffron plus Temple lotus, Krishna twilight, Ganga dawn, Tulsi grove, Himalayan mist, Vithoba indigo, Deepa ember, Monsoon prayer, Rose sandal, and Moonlit silver. Each palette is intentionally named and previewed in Settings, so a personal appearance can be chosen without guessing from an abstract colour code.

Appearance mode and devotional colour are deliberately separate. Light, Dark, and device-controlled appearance remain available, while the chosen devotional palette is applied consistently to the app’s Material surfaces, selected navigation state, timing panels, local player controls, routine cards, and Settings components. The selected palette is stored only on the device and restored safely after an app restart; unknown saved values fall back to Sacred saffron rather than blocking launch.

The Settings selector is horizontally efficient, touch-friendly, and retains user-selected reading comfort. This is a Compose-only preference feature: it introduces no background work, network access, GPS permission, remote profile, or private-data logging. Regression coverage verifies all stable palette identifiers, the full eleven-option catalogue, and defensive restoration behaviour.

## v1.3.4-rc.18 — 16 August 2026

This release gives the native app a calmer and more intentional devotional visual system. The shared warm palette now distinguishes dawn, evening, practice, and supporting surfaces with clearer contrast in both light and dark modes. A dedicated serif-led display hierarchy makes the devotional clock, routine names, and lyric-led content easier to scan without reducing the user-selected reading-comfort scaling.

The **Today** experience now opens with a more expressive devotional clock, a concise Saka-date context line, clearer sacred-window typography, and visibly grouped cards for routines, japa, exploration, and daily practice. Bottom navigation has a more deliberate selected state, while the settings and routine back actions use clearer tonal controls.

**My routines** now has a stronger daily-rhythm header, a warm anchor-time panel, more distinct morning and evening routine surfaces, and numbered completion markers that visibly acknowledge each completed step. The Aarti library receives matching hierarchy across its local player, optional city guidance, search, and lyric-entry cards. These changes are Compose-only: no network activity, background work, private-data logging, or scroll-time data loading was added.

## v1.3.3-rc.17 — 16 August 2026

**My routines** now feels more like a daily companion than a static reading list. It shows the next upcoming personal moment from the local Brahma Muhurta and sunset estimates, including a calm time-until label that refreshes on-device. The timing card also has a direct **Open reminder controls** action, which returns to the established exact-alarm controls instead of leaving the reminder path implicit.

Each daily routine now has a simple local progress indicator and a completion mark for every step. Marks are retained across navigation and app restarts for the current calendar day, clear naturally when a new day begins, and can be cleared manually at any time. The app never sends this progress, timing, prayer choice, or city information off-device.

The completion model validates stored dates defensively and ignores malformed data rather than blocking the app. The refined interface continues to keep user-owned exact alarms separate from the sunset estimate, bundled local lyrics separate from provider playback, and personal suggestions separate from ritual authority.

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
