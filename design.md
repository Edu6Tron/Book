# Spiritual Companion Mobile Design

## Product intent

Spiritual Companion is a calm, private, local-first devotional companion for daily Hindu practice. The mobile experience prioritizes immediate access to Aartis, festival guidance, temple discovery, and small daily rituals without waiting for remote content. Every primary action is designed for portrait use with a reachable lower-half action area and native iOS-style spacing, feedback, and navigation.

## Screen list

| Screen | Primary content and functionality |
|---|---|
| Today | Current time, a short daily intention, a next-practice card, the next festival, and direct entry points to Aartis, Festivals, and Temple Finder. |
| Aartis | A cached, filterable `FlatList` of devotional items with category chips, language indicators, favourites, and a compact reading view. |
| Festival Calendar | A cached, chronological festival list with month filters, significance summaries, and source labels. |
| Temple Finder | A responsive, offline curated temple directory with city filters and a deliberate external-map handoff only after a user selects a temple. |
| Practice | A daily ritual checklist, a 108-bead Japa counter, and a small completion summary persisted on-device. |
| Settings | Theme preference, language preference, local-content status, and privacy explanation. |

## Primary interaction model

The bottom tab bar contains **Today**, **Aartis**, **Festivals**, and **Practice**, keeping the four most frequent actions one tap away. Temple Finder is opened from a prominent Today shortcut and from the Festivals screen header, avoiding an overcrowded tab bar. Lists use `FlatList` and small preloaded content arrays so the first meaningful screen is available immediately, while filters operate in memory rather than triggering a database or network request.

## Key user flows

| Goal | Flow |
|---|---|
| Read an Aarti quickly | Open Aartis → choose a category or search term → tap an item → read the devotional text → favourite it if desired. |
| Check a festival | Open Festivals → select a month chip → open a festival card → review date, significance, and ritual suggestion. |
| Find a temple | From Today, tap Temple Finder → select city or use nearby locations → tap a temple → choose “Open in Maps” only when ready to leave the app. |
| Complete daily practice | Open Practice → check ritual steps → use Japa counter → save completion automatically on-device. |

## Performance and data choices

The first app release uses a compact bundled devotional and festival dataset. It is copied to state on application startup and memoized for filtering, so screen transitions never wait on a network call, database seeding job, map SDK, or geocoder. The Temple Finder shows local directory results first; map directions are an explicit external action, not work performed when the screen opens. Future downloadable content must be fetched in the background, atomically saved, and exposed only after validation.

## Visual language

| Token | Value | Purpose |
|---|---|---|
| Saffron | `#C65A18` | Primary actions, selected states, and ritual emphasis. |
| Deep indigo | `#202343` | Strong headings and dark-mode surfaces. |
| Marigold | `#E5A21A` | Festival accents and celebratory indicators. |
| Sand | `#FBF6EE` | Warm light-mode background. |
| Rosewood | `#2A1713` | Dark-mode background. |
| Sage | `#4F7B61` | Completed practices and reassuring success states. |

The interface avoids visual clutter: cards use 16-point rounded corners, low-elevation borders, high-contrast typography, and one clear primary action per view. Dark mode uses rosewood surfaces and muted gold accents, suitable for AMOLED displays without making devotional content difficult to read.
