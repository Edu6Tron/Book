package com.edu6tron.spiritualcompanion.nativepreview.alarm

import com.edu6tron.spiritualcompanion.nativepreview.data.RitualAlarmEntity

/**
 * A label-free local summary for confirming whether any ritual alarm is ready to fire.
 * It deliberately exposes counts and recovery guidance only, never alarm labels or media paths.
 */
data class RitualAlarmReadiness(
  val status: Status,
  val scheduledAlarmCount: Int,
) {
  enum class Status {
    NO_ENABLED_ALARMS,
    ALL_ENABLED_ALARMS_PAUSED,
    EXACT_ALARM_PERMISSION_NEEDED,
    BACKGROUND_RESTRICTION_MAY_DELAY,
    READY,
  }

  val headline: String
    get() = when (status) {
      Status.NO_ENABLED_ALARMS -> "No ritual alarm is active"
      Status.ALL_ENABLED_ALARMS_PAUSED -> "Active ritual alarms are paused"
      Status.EXACT_ALARM_PERMISSION_NEEDED -> "Allow exact alarms for reliable timing"
      Status.BACKGROUND_RESTRICTION_MAY_DELAY -> "Allow background alarm delivery"
      Status.READY -> "Ritual alarm is ready"
    }

  val detail: String
    get() = when (status) {
      Status.NO_ENABLED_ALARMS -> "Create or enable an alarm below when you want a devotional reminder."
      Status.ALL_ENABLED_ALARMS_PAUSED -> "Resume an alarm below when you are ready for its next scheduled occurrence."
      Status.EXACT_ALARM_PERMISSION_NEEDED -> "$scheduledAlarmCount active alarm${if (scheduledAlarmCount == 1) "" else "s"} can run, but Android may defer the timing until Alarms & reminders is allowed."
      Status.BACKGROUND_RESTRICTION_MAY_DELAY -> "$scheduledAlarmCount active alarm${if (scheduledAlarmCount == 1) "" else "s"} are scheduled, but battery restrictions can delay playback while the screen is off."
      Status.READY -> "$scheduledAlarmCount active alarm${if (scheduledAlarmCount == 1) "" else "s"} will use the local schedule and bundled fallback chime if a selected file is unavailable."
    }

  companion object {
    fun evaluate(
      alarms: List<RitualAlarmEntity>,
      exactAlarmAllowed: Boolean,
      batteryOptimizationsIgnored: Boolean = true,
      nowMillis: Long = System.currentTimeMillis(),
    ): RitualAlarmReadiness {
      val enabled = alarms.filter { it.enabled }
      if (enabled.isEmpty()) return RitualAlarmReadiness(Status.NO_ENABLED_ALARMS, 0)

      val scheduledCount = enabled.count { RitualAlarmTiming.nextScheduledAt(it, nowMillis) != null }
      if (scheduledCount == 0) return RitualAlarmReadiness(Status.ALL_ENABLED_ALARMS_PAUSED, 0)

      return RitualAlarmReadiness(
        status = when {
          !exactAlarmAllowed -> Status.EXACT_ALARM_PERMISSION_NEEDED
          !batteryOptimizationsIgnored -> Status.BACKGROUND_RESTRICTION_MAY_DELAY
          else -> Status.READY
        },
        scheduledAlarmCount = scheduledCount,
      )
    }
  }
}
