package com.edu6tron.spiritualcompanion.nativepreview.media

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OfflineSoundscapeTest {
  @Test
  fun `soundscape library contains three distinct offline resources`() {
    val soundscapes = OfflineSoundscape.entries

    assertEquals(3, soundscapes.size)
    assertEquals(3, soundscapes.map { it.resourceId }.toSet().size)
    assertTrue(soundscapes.all { it.playbackLabel.endsWith("· offline") })
  }
}
