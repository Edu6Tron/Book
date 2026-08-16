package com.edu6tron.spiritualcompanion.nativepreview.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ThemeModeTest {
  @Test
  fun storedThemeValuesAreStableAndUnknownValuesFallBackToLight() {
    assertEquals(ThemeMode.LIGHT, ThemeMode.fromStored("light"))
    assertEquals(ThemeMode.DARK, ThemeMode.fromStored("dark"))
    assertEquals(ThemeMode.SYSTEM, ThemeMode.fromStored("system"))
    assertEquals(ThemeMode.LIGHT, ThemeMode.fromStored("unexpected"))
    assertEquals(ThemeMode.LIGHT, ThemeMode.fromStored(null))
    assertTrue(ThemeMode.entries.map { it.storedValue }.distinct().size == ThemeMode.entries.size)
  }
}
