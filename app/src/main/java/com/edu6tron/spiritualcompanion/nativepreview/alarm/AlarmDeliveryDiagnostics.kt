package com.edu6tron.spiritualcompanion.nativepreview.alarm

import android.content.Context

/**
 * A deliberately small, device-local record of the last alarm-delivery stage.
 *
 * Only an enum token and an epoch timestamp are retained. This never stores an alarm ID or
 * label, media URI or file path, location, account information, search query, exception message,
 * or any other user-entered content. The record is overwritten on every new stage and is not
 * transmitted by this component.
 */
data class AlarmDeliveryDiagnostic(
  val stage: AlarmDeliveryStage,
  val occurredAtMillis: Long,
)

enum class AlarmDeliveryStage(
  val storedValue: String,
  val userFacingSummary: String,
) {
  RECEIVER_FIRED(
    storedValue = "receiver_fired",
    userFacingSummary = "Android delivered the scheduled alarm to the app",
  ),
  SERVICE_START_REJECTED(
    storedValue = "service_start_rejected",
    userFacingSummary = "Android did not allow the alarm service to start",
  ),
  FOREGROUND_SERVICE_STARTED(
    storedValue = "foreground_service_started",
    userFacingSummary = "The foreground alarm service started",
  ),
  FOREGROUND_SERVICE_FAILED(
    storedValue = "foreground_service_failed",
    userFacingSummary = "The foreground alarm service could not be started",
  ),
  LOCAL_TONE_STARTED(
    storedValue = "local_tone_started",
    userFacingSummary = "The selected local alarm tone began playing",
  ),
  FALLBACK_TONE_STARTED(
    storedValue = "fallback_tone_started",
    userFacingSummary = "The bundled offline alarm tone began playing",
  ),
  PLAYBACK_FAILED(
    storedValue = "playback_failed",
    userFacingSummary = "The alarm service started, but audio playback did not begin",
  );

  companion object {
    fun fromStoredValue(value: String?): AlarmDeliveryStage? =
      entries.firstOrNull { it.storedValue == value }
  }
}

object AlarmDeliveryDiagnostics {
  private const val PREFERENCES_NAME = "alarm_delivery_diagnostics_v1"
  private const val STAGE_KEY = "last_stage"
  private const val OCCURRED_AT_KEY = "last_stage_time"

  fun record(
    context: Context,
    stage: AlarmDeliveryStage,
    occurredAtMillis: Long = System.currentTimeMillis(),
  ) {
    if (occurredAtMillis <= 0L) return
    context.applicationContext
      .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
      .edit()
      .putString(STAGE_KEY, stage.storedValue)
      .putLong(OCCURRED_AT_KEY, occurredAtMillis)
      .apply()
  }

  fun read(context: Context): AlarmDeliveryDiagnostic? {
    val preferences = context.applicationContext
      .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    val stage = AlarmDeliveryStage.fromStoredValue(preferences.getString(STAGE_KEY, null)) ?: return null
    val occurredAtMillis = preferences.getLong(OCCURRED_AT_KEY, 0L)
    return if (occurredAtMillis > 0L) AlarmDeliveryDiagnostic(stage, occurredAtMillis) else null
  }

  fun clear(context: Context) {
    context.applicationContext
      .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
      .edit()
      .clear()
      .apply()
  }
}
