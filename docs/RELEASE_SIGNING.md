# Android release signing

The debug APKs attached to pre-releases are suitable for device testing. A Play Store or long-term direct-distribution release must be signed with a private keystore that is **never committed to this repository**.

## One-time keystore preparation

Create and retain the keystore in an encrypted, access-controlled location. Then base64-encode the binary file as a single line before entering it in GitHub.

```bash
base64 --wrap=0 spiritual-companion-release.jks > spiritual-companion-release.jks.base64
```

Add the following **repository or production-environment secrets** in GitHub. Do not place their values in source code, issues, release notes, or chat messages.

| GitHub secret | Purpose |
|---|---|
| `ANDROID_KEYSTORE_BASE64` | Base64 representation of the `.jks` keystore file. |
| `ANDROID_KEYSTORE_PASSWORD` | Password protecting the keystore. |
| `ANDROID_KEY_ALIAS` | Alias of the application signing key. |
| `ANDROID_KEY_PASSWORD` | Password for the signing key. |

## Creating a signed release

First create the GitHub Release and its tag. Then open the **Signed Android Release** workflow, choose **Run workflow**, and provide the existing release tag. The workflow verifies that every secret is present, restores the keystore only inside the temporary runner directory, creates a signed APK and AAB, calculates SHA-256 checksums, and attaches them to that release.

> The signing workflow is deliberately manual and protected by the `production` environment. It never runs automatically on a normal code push and it fails safely when a required secret is absent.

## Recovery and key safety

Retain at least two protected backups of the keystore and its passwords. Losing the original app-signing key prevents future update releases under the same Android application ID. Restrict GitHub production-environment access to maintainers who are authorised to publish the application.
