package com.edu6tron.spiritualcompanion.nativepreview.alarm

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.edu6tron.spiritualcompanion.nativepreview.data.RitualAlarmEntity

class RitualAlarmActivity : Activity() {
  private lateinit var alarm: RitualAlarmEntity

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    alarm = RitualAlarmScheduler.alarmFrom(intent) ?: run { finish(); return }
    setShowWhenLocked(true)
    setTurnScreenOn(true)
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    val padding = (28 * resources.displayMetrics.density).toInt()
    val panel = LinearLayout(this).apply {
      orientation = LinearLayout.VERTICAL
      setPadding(padding, padding, padding, padding)
      gravity = android.view.Gravity.CENTER
    }
    panel.addView(TextView(this).apply {
      text = "Ritual alarm\n${alarm.label}"
      gravity = android.view.Gravity.CENTER
      textSize = 30f
      setPadding(0, 0, 0, padding)
    })
    panel.addView(Button(this).apply {
      text = "Snooze 5 minutes"
      setOnClickListener { dispatch(RitualAlarmScheduler.ACTION_SNOOZE) }
    }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
    panel.addView(Button(this).apply {
      text = "Stop"
      setOnClickListener { dispatch(RitualAlarmScheduler.ACTION_STOP) }
    }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { topMargin = padding / 2 })
    setContentView(panel)
  }

  private fun dispatch(action: String) {
    ContextCompat.startForegroundService(this, RitualAlarmScheduler.serviceIntent(this, action, alarm))
    finish()
  }
}
