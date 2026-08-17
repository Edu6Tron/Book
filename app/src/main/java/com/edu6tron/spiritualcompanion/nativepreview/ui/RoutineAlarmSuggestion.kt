package com.edu6tron.spiritualcompanion.nativepreview.ui

import com.edu6tron.spiritualcompanion.nativepreview.data.DevotionalRoutineAnchor
import java.time.LocalDate
import java.time.LocalTime

/**
 * A transient editor prefill created from an offline routine estimate.
 *
 * It is deliberately not persisted: the user reviews the label, time, repeat days, and tone in
 * the existing exact-alarm editor before any alarm is saved.
 */
data class RoutineAlarmSuggestion(
  val context: RoutineAlarmContext,
  val time: AlarmTimeSelection,
  val referenceDate: LocalDate,
) {
  val alarmLabel: String get() = context.alarmLabel
  val editorTitle: String get() = "Create ${context.shortTitle} reminder"

  companion object {
    fun from(
      context: RoutineAlarmContext,
      estimatedTime: LocalTime?,
      referenceDate: LocalDate,
    ): RoutineAlarmSuggestion? = estimatedTime?.let {
      RoutineAlarmSuggestion(
        context = context,
        time = AlarmTimeSelection(hour = it.hour, minute = it.minute),
        referenceDate = referenceDate,
      )
    }
  }
}

enum class RoutineAlarmContext(
  val shortTitle: String,
  val alarmLabel: String,
  val anchorDescription: String,
) {
  BRAHMA_MUHURTA(
    shortTitle = "Brahma Muhurta",
    alarmLabel = "Brahma Muhurta",
    anchorDescription = "Brahma Muhurta begins",
  ),
  EVENING_PRARTHANA(
    shortTitle = "Evening Prarthana",
    alarmLabel = "Evening Prarthana",
    anchorDescription = "Sunset estimate",
  );

  companion object {
    fun from(anchor: DevotionalRoutineAnchor): RoutineAlarmContext = when (anchor) {
      DevotionalRoutineAnchor.BRAHMA_MUHURTA -> BRAHMA_MUHURTA
      DevotionalRoutineAnchor.SUNSET -> EVENING_PRARTHANA
    }
  }
}
