package com.edu6tron.spiritualcompanion.nativepreview.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NativeDailyGuidanceTest {
  @Test
  fun rotatesDeterministicallyAfterTheBundledGuidanceCycle() {
    assertEquals(NativeDailyGuidance.forDayOfYear(1), NativeDailyGuidance.forDayOfYear(8))
  }

  @Test
  fun bundledGuidanceAlwaysProvidesReadableContent() {
    (1..7).forEach { day ->
      val guidance = NativeDailyGuidance.forDayOfYear(day)
      assertTrue(guidance.title.isNotBlank())
      assertTrue(guidance.reflection.isNotBlank())
      assertTrue(guidance.smallAction.isNotBlank())
    }
  }
}
