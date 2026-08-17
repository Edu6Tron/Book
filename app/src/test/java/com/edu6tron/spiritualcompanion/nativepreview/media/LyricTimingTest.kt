package com.edu6tron.spiritualcompanion.nativepreview.media

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
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

  @Test
  fun `personal timing profile must be complete nonnegative and monotonic`() {
    assertTrue(LyricTiming.isValidProfile(listOf(0L, 1_200L, 4_800L), 3))
    assertFalse(LyricTiming.isValidProfile(listOf(0L, -1L, 4_800L), 3))
    assertFalse(LyricTiming.isValidProfile(listOf(0L, 4_800L, 1_200L), 3))
    assertFalse(LyricTiming.isValidProfile(listOf(0L, 1_200L), 3))
  }

  @Test
  fun `complete personal timing takes priority over proportional guided pace`() {
    val offsets = listOf(0L, 2_000L, 8_000L)
    assertEquals(0, LyricTiming.activeLineIndex(1_500L, 12_000L, 3, offsets))
    assertEquals(1, LyricTiming.activeLineIndex(2_000L, 12_000L, 3, offsets))
    assertEquals(2, LyricTiming.activeLineIndex(11_500L, 12_000L, 3, offsets))
    assertEquals(2_000L, LyricTiming.offsetForLine(offsets, 1, 3))
  }

  @Test
  fun `invalid or absent markers fall back safely to guided pace`() {
    assertEquals(2, LyricTiming.activeLineIndex(9_000L, 12_000L, 3, listOf(0L, -1L, 8_000L)))
    assertNull(LyricTiming.offsetForLine(listOf(0L, -1L, 8_000L), 1, 3))
  }

  @Test
  fun `marker capture keeps incomplete profiles in memory and rejects order violations`() {
    val first = LyricTiming.withOffset(emptyList(), 0, 1_000L, 3)
    assertEquals(listOf(1_000L, -1L, -1L), first)
    assertFalse(LyricTiming.isValidProfile(first.orEmpty(), 3))

    val second = LyricTiming.withOffset(first.orEmpty(), 1, 4_000L, 3)
    val complete = LyricTiming.withOffset(second.orEmpty(), 2, 7_000L, 3)
    assertTrue(LyricTiming.isValidProfile(complete.orEmpty(), 3))
    assertNull(LyricTiming.withOffset(complete.orEmpty(), 1, 500L, 3))
  }
}
