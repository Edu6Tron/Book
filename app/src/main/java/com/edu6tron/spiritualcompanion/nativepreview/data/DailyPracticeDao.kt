package com.edu6tron.spiritualcompanion.nativepreview.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyPracticeDao {
  @Query("SELECT * FROM daily_practice ORDER BY updatedAt DESC")
  fun observeAll(): Flow<List<DailyPracticeEntity>>

  @Query("SELECT * FROM daily_practice WHERE id = :id LIMIT 1")
  suspend fun findById(id: String): DailyPracticeEntity?

  @Upsert
  suspend fun upsert(practice: DailyPracticeEntity)
}
