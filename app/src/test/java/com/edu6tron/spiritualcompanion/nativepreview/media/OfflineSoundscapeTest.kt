package com.edu6tron.spiritualcompanion.nativepreview.media

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OfflineSoundscapeTest {
  @Test
  fun `soundscape library contains six distinct offline resources`() {
    val soundscapes = OfflineSoundscape.entries

    assertEquals(6, soundscapes.size)
    assertEquals(6, soundscapes.map { it.resourceId }.toSet().size)
    assertTrue(soundscapes.all { it.playbackLabel.endsWith("· offline") })
    assertTrue(soundscapes.map { it.title }.containsAll(listOf("Temple bells & stillness", "Monsoon reflection", "Lamp-flame stillness")))
  }
}
