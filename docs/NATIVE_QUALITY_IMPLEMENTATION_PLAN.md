# Native Android Quality and Devotional Routine Plan

**Project:** Spiritual Companion Native Android

**Author:** Manus AI

**Date:** 16 August 2026

## Product standard and responsible target

The app should feel calm, fast, dependable, and respectful of the user’s devotional practice. A high Play Store rating cannot be guaranteed because it depends on future user feedback, device diversity, and store policy. The practical target is therefore to earn strong feedback through reliable alarms, clear timing, fast offline content, accessible controls, understandable recovery states, and no surprise network or privacy behaviour.

## Verified active baseline

The active `main` branch is currently at `694e44f` and the Android module declares `1.3.1-rc.15` / version code `16`. It already contains a local Panchang engine, curated offline Aartis and festivals, Room-backed preferences, exact-alarm infrastructure, an offline fallback tone, Media3 playback, guided practice, and local soundscapes.

The requested **Brahma Muhurta and sunset-relative devotional routine foundation is not currently present in the active source tree**: the planned `DevotionalRoutine.kt`, `DevotionalRoutineScreen.kt`, and corresponding route/state controls are absent. Restoring this foundation is the first implementation priority; polish must not displace the app’s core daily purpose.

| Strength already present | Quality gap to close | Product consequence |
|---|---|---|
| Offline Panchang, local catalogue, Room preferences, exact-alarm service, bundled fallback tone | The main requested morning/evening routine is not exposed as a focused user flow | The app needs to make the daily ritual journey obvious from Today, not require users to assemble it themselves. |
| Compose + lifecycle-aware collection + immutable dashboard models | No documented performance measurement journey or baseline profile path | Smoothness must be measured rather than assumed, beginning with real release builds and high-frequency screens. |
| Local location text, no GPS/maps directory, user-initiated discovery boundary | Accessibility/release checklist is not yet formalised | Recurring daily actions need labels, predictable focus order, contrast, reachable targets, and recovery instructions. |

## Benchmark synthesis

Public Play Store material for mature Hindu companion apps highlights a useful pattern: users value **broad but organised offline reference content**, daily Panchang visibility, localisation, festival context, and simple paths into worship material. The Spiritual Companion should adopt those principles without replicating commercial booking, consultation, data-harvesting, or intrusive engagement patterns.

Android’s Compose performance guidance emphasises avoiding unnecessary recomposition and expensive composition work, keeping lazy content efficient, and using Baseline Profiles for first-use responsiveness. Android describes Baseline Profiles as covering important paths such as startup, navigation, and scrolling, with an overview claim of approximately 30% code-execution improvement from first launch. [1] [2]

Android’s accessibility guidance and test guidance identify semantic labels, logical traversal, scalable content, touch-target size, contrast, and manual accessibility-service testing as release-quality requirements. Accessibility Scanner and Google Play’s pre-launch reports can identify touch-target, low-contrast, content-label, and traversal issues. [3] [4]

## Implementation plan

| Workstream | Scope | Acceptance criteria | Priority |
|---|---|---|---|
| **1. Restore daily routines** | Add offline routine definitions, persisted enablement, focused My routines screen, Today entry point, lyric-led handoffs, special-day suggestions, and a direct path to the existing exact-alarm controls. | Evening order is exactly **Shubham Karoti → Vakratunda Mahakaya → Sukhkarta Dukhharta → Shirdi Majhe Pandharpur**; Brahma Muhurta routine is timing-aware; no automatic provider playback; all core readings remain available offline. | P0 |
| **2. Smoothness guardrails** | Audit Compose lists and screen state; preserve immutable state; use stable item keys; avoid database, timing, image decoding, and media preparation on the rendering path; retain lifecycle-aware state collection. | No blocking work in composables; high-frequency library/list screens preserve smooth scrolling; failures present a visible recovery message rather than ending the app flow. | P0 |
| **3. Alarm resilience** | Keep existing fixed-time exact-alarm path; clearly distinguish routine guidance from a manually configured reminder; make readiness/permission recovery concise. | Offline fallback tone remains usable; alarm permission problems show a direct recovery action; no city, media path, label, or free-form input enters diagnostics. | P0 |
| **4. Accessible daily flow** | Audit labels, semantics, logical order, contrast in all included palettes, and reading-comfort behaviour; test critical routes with TalkBack and scanner. | Icon controls have content descriptions; primary controls meet Android target guidance; large text does not clip key timing, alarm, or lyric content. | P1 |
| **5. Measured release performance** | Add a Baseline Profile/Macrobenchmark path after the user-flow baseline is stable; profile cold launch, Today, My routines, Aarti list, lyrics, and alarm editor. | Release-mode test/report documents the journeys and avoids regressions in startup or list browsing. | P1 |
| **6. Real-device release check** | Verify the signed APK on the user’s Realme Narzo 70 Turbo, including screen-off alarm, offline fallback, playback, theme/large text, and rotation/restore. | No immediate close, no frozen screen, no delayed interaction under ordinary use; known device-specific issue has a reproducible report and mitigation before release. | P0 |

## Immediate implementation sequence

The next native change set will first restore the missing routine model and route because it directly serves the user’s primary devotional goal. It will then add narrow, deterministic regression coverage for the routine order, timing anchors, special-day guidance, preference persistence, and lyric handoffs. The quality pass will follow in the same change set: clearly labelled critical actions, resilient empty/error states, stable lazy-list keys where relevant, and a documented release verification checklist.

Baseline Profiles and Macrobenchmark are planned as a separate performance workstream rather than speculative configuration. They require valid release journeys and should be generated/measured against a representative Android device or compatible test environment; adding them without measurement would create false confidence.

## Release gates

Before a candidate is published, the app must pass unit tests, Android lint, a debug APK assembly, and the GitHub Actions workflow. A release candidate should also include a checksum, source-level changelog, and a targeted manual check of the following critical flows.

| Critical flow | Required verification |
|---|---|
| App launch and restore | Opens to a usable screen; no crash when Android recreates the activity. |
| Today and Panchang | Local sunrise, sunset, Brahma Muhurta, Tithi, Nakshatra, and Saka information render without network access. |
| My routines | Correct evening order; routine timing is clearly labelled as an estimate/personal suggestion; lyric handoffs work locally. |
| Alarm | Create, edit, disable, delete, pause/resume, permission recovery, screen-off trigger, stop/snooze, and offline fallback all work. |
| Aarti and media | Library remains responsive; local selections work; online discovery is user-initiated only; no provider item becomes an alarm ringtone. |
| Accessibility | TalkBack order/labels, visible focus, contrast, 48dp-class targets, and large-text layouts work on the core routes. |

## References

[1] [Jetpack Compose performance](https://developer.android.com/develop/ui/compose/performance), Android Developers.

[2] [Baseline Profiles overview](https://developer.android.com/topic/performance/baselineprofiles/overview), Android Developers.

[3] [Accessibility in Jetpack Compose](https://developer.android.com/develop/ui/compose/accessibility), Android Developers.

[4] [Test your app’s accessibility](https://developer.android.com/guide/topics/ui/accessibility/testing), Android Developers.

[5] [Vaidika Vignanam on Google Play](https://play.google.com/store/apps/details?id=com.gennie.vaidikavignanam&hl=en_IE), Google Play.

[6] [Dharmayana on Google Play](https://play.google.com/store/apps/details?id=in.dharmayana.android&hl=en_US), Google Play.
