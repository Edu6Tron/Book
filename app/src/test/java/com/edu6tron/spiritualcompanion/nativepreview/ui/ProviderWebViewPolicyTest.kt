package com.edu6tron.spiritualcompanion.nativepreview.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class ProviderWebViewPolicyTest {
  @Test
  fun mobileUserAgentRemovesOnlyEmbeddedWebViewMarkers() {
    val agent = "Mozilla/5.0 (Linux; Android 15; CPH2521 Build/AP3A.240905.015; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/128.0.0.0 Mobile Safari/537.36"

    assertEquals(
      "Mozilla/5.0 (Linux; Android 15; CPH2521 Build/AP3A.240905.015) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Mobile Safari/537.36",
      ProviderWebViewPolicy.mobileUserAgent(agent),
    )
  }
}
