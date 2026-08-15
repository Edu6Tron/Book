# Releases and APK verification

This project uses [GitHub Releases](https://github.com/Edu6Tron/Book/releases) as the permanent download page for Android APKs. GitHub Actions artifacts are retained only for short-term development testing; they are not a substitute for a versioned release.

## Current release candidate

| Field | Value |
|---|---|
| Version | `v1.0.0-rc.1` |
| Package ID | `com.edu6tron.spiritualcompanion.nativepreview` |
| Minimum Android version | Android 8.0 / API 26 |
| Build type | Debug APK for direct testing |
| Distribution | GitHub Release asset |

| Asset | SHA-256 |
|---|---|
| `spiritual-companion-native-v1.0.0-rc.1-debug.apk` | `7bdab8ef1c50d9d84420aa5e6ba29285ff695112b68fee47802b2172db1108d8` |

The release page includes the APK and a `.sha256` checksum file. A later store-ready build will use a release signing key and a different package/distribution configuration.

## Install on an Android device

Download the `.apk` asset from the project’s Releases page. When Android asks, allow the browser or file manager that downloaded the file to install unknown apps, then open the download. This debug APK may show an Android warning because it is not Play Store signed; that warning is expected for a development release.

## Verify the APK before installing

On a computer, run the following command from the directory containing both release assets:

```bash
sha256sum --check spiritual-companion-native-v1.0.0-rc.1-debug.apk.sha256
```

The command must report `OK` before installation. The checksums in a GitHub Release correspond to the exact uploaded file, not an approximate build size.

## What GitHub Actions verifies

Each native Android build runs the debug unit tests and assembles the debug APK. A successful job uploads `spiritual-companion-native-debug-apk` as a temporary workflow artifact. The maintainer then attaches the verified artifact and its checksum to a versioned GitHub Release.
