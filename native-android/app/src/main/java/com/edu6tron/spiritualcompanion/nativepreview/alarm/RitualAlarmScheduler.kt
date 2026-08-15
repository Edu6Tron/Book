package com.edu6tron.spiritualcompanion.nativepreview.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.edu6tron.spiritualcompanion.nativepreview.data.RitualAlarmEntity
import com.edu6tron.spiritualcompanion.nativepreview.data.days
import java.util.Calendar

object RitualAlarmScheduler {
  const val ACTION_FIRE = "com.edu6tron.spiritualcompanion.nativepreview.alarm.FIRE"
  const val ACTION_STOP = "com.edu6tron.spiritualcompanion.nativepreview.alarm.STOP"
  const val ACTION_SNOOZE = "com.edu6tron.spiritualcompanion.nativepreview.alarm.SNOOZE"
  const val EXTRA_ID = "ritual_alarm_id"
  const val EXTRA_LABEL = "ritual_alarm_label"
  const val EXTRA_HOUR = "ritual_alarm_hour"
  const val EXTRA_MINUTE = "ritual_alarm_minute"
  const val EXTRA_DAYS = "ritual_alarm_days"
  const val EXTRA_TONE_URI = "ritual_alarm_tone_uri"
  const val EXTRA_AFTER_ALERT = "ritual_alarm_after_alert"

  fun scheduleNext(context: Context, alarm: RitualAlarmEntity): Boolean {
    cancel(context, alarm.id)
    if (!alarm.enabled || alarm.days().isEmpty()) return false
    val now = Calendar.getInstance()
    val candidate = (0..7).mapNotNull { offset ->
      Calendar.getInstance().apply {
        add(Calendar.DATE, offset)
        set(Calendar.HOUR_OF_DAY, alarm.hour)
        set(Calendar.MINUTE, alarm.minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
      }.takeIf { it.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY in alarm.days() && it.after(now) }
    }.firstOrNull() ?: return false
    scheduleAt(context, alarm, candidate.timeInMillis)
    return canScheduleExactAlarms(context)
  }

  fun snooze(context: Context, alarm: RitualAlarmEntity, minutes: Int = 5) {
    scheduleAt(context, alarm, System.currentTimeMillis() + minutes.coerceIn(1, 30) * 60_000L)
  }

  fun cancel(context: Context, id: String) {
    manager(context).cancel(pendingIntent(context, id))
  }

  fun canScheduleExactAlarms(context: Context): Boolean =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.S || manager(context).canScheduleExactAlarms()

  fun serviceIntent(context: Context, action: String, alarm: RitualAlarmEntity): Intent =
    Intent(context, RitualAlarmService::class.java).setAction(action).apply { putAlarmExtras(alarm) }

  private fun scheduleAt(context: Context, alarm: RitualAlarmEntity, triggerAt: Long) {
    val pending = pendingIntent(context, alarm.id, alarm)
    if (canScheduleExactAlarms(context)) {
      manager(context).setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
    } else {
      manager(context).setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
    }
  }

  private fun manager(context: Context) = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

  private fun pendingIntent(context: Context, id: String, alarm: RitualAlarmEntity? = null): PendingIntent {
    val intent = Intent(context, RitualAlarmReceiver::class.java)
      .setAction(ACTION_FIRE)
      .putExtra(EXTRA_ID, id)
    alarm?.let { intent.putAlarmExtras(it) }
    return PendingIntent.getBroadcast(context, id.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
  }

  fun Intent.putAlarmExtras(alarm: RitualAlarmEntity) {
    putExtra(EXTRA_ID, alarm.id)
    putExtra(EXTRA_LABEL, alarm.label)
    putExtra(EXTRA_HOUR, alarm.hour)
    putExtra(EXTRA_MINUTE, alarm.minute)
    putExtra(EXTRA_DAYS, alarm.repeatDays)
    putExtra(EXTRA_TONE_URI, alarm.toneUri)
    putExtra(EXTRA_AFTER_ALERT, alarm.afterAlertAartiId)
  }

  fun alarmFrom(intent: Intent): RitualAlarmEntity? {
    val id = intent.getStringExtra(EXTRA_ID) ?: return null
    val label = intent.getStringExtra(EXTRA_LABEL) ?: return null
    return RitualAlarmEntity(
      id = id,
      label = label,
      hour = intent.getIntExtra(EXTRA_HOUR, 6),
      minute = intent.getIntExtra(EXTRA_MINUTE, 0),
      repeatDays = intent.getStringExtra(EXTRA_DAYS).orEmpty(),
      enabled = true,
      toneUri = intent.getStringExtra(EXTRA_TONE_URI),
      afterAlertAartiId = intent.getStringExtra(EXTRA_AFTER_ALERT),
    )
  }
}
