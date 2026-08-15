package com.edu6tron.spiritualcompanion.nativepreview.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
  entities = [DailyPracticeEntity::class],
  version = 1,
  exportSchema = false,
)
abstract class SpiritualDatabase : RoomDatabase() {
  abstract fun dailyPracticeDao(): DailyPracticeDao
}
