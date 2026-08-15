package com.edu6tron.spiritualcompanion.nativepreview.alarm

import com.edu6tron.spiritualcompanion.nativepreview.data.RitualAlarmDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AlarmRecoveryEntryPoint {
  fun ritualAlarmDao(): RitualAlarmDao
}
