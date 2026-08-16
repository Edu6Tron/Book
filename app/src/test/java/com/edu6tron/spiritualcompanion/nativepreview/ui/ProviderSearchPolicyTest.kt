package com.edu6tron.spiritualcompanion.nativepreview.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProviderSearchPolicyTest {
  @Test
  fun blankSearchUsesTheBundledDevotionalDefault() {
    assertEquals(ProviderSearchPolicy.defaultQuery, ProviderSearchPolicy.normaliseQuery("  "))
  }

  @Test
  fun searchUrlUsesHttpsAndEncodesTheUserQuery() {
    val url = ProviderSearchPolicy.providerSearchUrl("Shiva aarti & lyrics")

    assertTrue(url.startsWith("https://www.youtube.com/results?search_query="))
    assertTrue(url.contains("Shiva+aarti+%26+lyrics"))
    assertFalse(url.contains("Shiva aarti & lyrics"))
  }
}
