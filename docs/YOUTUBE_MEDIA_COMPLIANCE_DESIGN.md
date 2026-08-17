# Lawful YouTube and Local-Media Design

## Purpose

This document defines the media boundary for Spiritual Companion. The product provides two deliberately separate paths:

1. **Local devotional media.** A user chooses an audio or video file already available on their device and may use the native synchronized-lyrics player offline.
2. **Official YouTube discovery and playback.** A user may search YouTube explicitly, review results, and play a selected item only through an official YouTube-supported surface. This path never claims to provide offline files or synchronized text for third-party recordings.

The app does not bypass advertising, modify YouTube player behavior to suppress advertising, download YouTube videos or audio, scrape YouTube pages, extract media URLs, or copy provider captions/lyrics. These restrictions are product requirements, not merely UI choices.

## Official capability boundaries

The YouTube Data API provides `search.list` for video search and requires a Google project with YouTube Data API access. The service has quota limits, and search results are returned as metadata rather than downloadable media. [1] [2]

Creating a playlist is an account-changing write action. The `playlists.insert` endpoint requires OAuth 2.0 authorisation with a YouTube scope and has a quota cost. It must never be invoked merely because a user signs in or searches. [3]

YouTube API clients must comply with the API Services Terms and Developer Policies. The official terms require clients to use the documented means of access, to protect API and user data, and to provide a privacy policy that describes data handling. [4]

| Capability | Permitted app behavior | Explicitly excluded behavior |
|---|---|---|
| Discover | User taps search; client retrieves a small metadata result page through the official Data API. | Background discovery, scraped search pages, indefinite refresh, or location-derived search without user action. |
| Play | User opens the item in the official YouTube application or official YouTube player surface with branding and standard provider controls. | Ad blocking, a custom overlay over provider player controls, hidden provider attribution, or falsely labelling it offline. |
| Playlist | User first sees the exact playlist title/privacy choice and a clear **Create playlist** confirmation, then completes Google OAuth and the write request. | Silent OAuth, token logging, automatic playlist creation, or any playlist modification without a separate confirmation. |
| Offline | Existing device-local file continues in the native synchronized player. | Downloading, caching, converting, or extracting YouTube streams, audio, captions, or video files. |
| Lyrics | The native full-text/synchronized display remains limited to rights-verified text and locally selected media. | Associating supplied lyrics or personalised markers with a third-party YouTube recording unless separately authorised for that work and recording. |

## Privacy model

Search query text stays in-memory for the current search and must not enter application diagnostics. Search-result metadata is not retained beyond the user’s active session unless the user explicitly saves a provider link. OAuth credentials must use platform secure storage if native OAuth is later added, and only the minimum scope for playlist creation may be requested. The app must never log a Google account identifier, channel identifier, playlist identifier, provider URL parameters, title, or user-entered search text.

Provider discovery is not part of devotional alarm execution. Alarms and routine suggestions remain offline-first and never trigger a provider query or player launch by themselves.

## Delivery stages

The first implementation stage should add the two-path selection surface and a user-initiated official search/playback hand-off. Account-linked playlist creation is a second-stage action because it needs an approved OAuth client identity, consent screen, authorised redirect configuration, a clear in-app confirmation, token-revocation UX, and explicit user approval immediately before the write operation.

Until the OAuth prerequisites are supplied and configured, the app must show this truthfully: **“Playlist creation requires your own approved Google/YouTube authorisation. No account is connected.”**

## References

[1] [YouTube Data API Overview](https://developers.google.com/youtube/v3/getting-started)

[2] [YouTube Data API: `search.list`](https://developers.google.com/youtube/v3/docs/search/list)

[3] [YouTube Data API: `playlists.insert`](https://developers.google.com/youtube/v3/docs/playlists/insert)

[4] [YouTube API Services Terms of Service](https://developers.google.com/youtube/terms/api-services-terms-of-service)
