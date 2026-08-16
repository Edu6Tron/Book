# Spiritual Companion: Reference-Informed Original Product Blueprint

**Author:** Manus AI · **Date:** 16 August 2026 · **Scope:** Native Kotlin / Jetpack Compose Android application
**Status:** Implementation blueprint

## Purpose and product standard

Spiritual Companion should not become a generic content catalogue, a copied finance dashboard, or a clone of an alarm application. Its defining job is narrower and more valuable: help a person **wake intentionally before Brahma Muhurta, recognise today’s devotional timing, complete an evening prayer sequence at sunset, and read or listen without friction**. The product standard is an original, offline-first daily companion that feels calm when opened in the morning, decisive when a reminder arrives, and easy to return to at sunset.

The benchmark study supports that direction. Now in Android establishes the value of clear state ownership, adaptive Material design, and measurable performance practice. FocusModes validates an explicit user-controlled scheduling and foreground-work boundary. Media3 guidance and the podcast reference support a clean local-player state rather than ambiguous playback. Ivy Wallet and Expense Manager reinforce deliberate visual systems, compact daily dashboards, and tested user-facing contracts. None of their proprietary or open-source implementation code, assets, layouts, or brand identity will be copied. [1] [2] [3] [4] [5] [6]

> **Product promise:** “Open one calm screen, know the next devotional moment, begin or resume the right routine, and stay in control of reminders and sound.”

## Reference-to-original translation

| Reference pattern | Lawful original translation for Spiritual Companion | Explicitly excluded |
|---|---|---|
| Clear feature boundaries and state ownership | Keep dashboard, routines, alarm configuration, local catalogue, and playback as distinct user responsibilities; expose immutable screen state from the ViewModel. | Copying modules, package structures, source code, or test code. |
| User-controlled scheduled modes | Make routine availability and reminders visible, switchable, and reversible. Explain exact-alarm status before the user relies on it. | Background network polling, hidden auto-play, or alarms tied to streamed provider media. |
| Compact media controls and accurate state | Use one truthful local playback owner; show a compact “now playing” surface only while user-started audio is active; always provide Stop. | Content scraping, YouTube downloading, or presenting external media as offline. |
| High-information personal dashboard | Establish a devotional “Now / Next / Later” hierarchy: current time, immediate sacred window, one primary action, then supporting details. | Finance-specific cards, charts, copy, visual assets, or workflow structures. |
| Tested visual and performance contracts | Add deterministic UI-state logic tests first; add startup/list scroll measurement only when the project can maintain them in CI. | Unrealistic “five-star guaranteed” claims or unmeasured optimisation. |

## Original experience architecture

The following four surfaces form the original daily journey. They are deliberately finite so the app remains understandable on a small Android screen and responsive on mid-range devices.

| Surface | User need | Original interaction contract | Acceptance criterion |
|---|---|---|---|
| **Today** | “What matters right now?” | A `Now / Next / Later` summary leads with the current devotional context, then one primary action: **Open my routine**. Supporting timing stays available without competing for attention. | A user can reach the relevant routine in one clear action and can understand the time anchor without opening another tab. |
| **My routines** | “What do I do for this prayer?” | Two independent, editable personal suggestions: **Brahma Muhurta preparation** and **Evening Prarthana**. Each presents a transparent anchor, ordered steps, recitation/lyrics path, optional special-day note, and a reminder-control route. | The requested evening sequence is always ordered *Shubham Karoti → Vakratunda Mahakaya → Sukhkarta Dukhharta → Shirdi Majhe Pandharpur*. |
| **Ritual alarm control** | “Will it really remind me?” | Exact-alarm readiness, pause days, selected local tone, test action, edit/delete, and a clear fallback statement. Existing Android permission limitations are described plainly. | No setting implies a scheduled outcome that the device cannot provide. |
| **Local player and reading** | “Can I listen or recite without losing my place?” | Local audio is user-initiated. The player uses truthful loading/playing/paused/ended states and a safe Stop action. Recitation remains readable with or without audio. | Provider content never becomes an alarm tone and no network request occurs during routine screen scrolling. |

## First implementation tranche

This release should prioritise the features that make the app more useful every single day while retaining the current privacy, offline, and legal boundaries.

| Priority | Original improvement | Kotlin / Compose approach | Why it is high impact |
|---|---|---|---|
| P0 | **Daily devotional focus card** | Add a pure `DevotionalFocus` reducer that receives local time plus Panchang output and selects `Brahma Muhurta`, `Sunset prayer`, or `Next practice`. Render it as the first actionable dashboard card. | It removes decision fatigue and makes the app’s promise visible immediately. |
| P0 | **Resumable routine steps** | Persist only today’s completed step IDs in Room preferences, keyed by ISO local date and routine ID. Permit explicit reset; do not create a streak score or cloud record. | It turns lyrics and timing into a repeatable habit aid without surveillance or pressure. |
| P0 | **Reliable reminder hand-off** | Route routine users to the existing exact-alarm controls with the intended timing context; continue using the bundled fallback alarm. | It creates a predictable bridge between a personal plan and the device-level alarm system. |
| P1 | **One-owner local player contract** | Keep the existing Media3 owner authoritative, make Stop reachable, release resources predictably, and use a small immutable player state model. | It prevents misleading player controls and competing sound behaviour. |
| P1 | **Accessible interaction contract** | Give non-text controls labels, retain state descriptions for toggles/progress, maintain tap targets, and avoid encoding timing status by colour alone. | It improves everyday use, particularly during low-light morning and evening practice. |
| P1 | **Measured smoothness guardrails** | Preserve `LazyColumn`/stable item keys, avoid work in composable bodies, and add a baseline-profile/macrobenchmark feasibility task only after CI/emulator capacity is proven. | It targets actual responsiveness rather than superficial animation. |
| P2 | **Adaptive layout refinement** | Use window-size-aware arrangements for tablet/landscape while retaining a one-handed portrait priority. | It broadens device quality without distracting from the Android phone experience. |

## Quality gates

Every release implementing this blueprint must meet the following gate. These are quality checks, not a promise of a Play Store star rating.

| Gate | Evidence required |
|---|---|
| Kotlin correctness | Relevant deterministic unit tests pass, including timing-window and daily-progress transitions. |
| Android quality | `lintDebug` and a clean debug APK assembly pass. |
| Alarm safety | Manual device verification confirms the existing local fallback and exact-alarm readiness handling; no online audio is used as an alarm. |
| Accessibility | Key controls have semantic labels/state descriptions and lint has no new accessibility warning. |
| Privacy | No city names, media paths, labels, lyric search, or user inputs are written to diagnostics. |
| Release traceability | GitHub Actions passes and the APK/checksum are attached to a versioned release. |

## Deferred work and non-goals

The application must not promise user-uploaded video downloads, scrape providers, stream automatically at alarm time, or silently collect location. Legal downloads, background provider discovery, cloud accounts, social features, gamified streaks, and complex cross-device sync remain out of scope until separate user and legal requirements exist. Baseline Profiles and Macrobenchmark infrastructure are beneficial but should be adopted only when their test devices and CI cost are sustainable; a superficial profile is worse than no profile.

## References

[1]: https://github.com/android/nowinandroid "Now in Android — GitHub"
[2]: https://github.com/adnanrangrej/Focus-Modes-App "FocusModes — GitHub"
[3]: https://developer.android.com/media/implement/playback-app "Create a basic playback app — Android Developers"
[4]: https://github.com/fabirt/podcast-app "Podcast App — GitHub"
[5]: https://github.com/Ivy-Apps/ivy-wallet "Ivy Wallet — GitHub"
[6]: https://github.com/nkuppan/expensemanager "Expense Manager — GitHub"
