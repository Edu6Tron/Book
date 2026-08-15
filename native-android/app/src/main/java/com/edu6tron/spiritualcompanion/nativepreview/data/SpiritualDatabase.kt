package com.edu6tron.spiritualcompanion.nativepreview.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
  entities = [
    DailyPracticeEntity::class,
    FavouriteAartiEntity::class,
    AppPreferenceEntity::class,
    StringPreferenceEntity::class,
    RitualAlarmEntity::class,
    MediaSelectionEntity::class,
  ],
  version = 5,
  exportSchema = false,
)
abstract class SpiritualDatabase : RoomDatabase() {
  abstract fun dailyPracticeDao(): DailyPracticeDao
  abstract fun appStateDao(): AppStateDao
  abstract fun ritualAlarmDao(): RitualAlarmDao
  abstract fun mediaSelectionDao(): MediaSelectionDao
}
