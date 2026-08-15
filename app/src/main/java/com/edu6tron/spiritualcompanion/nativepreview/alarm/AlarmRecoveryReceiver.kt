package com.edu6tron.spiritualcompanion.nativepreview.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AlarmRecoveryReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    if (intent.action !in RECOVERY_ACTIONS) return
    val pendingResult = goAsync()
    val applicationContext = context.applicationContext
    CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
      try {
        val entryPoint = EntryPointAccessors.fromApplication(applicationContext, AlarmRecoveryEntryPoint::class.java)
        entryPoint.ritualAlarmDao().enabledAlarms().forEach { alarm ->
          RitualAlarmScheduler.scheduleNext(applicationContext, alarm)
        }
      } finally {
        pendingResult.finish()
      }
    }
  }

  private companion object {
    val RECOVERY_ACTIONS = setOf(
      Intent.ACTION_BOOT_COMPLETED,
      Intent.ACTION_TIME_CHANGED,
      Intent.ACTION_TIMEZONE_CHANGED,
      Intent.ACTION_MY_PACKAGE_REPLACED,
    )
  }
}
