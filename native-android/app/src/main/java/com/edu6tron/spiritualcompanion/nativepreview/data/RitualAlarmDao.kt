package com.edu6tron.spiritualcompanion.nativepreview.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RitualAlarmDao {
  @Query("SELECT * FROM ritual_alarms ORDER BY hour, minute")
  fun observeAll(): Flow<List<RitualAlarmEntity>>

  @Query("SELECT * FROM ritual_alarms WHERE enabled = 1")
  suspend fun enabledAlarms(): List<RitualAlarmEntity>

  @Upsert
  suspend fun upsert(alarm: RitualAlarmEntity)

  @Delete
  suspend fun delete(alarm: RitualAlarmEntity)
}
