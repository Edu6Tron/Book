package com.edu6tron.spiritualcompanion.nativepreview.diagnostics

import android.util.Log

/**
 * Emits minimal local diagnostic markers for Logcat without recording personal devotional,
 * location, media-path, or alarm-label data. The original crash handler is always retained.
 */
object NativeDiagnostics {
  private const val TAG = "SpiritualCompanion"

  fun installUncaughtExceptionMarker() {
    val priorHandler = Thread.getDefaultUncaughtExceptionHandler()
    Thread.setDefaultUncaughtExceptionHandler { thread, error ->
      Log.e(TAG, "Fatal native error at ${thread.name}: ${error.javaClass.simpleName}")
      priorHandler?.uncaughtException(thread, error)
    }
  }

  fun recordFailure(component: String, error: Throwable) {
    Log.w(TAG, "$component failed: ${error.javaClass.simpleName}")
  }
}
