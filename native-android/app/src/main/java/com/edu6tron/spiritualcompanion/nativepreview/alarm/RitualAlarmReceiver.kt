package com.edu6tron.spiritualcompanion.nativepreview.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class RitualAlarmReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    val alarm = RitualAlarmScheduler.alarmFrom(intent) ?: return
    when (intent.action) {
      RitualAlarmScheduler.ACTION_FIRE -> {
        ContextCompat.startForegroundService(context, RitualAlarmScheduler.serviceIntent(context, RitualAlarmScheduler.ACTION_FIRE, alarm))
        RitualAlarmScheduler.scheduleNext(context, alarm)
      }
      RitualAlarmScheduler.ACTION_SNOOZED_FIRE ->
        ContextCompat.startForegroundService(context, RitualAlarmScheduler.serviceIntent(context, RitualAlarmScheduler.ACTION_FIRE, alarm))
      RitualAlarmScheduler.ACTION_RESUME_AFTER_PAUSE ->
        RitualAlarmScheduler.scheduleNext(context, alarm.copy(pauseUntilMillis = null))
      RitualAlarmScheduler.ACTION_SNOOZE,
      RitualAlarmScheduler.ACTION_STOP -> ContextCompat.startForegroundService(context, RitualAlarmScheduler.serviceIntent(context, intent.action.orEmpty(), alarm))
    }
  }
}
