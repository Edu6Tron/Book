# Play Store Benchmark Notes — Native Quality Plan

Research date: 16 August 2026. These public listing observations are product signals, not an endorsement of any competitor’s claims or a promise of app ratings.

## Comparable devotional experience signals

| App | Public signal observed | Pattern relevant to Spiritual Companion | Source |
|---|---|---|---|
| Vignanam | Google Play showed a 4.8 rating from 28.2K reviews. Its visible store material emphasises local-language scripture access, Panchang context, and readable presentation. The reviewed feedback specifically praises available Indian languages and meanings, while another reviewer asks for reusable playlists. | Keep content immediately readable and offline-first; use language/meaning cues where supported; make personal routine organisation durable rather than requiring repeated setup. | https://play.google.com/store/apps/details?id=com.gennie.vaidikavignanam&hl=en_IE |
| Dharmayana | Google Play presents an all-in-one daily Hindu companion with a public 4.5 rating claim and feature messaging around Panchang, festivals, prayers, regional languages, and distinct morning/evening routines. | The native app should foreground a single daily next action, timing context, and routine continuity rather than adding an unbounded feature catalogue. | https://play.google.com/store/apps/details?id=in.dharmayana.android&hl=en_US |

## Preliminary design and quality implications

The highest-value patterns are **calm, immediately useful daily context**, **fast access to the next devotion**, **reliable personal progress**, and **content that remains useful without an account or connection**. The existing app already has a strong offline foundation, timing-aware Panchang, local recitation, alarms, and persisted themes. The next benchmark should concentrate on reducing friction in the first thirty seconds of use, strengthening empty/error and recovery states, and proving smoothness through measured Android checks rather than visual complexity.

The app will not replicate commercial consultation, booking, or intrusive engagement patterns. It will preserve user control, local preferences, user-initiated online discovery only, and the existing instruction that personal routines are suggestions rather than ritual authority.

## Android technical quality findings

| Official Android guidance | Relevant implementation consequence | Source |
|---|---|---|
| Compose performance guidance recommends stable state, avoiding expensive calculations in composition, helping lazy layouts, limiting unnecessary recomposition, deferring state reads where appropriate, and using Baseline Profiles for smoother first-use behaviour. | Audit high-frequency dashboard, Aarti, festival, and routine composition; preserve immutable state, do timing work off the main/rendering path, and add performance-oriented regression coverage before adding visual complexity. | https://developer.android.com/develop/ui/compose/performance |
| Android’s Baseline Profiles overview describes precompilation of critical code paths, including app startup, navigation, and scrolling, and reports an approximately 30% code-execution improvement from first launch in its overview. It also identifies Macrobenchmark as the mechanism for measuring profile impact. | Add a dedicated Baseline Profile and benchmark journey only after the release build path is confirmed, starting with cold launch, Today, My routines, and Aarti browsing. Keep this separate from functional tests so performance changes remain measurable. | https://developer.android.com/topic/performance/baselineprofiles/overview |

## Planned quality guardrails

The quality release should focus on measurable, user-visible outcomes: resilient launch and restore, no main-thread database or timing work, stable Compose inputs, lifecycle-safe player and alarm state, 48dp-class touch targets, readable large-text behaviour, release-mode shrinking/optimisation checks, and real-device verification on the user’s Realme Narzo 70 Turbo. A Play Store rating cannot be guaranteed; the responsible target is to consistently earn positive feedback by preventing crashes, slow first use, unclear timing, and dead-end recovery states.

| Official Android accessibility guidance | Relevant implementation consequence | Source |
|---|---|---|
| Compose accessibility guidance covers semantics, traversal order, API defaults, and user-scalable content. | Give icon-only controls explicit labels, maintain logical TalkBack order in the daily flow, and verify that reading-comfort choices do not cause clipped content. | https://developer.android.com/develop/ui/compose/accessibility |
| Android’s accessibility testing guidance recommends manual accessibility-service testing, analysis tools, automated tests, and user testing. Accessibility Scanner and Play pre-launch reports surface touch-target, contrast, label, and traversal problems. | Add a release checklist for TalkBack, touch targets, contrast, and error/recovery states; run scanner/pre-launch checks when a signed Play-ready build is available. | https://developer.android.com/guide/topics/ui/accessibility/testing |
