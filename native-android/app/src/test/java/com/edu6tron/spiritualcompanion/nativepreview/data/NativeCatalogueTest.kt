package com.edu6tron.spiritualcompanion.nativepreview.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NativeCatalogueTest {
  @Test
  fun offlineCatalogueHasCoreDevotionalContent() {
    assertTrue(NativeCatalogue.aartis.size >= 6)
    assertTrue(NativeCatalogue.festivals.size >= 9)
    assertTrue(NativeCatalogue.temples.size >= 6)
    assertTrue(NativeCatalogue.aartis.all { it.verses.isNotEmpty() && it.source.isNotBlank() })
    assertTrue(NativeCatalogue.temples.all { it.authority.isNotBlank() && it.sourceUrl.startsWith("https://") })
  }

  @Test
  fun maharashtraSuggestionsPrioritiseRegionalDevotionalContent() {
    val suggestions = NativeCatalogue.suggestionsFor("Pune, Maharashtra")
    assertFalse(suggestions.isEmpty())
    assertTrue(suggestions.any { it.category == "Ganesh" })
    assertTrue(suggestions.any { "Marathi" in it.languages })
  }

  @Test
  fun unknownLocationKeepsFullOfflineLibraryAvailable() {
    assertEquals(NativeCatalogue.aartis, NativeCatalogue.suggestionsFor(""))
  }
}
