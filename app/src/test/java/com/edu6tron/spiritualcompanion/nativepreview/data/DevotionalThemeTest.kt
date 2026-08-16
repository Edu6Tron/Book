package com.edu6tron.spiritualcompanion.nativepreview.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DevotionalThemeTest {
  @Test
  fun `all devotional palettes restore from their stable local values`() {
    DevotionalTheme.entries.forEach { theme ->
      assertEquals(theme, DevotionalTheme.fromStored(theme.storedValue))
    }
    assertEquals(11, DevotionalTheme.entries.size)
    assertTrue(DevotionalTheme.entries.map { it.storedValue }.distinct().size == DevotionalTheme.entries.size)
  }

  @Test
  fun `missing or unsupported palette falls back to sacred saffron`() {
    assertEquals(DevotionalTheme.SACRED_SAFFRON, DevotionalTheme.fromStored(null))
    assertEquals(DevotionalTheme.SACRED_SAFFRON, DevotionalTheme.fromStored("not-a-theme"))
  }
}
