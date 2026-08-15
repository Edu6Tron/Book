package com.edu6tron.spiritualcompanion.nativepreview.data

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RitualAlarmEntityTest {
  private val alarm = RitualAlarmEntity(
    id = "brahma",
    label = "Brahma Muhurta",
    hour = 4,
    minute = 30,
    repeatDays = "0,1,2,3,4,5,6",
    enabled = true,
  )

  @Test
  fun pauseStateExpiresAtTheSelectedTime() {
    val now = 1_000_000L
    assertTrue(alarm.copy(pauseUntilMillis = now + 1).isPaused(now))
    assertFalse(alarm.copy(pauseUntilMillis = now).isPaused(now))
    assertFalse(alarm.copy(pauseUntilMillis = now - 1).isPaused(now))
  }
}
