package com.edu6tron.spiritualcompanion.nativepreview.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.pow

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

  @Test
  fun `all devotional palettes retain readable foreground contrast`() {
    DevotionalTheme.entries.forEach { theme ->
      val primary = theme.previewPrimaryArgb
      val accent = theme.previewAccentArgb
      assertTrue("${theme.label} primary must remain readable on light controls", contrastRatio(0xFFFFFFFFL, primary) >= 4.5)
      assertTrue("${theme.label} accent must remain readable in dark controls", contrastRatio(0xFF211A16L, accent) >= 4.5)
      assertTrue("${theme.label} dark container foreground must remain readable", contrastRatio(primary, accent) >= 4.5)
    }
  }

  private fun contrastRatio(first: Long, second: Long): Double {
    val light = relativeLuminance(first)
    val dark = relativeLuminance(second)
    return (maxOf(light, dark) + 0.05) / (minOf(light, dark) + 0.05)
  }

  private fun relativeLuminance(argb: Long): Double {
    fun channel(shift: Int): Double {
      val value = ((argb shr shift) and 0xFFL).toDouble() / 255.0
      return if (value <= 0.04045) value / 12.92 else ((value + 0.055) / 1.055).pow(2.4)
    }
    return 0.2126 * channel(16) + 0.7152 * channel(8) + 0.0722 * channel(0)
  }
}
