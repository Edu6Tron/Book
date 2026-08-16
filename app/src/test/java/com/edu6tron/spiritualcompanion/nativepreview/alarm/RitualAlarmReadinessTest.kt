package com.edu6tron.spiritualcompanion.nativepreview.alarm

import com.edu6tron.spiritualcompanion.nativepreview.data.RitualAlarmEntity
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

class RitualAlarmReadinessTest {
  private val utc = TimeZone.getTimeZone("UTC")
  private val now = utcMillis(2026, Calendar.AUGUST, 17, 4, 0)

  @Test
  fun reportsNoEnabledAlarmWithoutExposingAlarmDetails() {
    val readiness = RitualAlarmReadiness.evaluate(listOf(alarm(enabled = false)), exactAlarmAllowed = false, nowMillis = now)

    assertEquals(RitualAlarmReadiness.Status.NO_ENABLED_ALARMS, readiness.status)
    assertEquals(0, readiness.scheduledAlarmCount)
  }

  @Test
  fun reportsPausedAlarmsBeforeCheckingExactAlarmPermission() {
    val readiness = RitualAlarmReadiness.evaluate(
      listOf(alarm(pauseUntilMillis = now + 60_000L)),
      exactAlarmAllowed = false,
      nowMillis = now,
    )

    assertEquals(RitualAlarmReadiness.Status.ALL_ENABLED_ALARMS_PAUSED, readiness.status)
  }

  @Test
  fun reportsExactAlarmRecoveryWhenAnUpcomingAlarmCannotBeExact() {
    val readiness = RitualAlarmReadiness.evaluate(listOf(alarm()), exactAlarmAllowed = false, nowMillis = now)

    assertEquals(RitualAlarmReadiness.Status.EXACT_ALARM_PERMISSION_NEEDED, readiness.status)
    assertEquals(1, readiness.scheduledAlarmCount)
  }

  @Test
  fun reportsReadyWhenAnUpcomingAlarmHasExactScheduling() {
    val readiness = RitualAlarmReadiness.evaluate(listOf(alarm()), exactAlarmAllowed = true, nowMillis = now)

    assertEquals(RitualAlarmReadiness.Status.READY, readiness.status)
    assertEquals(1, readiness.scheduledAlarmCount)
  }

  private fun alarm(
    enabled: Boolean = true,
    pauseUntilMillis: Long? = null,
  ) = RitualAlarmEntity(
    id = "private-id",
    label = "Private devotional label",
    hour = 4,
    minute = 30,
    repeatDays = "0,1,2,3,4,5,6",
    enabled = enabled,
    pauseUntilMillis = pauseUntilMillis,
  )

  private fun utcMillis(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long =
    Calendar.getInstance(utc).apply {
      clear()
      set(year, month, day, hour, minute, 0)
    }.timeInMillis
}
