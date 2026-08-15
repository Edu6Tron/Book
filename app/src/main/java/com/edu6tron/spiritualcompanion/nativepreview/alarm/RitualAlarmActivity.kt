package com.edu6tron.spiritualcompanion.nativepreview.alarm

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.edu6tron.spiritualcompanion.nativepreview.data.RitualAlarmEntity

class RitualAlarmActivity : Activity() {
  private lateinit var alarm: RitualAlarmEntity

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    alarm = RitualAlarmScheduler.alarmFrom(intent) ?: run { finish(); return }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
      setShowWhenLocked(true)
      setTurnScreenOn(true)
    } else {
      @Suppress("DEPRECATION")
      window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
    }
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    val padding = (28 * resources.displayMetrics.density).toInt()
    val panel = LinearLayout(this).apply {
      orientation = LinearLayout.VERTICAL
      setPadding(padding, padding, padding, padding)
      gravity = android.view.Gravity.CENTER
      setBackgroundColor(Color.rgb(44, 24, 10))
    }
    panel.addView(TextView(this).apply {
      text = "Ritual alarm\n${String.format(java.util.Locale.getDefault(), "%02d:%02d", alarm.hour, alarm.minute)}\n${alarm.label}\n${if (alarm.toneUri == null) "Bundled devotional chime" else "Selected local tone"}"
      gravity = android.view.Gravity.CENTER
      textSize = 30f
      setTextColor(Color.WHITE)
      setPadding(0, 0, 0, padding)
    })
    panel.addView(Button(this).apply {
      text = "Snooze 5 minutes"
      setOnClickListener { dispatch(RitualAlarmScheduler.ACTION_SNOOZE) }
    }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
    panel.addView(Button(this).apply {
      text = "Snooze 10 minutes"
      setOnClickListener { dispatch(RitualAlarmScheduler.ACTION_SNOOZE, 10) }
    }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { topMargin = padding / 3 })
    panel.addView(Button(this).apply {
      text = "Stop"
      setOnClickListener { dispatch(RitualAlarmScheduler.ACTION_STOP) }
    }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { topMargin = padding / 2 })
    setContentView(panel)
  }

  private fun dispatch(action: String, snoozeMinutes: Int = 5) {
    ContextCompat.startForegroundService(this, RitualAlarmScheduler.serviceIntent(this, action, alarm).putExtra("ritual_alarm_snooze_minutes", snoozeMinutes))
    finish()
  }
}
