package com.edu6tron.spiritualcompanion.nativepreview.di

import android.content.Context
import androidx.room.Room
import com.edu6tron.spiritualcompanion.nativepreview.data.AppStateDao
import com.edu6tron.spiritualcompanion.nativepreview.data.DailyPracticeDao
import com.edu6tron.spiritualcompanion.nativepreview.data.MediaSelectionDao
import com.edu6tron.spiritualcompanion.nativepreview.data.RitualAlarmDao
import com.edu6tron.spiritualcompanion.nativepreview.data.SpiritualDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
  @Provides
  @Singleton
  fun provideDatabase(@ApplicationContext context: Context): SpiritualDatabase =
    Room.databaseBuilder(context, SpiritualDatabase::class.java, "spiritual-companion.db")
      .fallbackToDestructiveMigration()
      .build()

  @Provides
  fun provideDailyPracticeDao(database: SpiritualDatabase): DailyPracticeDao = database.dailyPracticeDao()

  @Provides
  fun provideAppStateDao(database: SpiritualDatabase): AppStateDao = database.appStateDao()

  @Provides
  fun provideRitualAlarmDao(database: SpiritualDatabase): RitualAlarmDao = database.ritualAlarmDao()

  @Provides
  fun provideMediaSelectionDao(database: SpiritualDatabase): MediaSelectionDao = database.mediaSelectionDao()
}
