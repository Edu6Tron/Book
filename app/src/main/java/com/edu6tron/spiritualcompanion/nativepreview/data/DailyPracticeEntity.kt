package com.edu6tron.spiritualcompanion.nativepreview.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_practice")
data class DailyPracticeEntity(
  @PrimaryKey val id: String,
  val title: String,
  val completed: Boolean,
  val updatedAt: Long,
)
