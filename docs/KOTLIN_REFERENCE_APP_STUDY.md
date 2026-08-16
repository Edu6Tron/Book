# Kotlin Reference App Study for Spiritual Companion

**Purpose.** This study records lawful product and engineering patterns drawn from public reference material. It does not authorise copying source code, screens, proprietary assets, product names, or branding. Every resulting change in Spiritual Companion must be original, reviewed against its own user needs, and consistent with its offline-first privacy commitments.

## Evidence collected on 16 August 2026

| Reference | Publicly verified strengths | Original adaptation for Spiritual Companion |
|---|---|---|
| Now in Android | Google describes the project as a fully functional Android app written entirely with Kotlin and Jetpack Compose. Its visible repository structure includes dedicated modules for benchmarks, build logic, core, features, and sync; the repository also presents a substantial automated-development history. [1] | Keep the existing focused single-module scope while adopting its discipline: explicit UI state, separated data boundaries, predictable navigation, and release validation. A future isolated benchmark/profile module is only justified after measured startup or scroll bottlenecks appear on representative devices. |
| FocusModes | Its public README presents an offline-first Kotlin/Compose/Material 3 app using Room, Flow, Hilt, a foreground service, configurable modes, permission onboarding, and local-only data. [2] | Continue the current personal-routine approach: explicit user enablement, a transparent path to exact reminder controls, foreground alarm playback only when the user creates an alarm, and clear permission-recovery guidance. Do not copy its implementation or use an accessibility service, because Spiritual Companion has no app-blocking purpose. |

## Supplied-link availability notes

Two initial links were not usable on 16 August 2026: `ivy-llc/ivy` redirected to `unifyai/ivy`, a Python machine-learning framework, and `dipanshgoyal/ExpenseFlow` returned GitHub’s public “Page not found” response. [3] [4] The supplied source list subsequently identified the intended Android references—**Ivy Wallet** at `Ivy-Apps/ivy-wallet` and **Expense Manager** at `nkuppan/expensemanager`—which require direct review before their product or implementation claims may inform this study. [8] [9]

## Follow-up playback sources

Public search identified Android’s official Media3 playback guide and the AndroidX Media repository as appropriate sources for the app’s existing user-initiated local-media player. [5] [6] A public podcast-app repository was also identified for later workflow comparison, but it must be reviewed directly before any claim or adaptation is made. [7] The product boundary remains unchanged: streaming-provider content is not an alarm tone, it is never auto-played, and any future downloaded material must be legally authorised and explicitly user initiated.

Android’s Media3 guide recommends an `ExoPlayer` implementation, calls out audio attributes and audio-focus handling, describes releasing player/controller resources at the appropriate lifecycle boundary, and documents the `MediaSession` connection used to keep the system and player state aligned. [5] The existing app should therefore retain its local Media3 path, but its next original player refinement should establish one explicit playback owner, expose only truthful player state, release cleanly, and avoid competing alarm/player playback.

The reviewed podcast reference is a Kotlin/Compose sample that publicly lists Hilt, MVVM, foreground playback, media notifications, a media browser service, player actions, local cache playback, and light/dark themes. [7] The lawful adaptation is **not** to copy its code or build a network podcast service. Instead, Spiritual Companion can borrow the product pattern of a compact, explicit local player state with predictable Play/Pause/Stop controls and a clear distinction between user-started devotional audio and the protected ritual-alarm fallback path.

## Corrected UI and product references

Ivy Wallet is an archived, publicly available Android product repository with a documented multi-module shape, feature/core separation, UI tests, CI assets, a widget, accessibility-related fixes, and a recent history that visibly includes screenshot tests and Detekt maintenance. [8] Its archive status and GPL-3.0 licensing mean it is unsuitable as a code source for this app; the lawful takeaway is process-level: maintain an intentional UI system, separate high-change user features from shared data/UI concerns, and test the visual contract rather than relying only on manual inspection.

Expense Manager is an actively maintained open-source Kotlin/Compose Android project whose public repository shows `core`, `feature`, `build-logic`, and `macrobenchmark` modules alongside explicit Compose, Material, Room, Navigation, and adaptive-design tags. [9] Its lawful product lesson is that a personal daily dashboard should prioritise a compact “what matters now” surface, consistent cards, confident empty states, and one obvious next action. Spiritual Companion should apply this through original devotional timing and routine content—not financial layouts or copied styling.

## Provisional product principles

The app should always answer three questions in one glance: **What is the next devotional moment? What will happen then? What can I do now?** The dashboard should lead to a short routine, but never obscure the existing prayer library, practical alarms, or offline fallback choices.

Technical improvements should prioritise measurable reliability over fashionable complexity. The existing Kotlin, Compose, Room, Hilt, Media3, and exact-alarm foundations are appropriate. Performance work should first reduce duplicate state, unnecessary recomposition, unbounded work in scrolling screens, and heavy startup tasks; it should not introduce background networking or data collection.

## References

[1]: https://github.com/android/nowinandroid "android/nowinandroid — GitHub"
[2]: https://github.com/adnanrangrej/Focus-Modes-App "adnanrangrej/Focus-Modes-App — GitHub"
[3]: https://github.com/ivy-llc/ivy "ivy-llc/ivy redirect observed on GitHub"
[4]: https://github.com/dipanshgoyal/ExpenseFlow "dipanshgoyal/ExpenseFlow — GitHub Page not found"
[5]: https://developer.android.com/media/implement/playback-app "Create a basic playback app — Android Developers"
[6]: https://github.com/androidx/media "androidx/media — GitHub"
[7]: https://github.com/fabirt/podcast-app "fabirt/podcast-app — GitHub"
[8]: https://github.com/Ivy-Apps/ivy-wallet "Ivy Wallet — GitHub"
[9]: https://github.com/nkuppan/expensemanager "Expense Manager — GitHub"
