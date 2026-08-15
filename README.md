# Spiritual Companion

A production-grade, privacy-first mobile spiritual companion application built with **React Native**, **Expo SDK 54**, and **NativeWind**. Designed for instant offline reliability, secure server-side devotional discovery, government-verified temple directories, and mindful daily rituals.

---

## 🌟 Key Features

- **Instant Offline Library:** Bundled Aartis, Mantras, and Festival calendar accessible without waiting for network connectivity.
- **Location-Aware Devotional Discovery:** Optional location filtering (e.g., Maharashtra, Pune, Odisha) to instantly surface relevant regional traditions and deities.
- **Verified Temple Directory:** Authentic temple records sourced directly from Indian government endowment registers (such as the Odisha Hindu Religious Endowments Department) with complete source provenance and zero GPS tracking requirements.
- **User-Triggered Online Discovery:** Optional secure YouTube Data API v3 integration for exploring fresh devotional media on demand.
- **Local Audio Player & Rituals:** Built-in audio playback for user-owned devotional tracks and daily practice counters (Japa).

---

## 📱 Tech Stack

- **Framework:** Expo SDK 54 (React Native 0.81, React 19)
- **Styling:** NativeWind v4 (Tailwind CSS)
- **Navigation:** Expo Router 6
- **State & Storage:** React Context with AsyncStorage persistence
- **Testing:** Vitest & TypeScript

---

## 🚀 Getting Started & Releases

### Download Latest Release
Visit the [GitHub Releases](https://github.com/Edu6Tron/Book/releases) page to download the latest signed Android APK or release bundles.

### Local Development
```bash
# Clone the repository
git clone https://github.com/Edu6Tron/Book.git
cd Book

# Install dependencies
pnpm install

# Start development server
pnpm dev
```

### Native Android Preview APK

The repository now also contains a separate Kotlin and Jetpack Compose proof-of-foundation under [`native-android/`](native-android/). It includes Room, Hilt, Gradle, and Media3/ExoPlayer, and is kept separate so it does not replace the Expo application.

GitHub Actions builds an unsigned debug APK whenever native Android files change or when you manually run **Native Android APK** from the Actions tab. Download the `spiritual-companion-native-debug-apk` workflow artifact after a successful run. See [`native-android/README.md`](native-android/README.md) for local build instructions and scope.

---

## 🛡️ License & Provenance
Developed as a dedicated devotional companion. All government-sourced temple records and traditional devotional texts are credited to their respective public archives.
