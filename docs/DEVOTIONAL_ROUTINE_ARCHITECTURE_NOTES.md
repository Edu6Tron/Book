# Devotional Routine Architecture Notes

## Online provider boundary

The routine system is designed to remain useful with no network connection. Every scheduled devotional action must have a local alarm, local routine metadata, and an offline fallback. Online provider content is an optional, explicitly user-started discovery or viewing experience rather than a scheduled alarm source.

Google’s IFrame Player documentation describes an embedded player as a controllable player surface and specifies a minimum viewport of 200 by 200 pixels, with larger 16:9 dimensions recommended when controls are shown. Its player-parameter guidance notes that autoplay can begin without user interaction and that this causes playback data collection and sharing when the player loads. The app will therefore not start an embedded provider player automatically at a scheduled time. [1] [2]

The YouTube Developer Policies require a user action relating to a YouTube resource to be clearly identifiable as a YouTube action, distinct from application functionality, and clearly initiated by the user. The policies also require a privacy-policy disclosure when YouTube API Services are used. Accordingly, online provider search and playback remain clearly labelled, user-initiated, and separate from routine alarms and local soundscapes. [3]

The native routine design does **not** download or cache third-party provider audiovisual content. A future download feature would require a separately licensed content source and its own rights, storage, deletion, and attribution design. Locally bundled or user-selected local files may be used only where the user has a lawful right to use them.

## Product decision

The first routine implementation will provide a configurable **Brahma Muhurta** wake-up routine and a sunset-relative **Evening Prarthana** sequence. The default evening sequence reflects the requested order: Shubham Karoti, Vakratunda Mahakaya, Sukhkarta Dukhharta, and Shirdi Majhe Pandharpur. It will present each item as optional user-configurable local routine content rather than a prescriptive religious authority.

Special-day suggestions will be generated from the app’s offline Panchang model and will be shown as clearly labelled suggestions. They will never silently replace the user’s saved routine or add a network dependency. Any exact local observance date remains subject to the user’s selected city and local tradition.

## Shirdi schedule reference

The official Shree Saibaba Sansthan Trust programme identifies **Shirdi Majhe Pandharpur** in its morning programme and places Dhoop Aarti at sunset. Its official Aarti catalogue also lists Shirdi Maze Pandharpur among the Dhoop Aarti items. The app will cite this as devotional context only: a personal routine must remain user-owned, configurable, and separate from a temple’s own operating schedule. [4] [5]

## Sources

1. [YouTube IFrame Player API Reference](https://developers.google.com/youtube/iframe_api_reference)
2. [YouTube Embedded Players and Player Parameters](https://developers.google.com/youtube/player_parameters)
3. [YouTube API Services Developer Policies](https://developers.google.com/youtube/terms/developer-policies)
4. [Shree Saibaba Sansthan Trust — Daily Programme](https://sai.org.in/en/daily-programme)
5. [Shree Saibaba Sansthan Trust — Aarti catalogue](https://sai.org.in/en/aarties)
