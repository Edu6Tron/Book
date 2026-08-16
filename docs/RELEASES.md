# Native Android Releases

The repository publishes installable debug candidates through GitHub Releases. Each release includes the APK and a matching SHA-256 checksum file. Verify the checksum before installing an APK received from any third party.

| Version | Status | APK | Verification |
|---|---|---|---|
| [`v1.2.0-rc.10`](https://github.com/Edu6Tron/Book/releases/tag/v1.2.0-rc.10) | Current offline-multimedia device-test candidate | [`spiritual-companion-native-v1.2.0-rc.10-debug.apk`](https://github.com/Edu6Tron/Book/releases/download/v1.2.0-rc.10/spiritual-companion-native-v1.2.0-rc.10-debug.apk) | Native unit tests, Android lint, debug assembly, and independent GitHub Actions validation passed. |
| [`v1.1.6-rc.9`](https://github.com/Edu6Tron/Book/releases/tag/v1.1.6-rc.9) | Prior transparent-alarm-status candidate | GitHub Release asset | SHA-256 is published with the release. |
| [`v1.1.5-rc.8`](https://github.com/Edu6Tron/Book/releases/tag/v1.1.5-rc.8) | Prior bounded-discovery candidate | GitHub Release asset | SHA-256 is published with the release. |
| [`v1.1.4-rc.7`](https://github.com/Edu6Tron/Book/releases/tag/v1.1.4-rc.7) | Prior daily-guidance and reading-comfort candidate | GitHub Release asset | SHA-256 is published with the release. |
| [`v1.1.3-rc.6`](https://github.com/Edu6Tron/Book/releases/tag/v1.1.3-rc.6) | Prior clock-picker candidate | GitHub Release asset | SHA-256 is published with the release. |
| [`v1.1.2-rc.5`](https://github.com/Edu6Tron/Book/releases/tag/v1.1.2-rc.5) | Prior scroll-optimised candidate | GitHub Release asset | SHA-256 is published with the release. |
| [`v1.1.1-rc.4`](https://github.com/Edu6Tron/Book/releases/tag/v1.1.1-rc.4) | Prior device-hardening candidate | GitHub Release asset | SHA-256 is published with the release. |
| [`v1.1.0-rc.3`](https://github.com/Edu6Tron/Book/releases/tag/v1.1.0-rc.3) | Prior feature-completion candidate | GitHub Release asset | SHA-256 is published with the release. |

> `v1.2.0-rc.10` is an installable **debug** release candidate for device testing. A Play Store-ready signed APK or AAB is deliberately not published until the repository owner configures the protected GitHub Actions signing secrets described in [Release signing](RELEASE_SIGNING.md).

## Verify an APK

On macOS or Linux, run the following from the directory containing the downloaded files.

```bash
sha256sum -c spiritual-companion-native-v1.2.0-rc.10-debug.apk.sha256
```

The command must report `OK` before installation. Android may require the user to allow installation from the browser or file manager used to open the APK.
