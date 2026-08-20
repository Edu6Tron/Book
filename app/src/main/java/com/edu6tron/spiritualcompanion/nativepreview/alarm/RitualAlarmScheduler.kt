package com.edu6tron.spiritualcompanion.nativepreview.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import com.edu6tron.spiritualcompanion.nativepreview.MainActivity
import com.edu6tron.spiritualcompanion.nativepreview.data.RitualAlarmEntity
import com.edu6tron.spiritualcompanion.nativepreview.data.days

object RitualAlarmScheduler {
  const val ACTION_FIRE = "com.edu6tron.spiritualcompanion.nativepreview.alarm.FIRE"
  const val ACTION_SNOOZED_FIRE = "com.edu6tron.spiritualcompanion.nativepreview.alarm.SNOOZED_FIRE"
  const val ACTION_RESUME_AFTER_PAUSE = "com.edu6tron.spiritualcompanion.nativepreview.alarm.RESUME_AFTER_PAUSE"
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
    cancelScheduledTriggers(context, alarm.id)
    if (!alarm.enabled || alarm.days().isEmpty()) return false
    alarm.pauseUntilMillis?.takeIf { it > System.currentTimeMillis() }?.let { pauseUntil ->
      scheduleAt(context, alarm, pauseUntil, ACTION_RESUME_AFTER_PAUSE)
      return canScheduleExactAlarms(context)
    }
    val nextTriggerAt = RitualAlarmTiming.nextScheduledAt(alarm) ?: return false
    scheduleAt(context, alarm, nextTriggerAt, ACTION_FIRE)
    return canScheduleExactAlarms(context)
  }

  fun snooze(context: Context, alarm: RitualAlarmEntity, minutes: Int = 5) {
    manager(context).cancel(pendingIntent(context, alarm.id, ACTION_SNOOZED_FIRE))
    scheduleAt(
      context,
      alarm,
      System.currentTimeMillis() + minutes.coerceIn(1, 30) * 60_000L,
      ACTION_SNOOZED_FIRE,
    )
  }

  fun cancel(context: Context, id: String) {
    cancelScheduledTriggers(context, id)
    manager(context).cancel(pendingIntent(context, id, ACTION_SNOOZED_FIRE))
  }

  fun canScheduleExactAlarms(context: Context): Boolean =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.S || manager(context).canScheduleExactAlarms()

  /**
   * Battery restrictions are device-controlled and can delay an otherwise valid alarm on some
   * Android builds. The app never requests an exemption automatically; this only informs the
   * person about the current prerequisite.
   */
  fun isIgnoringBatteryOptimizations(context: Context): Boolean =
    context.getSystemService(PowerManager::class.java)
      .isIgnoringBatteryOptimizations(context.packageName)

  fun serviceIntent(context: Context, action: String, alarm: RitualAlarmEntity): Intent =
    Intent(context, RitualAlarmService::class.java).setAction(action).apply { putAlarmExtras(alarm) }

  private fun scheduleAt(context: Context, alarm: RitualAlarmEntity, triggerAt: Long, action: String) {
    val pending = pendingIntent(context, alarm.id, action, alarm)
    if (canScheduleExactAlarms(context) && (action == ACTION_FIRE || action == ACTION_SNOOZED_FIRE)) {
      manager(context).setAlarmClock(
        AlarmManager.AlarmClockInfo(triggerAt, appOpenIntent(context, alarm.id)),
        pending,
      )
    } else if (canScheduleExactAlarms(context)) {
      manager(context).setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
    } else {
      manager(context).setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
    }
  }

  private fun appOpenIntent(context: Context, id: String): PendingIntent =
    PendingIntent.getActivity(
      context,
      17 * id.hashCode(),
      Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP),
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )

  private fun manager(context: Context) = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

  private fun cancelScheduledTriggers(context: Context, id: String) {
    manager(context).cancel(pendingIntent(context, id, ACTION_FIRE))
    manager(context).cancel(pendingIntent(context, id, ACTION_RESUME_AFTER_PAUSE))
  }

  private fun pendingIntent(context: Context, id: String, action: String, alarm: RitualAlarmEntity? = null): PendingIntent {
    val intent = Intent(context, RitualAlarmReceiver::class.java)
      .setAction(action)
      .putExtra(EXTRA_ID, id)
    alarm?.let { intent.putAlarmExtras(it) }
    return PendingIntent.getBroadcast(
      context,
      31 * id.hashCode() + action.hashCode(),
      intent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
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
