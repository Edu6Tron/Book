package com.edu6tron.spiritualcompanion.nativepreview.util

import org.junit.Assert.assertEquals
import org.junit.Test

class PracticeMathTest {
  @Test
  fun computesRemainingRepetitionsForNewAndPartialMalas() {
    assertEquals(108, PracticeMath.remainingToNextMala(0))
    assertEquals(107, PracticeMath.remainingToNextMala(1))
    assertEquals(1, PracticeMath.remainingToNextMala(107))
    assertEquals(108, PracticeMath.remainingToNextMala(108))
    assertEquals(107, PracticeMath.remainingToNextMala(109))
  }
}
