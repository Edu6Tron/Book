package com.edu6tron.spiritualcompanion.nativepreview.alarm

import com.edu6tron.spiritualcompanion.nativepreview.data.RitualAlarmEntity
import com.edu6tron.spiritualcompanion.nativepreview.data.days
import com.edu6tron.spiritualcompanion.nativepreview.data.isPaused
import java.util.Calendar
import java.util.TimeZone

/**
 * Pure timing rules shared by the scheduler and user interface. This gives the user the same
 * next-occurrence information that the exact AlarmManager scheduling path uses.
 */
object RitualAlarmTiming {
  fun nextScheduledAt(
    alarm: RitualAlarmEntity,
    nowMillis: Long = System.currentTimeMillis(),
    timeZone: TimeZone = TimeZone.getDefault(),
  ): Long? {
    if (!alarm.enabled || alarm.days().isEmpty() || alarm.isPaused(nowMillis)) return null

    val now = Calendar.getInstance(timeZone).apply { timeInMillis = nowMillis }
    return (0..7).firstNotNullOfOrNull { offset ->
      (now.clone() as Calendar).apply {
        add(Calendar.DATE, offset)
        set(Calendar.HOUR_OF_DAY, alarm.hour)
        set(Calendar.MINUTE, alarm.minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
      }.takeIf { candidate ->
        candidate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY in alarm.days() &&
          candidate.timeInMillis > nowMillis
      }?.timeInMillis
    }
  }
}
