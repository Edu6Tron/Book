package com.edu6tron.spiritualcompanion.nativepreview.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_selection")
data class MediaSelectionEntity(
  @PrimaryKey val id: String = "devotional_audio",
  val uri: String,
  val label: String,
  val updatedAt: Long,
)
