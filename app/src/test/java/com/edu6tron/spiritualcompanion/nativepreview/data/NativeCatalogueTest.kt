package com.edu6tron.spiritualcompanion.nativepreview.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NativeCatalogueTest {
  @Test
  fun offlineCatalogueHasCoreDevotionalContent() {
    assertTrue(NativeCatalogue.aartis.size >= 24)
    assertTrue(NativeCatalogue.festivals.size >= 26)
    assertTrue(NativeCatalogue.temples.size >= 23)
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

  @Test
  fun citySuggestionsRemainMeaningfulAcrossSupportedRegions() {
    assertTrue(NativeCatalogue.suggestionsFor("Varanasi, Uttar Pradesh").any { it.category == "Shiva" || it.deity == "Rama" })
    assertTrue(NativeCatalogue.suggestionsFor("Tirupati, Andhra Pradesh").any { it.category == "Vishnu" || it.deity == "Krishna" })
    assertTrue(NativeCatalogue.suggestionsFor("Chennai, Tamil Nadu").any { "Tamil" in it.languages })
    assertTrue(NativeCatalogue.suggestionsFor("Ahmedabad, Gujarat").any { "Gujarati" in it.languages })
    assertTrue(NativeCatalogue.suggestionsFor("Shimla, Himachal Pradesh").any { it.category == "Devi" || it.category == "Hanuman" })
  }
}
