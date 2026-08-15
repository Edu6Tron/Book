package com.edu6tron.spiritualcompanion.nativepreview.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_aarti")
data class FavouriteAartiEntity(
  @PrimaryKey val aartiId: String,
  val createdAt: Long = System.currentTimeMillis(),
)

@Entity(tableName = "app_preference")
data class AppPreferenceEntity(
  @PrimaryKey val key: String,
  val value: Long,
)

@Entity(tableName = "string_preference")
data class StringPreferenceEntity(
  @PrimaryKey val key: String,
  val value: String,
)
