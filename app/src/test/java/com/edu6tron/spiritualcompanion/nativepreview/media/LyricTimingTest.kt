package com.edu6tron.spiritualcompanion.nativepreview.media

import org.junit.Assert.assertEquals
import org.junit.Test

class LyricTimingTest {
  @Test
  fun `returns no highlighted verse without usable duration or verses`() {
    assertEquals(-1, LyricTiming.activeVerseIndex(100L, 0L, 4))
    assertEquals(-1, LyricTiming.activeVerseIndex(100L, 1_000L, 0))
  }

  @Test
  fun `maps duration proportionally and bounds final position`() {
    assertEquals(0, LyricTiming.activeVerseIndex(0L, 1_000L, 4))
    assertEquals(1, LyricTiming.activeVerseIndex(490L, 1_000L, 4))
    assertEquals(3, LyricTiming.activeVerseIndex(1_000L, 1_000L, 4))
    assertEquals(3, LyricTiming.activeVerseIndex(9_000L, 1_000L, 4))
  }
}
