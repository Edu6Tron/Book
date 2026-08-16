package com.edu6tron.spiritualcompanion.nativepreview.data

import com.edu6tron.spiritualcompanion.nativepreview.panchang.PanchangSnapshot
import java.time.LocalTime

/**
 * A concise, personal daily cue derived only from the local Panchang estimate and the routines
 * that the person has chosen to keep enabled. It is guidance, not ritual authority.
 */
data class DevotionalFocus(
  val label: String,
  val title: String,
  val detail: String,
)

object DevotionalFocusResolver {
  private data class Candidate(
    val title: String,
    val time: LocalTime,
    val activeEnd: LocalTime,
    val detail: String,
  )

  fun resolve(
    snapshot: PanchangSnapshot,
    now: LocalTime,
    brahmaMuhurtaEnabled: Boolean,
    eveningRoutineEnabled: Boolean,
  ): DevotionalFocus {
    val candidates = buildList {
      if (brahmaMuhurtaEnabled && snapshot.brahmaMuhurtaStart != null && snapshot.brahmaMuhurtaEnd != null) {
        add(
          Candidate(
            title = "Brahma Muhurta",
            time = snapshot.brahmaMuhurtaStart,
            activeEnd = snapshot.brahmaMuhurtaEnd,
            detail = "A quiet dawn preparation for your personal practice.",
          ),
        )
      }
      if (eveningRoutineEnabled && snapshot.sunset != null) {
        val beginning = snapshot.sunset.minusMinutes(30)
        add(
          Candidate(
            title = "Evening Prarthana",
            time = beginning,
            activeEnd = snapshot.sunset.plusMinutes(45),
            detail = "Your sunset prayer sequence is ready when you are.",
          ),
        )
      }
    }.sortedBy { it.time }

    if (candidates.isEmpty()) {
      return DevotionalFocus(
        label = "PERSONAL PLAN",
        title = "Choose a devotional rhythm",
        detail = "Enable a dawn or evening routine to see a simple daily cue here.",
      )
    }

    candidates.firstOrNull { now >= it.time && now <= it.activeEnd }?.let { active ->
      return DevotionalFocus(
        label = "NOW",
        title = active.title,
        detail = active.detail,
      )
    }

    candidates.firstOrNull { now < it.time }?.let { upcoming ->
      return DevotionalFocus(
        label = "NEXT",
        title = "${upcoming.title} · ${upcoming.time.focusTime()}",
        detail = upcoming.detail,
      )
    }

    return DevotionalFocus(
      label = "TODAY",
      title = "Your planned moments are complete",
      detail = "Return whenever a few quiet minutes feel right; tomorrow’s local timing refreshes automatically.",
    )
  }

  private fun LocalTime.focusTime(): String {
    val sourceHour = hour
    val displayHour = if (sourceHour % 12 == 0) 12 else sourceHour % 12
    val suffix = if (sourceHour < 12) "AM" else "PM"
    return "$displayHour:${minute.toString().padStart(2, '0')} $suffix"
  }
}
