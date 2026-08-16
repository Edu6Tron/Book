package com.edu6tron.spiritualcompanion.nativepreview.data

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GuidedPracticeJourneyTest {
  @Test
  fun `offline journeys are distinct and provide actionable steps`() {
    val journeys = NativeGuidedPracticeJourneys.all

    assertEquals(3, journeys.size)
    assertEquals(journeys.size, journeys.map { it.id }.toSet().size)
    assertTrue(journeys.all { it.steps.size >= 4 && it.artworkResId != 0 })
  }

  @Test
  fun `featured journey is deterministic for a date`() {
    val date = LocalDate.of(2026, 8, 16)

    assertEquals(
      NativeGuidedPracticeJourneys.featuredFor(date),
      NativeGuidedPracticeJourneys.featuredFor(date),
    )
  }
}
