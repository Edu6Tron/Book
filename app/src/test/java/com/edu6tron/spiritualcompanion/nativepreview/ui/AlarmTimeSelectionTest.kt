package com.edu6tron.spiritualcompanion.nativepreview.ui

import java.util.Calendar
import java.util.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Test

class AlarmTimeSelectionTest {
  @Test
  fun formatsClockTimeWithLeadingZeroes() {
    assertEquals("04:05", AlarmTimeSelection(hour = 4, minute = 5).displayText())
    assertEquals("23:59", AlarmTimeSelection(hour = 23, minute = 59).displayText())
  }

  @Test
  fun currentTimeActionReadsTheCalendarAtTapTime() {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
      set(2026, Calendar.AUGUST, 15, 19, 42)
    }

    assertEquals(AlarmTimeSelection(hour = 19, minute = 42), AlarmTimeSelection.from(calendar))
  }

  @Test
  fun brahmaMuhurtaPresetUsesTheExpectedAlarmTime() {
    assertEquals(AlarmTimeSelection(hour = 4, minute = 30), AlarmTimeSelection.brahmaMuhurta)
  }
}
