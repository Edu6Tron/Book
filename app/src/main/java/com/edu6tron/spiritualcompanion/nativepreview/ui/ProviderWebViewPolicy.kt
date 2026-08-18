package com.edu6tron.spiritualcompanion.nativepreview.ui

/**
 * Keeps the user-initiated official provider surface in its ordinary mobile-web rendering mode.
 * The app does not inject, restyle, or otherwise alter the provider page itself.
 */
object ProviderWebViewPolicy {
  fun mobileUserAgent(defaultUserAgent: String): String = defaultUserAgent
    .replace("; wv", "")
    .replace(" Version/4.0", "")
}
