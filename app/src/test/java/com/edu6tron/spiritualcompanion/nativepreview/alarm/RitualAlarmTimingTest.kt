package com.edu6tron.spiritualcompanion.nativepreview.alarm

import com.edu6tron.spiritualcompanion.nativepreview.data.RitualAlarmEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

class RitualAlarmTimingTest {
  private val utc = TimeZone.getTimeZone("UTC")

  @Test
  fun returnsTheSameDayOccurrenceWhenItIsStillAhead() {
    val now = utcMillis(2026, Calendar.AUGUST, 17, 4, 0) // Monday
    val alarm = alarm(hour = 4, minute = 30, days = "1")

    assertEquals(utcMillis(2026, Calendar.AUGUST, 17, 4, 30), RitualAlarmTiming.nextScheduledAt(alarm, now, utc))
  }

  @Test
  fun movesToTheFollowingSelectedDayAfterTodaysTimeHasPassed() {
    val now = utcMillis(2026, Calendar.AUGUST, 17, 5, 0) // Monday
    val alarm = alarm(hour = 4, minute = 30, days = "1,3")

    assertEquals(utcMillis(2026, Calendar.AUGUST, 19, 4, 30), RitualAlarmTiming.nextScheduledAt(alarm, now, utc))
  }

  @Test
  fun doesNotPresentAnUpcomingTimeForPausedOrDisabledAlarm() {
    val now = utcMillis(2026, Calendar.AUGUST, 17, 4, 0)

    assertNull(RitualAlarmTiming.nextScheduledAt(alarm(enabled = false), now, utc))
    assertNull(RitualAlarmTiming.nextScheduledAt(alarm(pauseUntilMillis = now + 60_000L), now, utc))
  }

  private fun alarm(
    hour: Int = 4,
    minute: Int = 30,
    days: String = "0,1,2,3,4,5,6",
    enabled: Boolean = true,
    pauseUntilMillis: Long? = null,
  ) = RitualAlarmEntity(
    id = "ritual",
    label = "Brahma Muhurta",
    hour = hour,
    minute = minute,
    repeatDays = days,
    enabled = enabled,
    pauseUntilMillis = pauseUntilMillis,
  )

  private fun utcMillis(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long =
    Calendar.getInstance(utc).apply {
      clear()
      set(year, month, day, hour, minute, 0)
    }.timeInMillis
}
