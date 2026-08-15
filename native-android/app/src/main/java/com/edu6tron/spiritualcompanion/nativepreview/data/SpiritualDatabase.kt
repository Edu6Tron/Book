package com.edu6tron.spiritualcompanion.nativepreview.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
  entities = [DailyPracticeEntity::class, FavouriteAartiEntity::class, AppPreferenceEntity::class, RitualAlarmEntity::class],
  version = 3,
  exportSchema = false,
)
abstract class SpiritualDatabase : RoomDatabase() {
  abstract fun dailyPracticeDao(): DailyPracticeDao
  abstract fun appStateDao(): AppStateDao
  abstract fun ritualAlarmDao(): RitualAlarmDao
}
