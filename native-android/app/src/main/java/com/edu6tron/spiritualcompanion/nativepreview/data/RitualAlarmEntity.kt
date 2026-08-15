package com.edu6tron.spiritualcompanion.nativepreview.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ritual_alarms")
data class RitualAlarmEntity(
  @PrimaryKey val id: String,
  val label: String,
  val hour: Int,
  val minute: Int,
  val repeatDays: String,
  val enabled: Boolean,
  val toneUri: String? = null,
  val afterAlertAartiId: String? = null,
  val pauseUntilMillis: Long? = null,
  val updatedAt: Long = System.currentTimeMillis(),
)

fun RitualAlarmEntity.days(): List<Int> = repeatDays.split(",")
  .mapNotNull(String::toIntOrNull)
  .filter { it in 0..6 }
  .distinct()
  .sorted()

fun RitualAlarmEntity.isPaused(now: Long = System.currentTimeMillis()): Boolean =
  (pauseUntilMillis ?: 0L) > now
