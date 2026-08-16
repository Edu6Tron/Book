package com.edu6tron.spiritualcompanion.nativepreview.data

import org.junit.Assert.assertEquals
import org.junit.Test

class ReadingComfortTest {
  @Test
  fun restoresEveryPersistedReadingComfortOption() {
    ReadingComfort.entries.forEach { option ->
      assertEquals(option, ReadingComfort.fromStored(option.storedValue))
    }
  }

  @Test
  fun defaultsToStandardForMissingOrUnknownPreferences() {
    assertEquals(ReadingComfort.STANDARD, ReadingComfort.fromStored(null))
    assertEquals(ReadingComfort.STANDARD, ReadingComfort.fromStored("unsupported"))
  }
}
