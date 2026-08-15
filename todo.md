# Native Android Release TODO

- [x] Audit the public repository presentation and remove the obsolete Expo mirror from this native Android repository.
- [x] Replace the root documentation with a Kotlin-first native Android project overview and contribution guide.
- [x] Confirm the Kotlin-first repository presentation after GitHub refreshes language statistics.
- [x] Publish the verified `v1.0.0-rc.1` Android APK and SHA-256 checksum as a GitHub Release.
- [x] Verify the public release page and direct APK download flow on GitHub.
- [ ] Configure release signing secrets before creating a Play Store distribution build.
- [x] Retire the obsolete Tag Notice & Release Metadata workflow and remove its failed historical run so the Actions page shows only reliable release automation.
- [x] Audit the installed native APK against the promised dashboard, Aarti, temple, media, alarm, and persistence flows; document reproducible defects.
- [x] Repair the core Kotlin app flows rather than changing only repository presentation or release metadata.
- [x] Add regression coverage for repaired alarm-tone persistence and validate the debug APK with unit tests, assembly, and lint before publishing a new version.
- [x] Publish a genuinely updated APK with versionCode `3`, release notes, checksum, and a distinct `v1.0.1-rc.2` tag.
