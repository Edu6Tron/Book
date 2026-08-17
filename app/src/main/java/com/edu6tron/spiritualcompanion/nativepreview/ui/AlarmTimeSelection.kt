package com.edu6tron.spiritualcompanion.nativepreview.ui

import java.util.Calendar

data class AlarmTimeSelection(
  val hour: Int,
  val minute: Int,
) {
  init {
    require(hour in 0..23) { "Hour must be between 0 and 23" }
    require(minute in 0..59) { "Minute must be between 0 and 59" }
  }

  fun displayText(): String = "%02d:%02d".format(hour, minute)

  companion object {
    val brahmaMuhurta = AlarmTimeSelection(hour = 4, minute = 30)

    fun from(calendar: Calendar): AlarmTimeSelection = AlarmTimeSelection(
      hour = calendar.get(Calendar.HOUR_OF_DAY),
      minute = calendar.get(Calendar.MINUTE),
    )
  }
}
