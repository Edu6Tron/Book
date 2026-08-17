# Synchronized Lyrics Player Design

## Purpose

This design adds a **Spotify-like reading experience** to the native Spiritual Companion without treating an arbitrary recording or an online page as reusable devotional content. The player gives the current lyric line visual focus, supports seeking and local audio playback, and works entirely offline after a user selects media from their device.

The experience is an original devotional reader and local-media player. It is not a clone of any streaming product, and it does not fetch, download, embed, or extract third-party recordings.

## Content and rights boundary

| Content category | Lyric display | Timed highlighting | Recording behavior |
|---|---|---|---|
| Verified complete text | The complete approved text may be displayed with its attribution and licence. | Supported through personal timing markers or readable guided progression. | Only user-provided local audio, unless a recording receives a separate rights review. |
| Guided excerpt | Only the existing excerpt is displayed and is labelled as an excerpt. | Supported only for the displayed lines; it never implies that the full work is included. | Only user-provided local audio. |
| Unverified/commercial material | Not bundled. | Not supported. | Not played, downloaded, extracted, or cached by the app. |

The initial complete-text player target is **Om Jai Shiv Omkara**, the catalogue’s existing CC BY-SA 4.0 entry. Its attribution card stays inside the player. Other catalogue entries retain their current excerpt status.

## Timing model

`LyricTiming` provides a deterministic line resolver. A lyric timing profile is a list of non-negative millisecond offsets, one per displayed line. For a local recording selected by the user, the reader offers **Personal sync**: while the recording plays, the user moves to a line and records the current playback position. Those markers are saved only on-device and are never uploaded or logged.

When personal markers are incomplete or unavailable, the reader uses a clearly labelled **guided reading pace** based on the selected audio duration. Guided pace moves among the displayed lines for reading assistance; it is never represented as a verified transcription of the recording’s timing. No timing profile is bundled for a third-party recording.

## Player interaction

The player opens as a full-screen dialog and retains the selected local audio, active line, source-status card, text-attribution card, and closing control in one focused view.

| Control | Behavior |
|---|---|
| Play / pause | Toggles the selected local audio without opening another application. |
| Previous / next line | Moves lyric focus and seeks when a personal marker is present. |
| Fifteen-second controls | Seeks backward or forward within the playable duration. |
| Progress bar | Shows current position and permits direct seeking. |
| Reading mode | Shows the lyrics without audio while retaining manual next/previous line control. |
| Personal sync | Records or clears a marker for the focused line. |
| Attribution | Preserves text-source, licence, and adaptation disclosure for whole texts. |

All essential actions have text labels or explicit accessibility descriptions. Current-line changes are exposed as a polite live-region update, so TalkBack users receive concise context without receiving every progress tick.

## Data, privacy, and fallback behavior

Personal marker data is keyed by the stable Aarti identifier and stored in the app’s existing local preference storage. It contains only line offsets, no audio file path, city, address, recording metadata, lyric search term, or user-entered text. Clearing selected media also clears personal timing markers because those markers no longer identify a valid recording.

The player handles unavailable local media, unreadable duration, absent markers, buffering, errors, and completed playback without crashing. It returns to reading mode and explains the local fallback. Media3 player state and seeking use the official `Player` control and listener model. [1] [2]

## Acceptance criteria

The implementation is ready only when it satisfies all of the following conditions.

1. The player can open every approved displayable Aarti and never invents a full text for an excerpt.
2. A local audio file can play, pause, seek, and stop from the in-app player.
3. Current-line highlighting follows personal markers when present and a labelled guided pace otherwise.
4. Personal marker persistence survives normal app recreation but is removed with cleared media.
5. The UI remains usable without media, offline, during local-file failure, and with accessibility services.
6. Tests cover line resolution, invalid marker rejection, player-state fallback, and content-status labels.

## References

[1] Android Developers, *Introduction to Jetpack Media3*: https://developer.android.com/media/media3

[2] Android Developers, *Player events*: https://developer.android.com/media/media3/exoplayer/listening-to-player-events
