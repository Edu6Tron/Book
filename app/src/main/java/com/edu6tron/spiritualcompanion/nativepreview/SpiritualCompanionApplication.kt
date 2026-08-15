package com.edu6tron.spiritualcompanion.nativepreview

import android.app.Application
import com.edu6tron.spiritualcompanion.nativepreview.diagnostics.NativeDiagnostics
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SpiritualCompanionApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    NativeDiagnostics.installUncaughtExceptionMarker()
  }
}
