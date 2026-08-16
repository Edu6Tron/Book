package com.edu6tron.spiritualcompanion.nativepreview.data

import com.edu6tron.spiritualcompanion.nativepreview.panchang.PanchangSnapshot

enum class DevotionalRoutineAnchor(val title: String) {
  BRAHMA_MUHURTA("Brahma Muhurta"),
  SUNSET("Sunset"),
}

data class DevotionalRoutineStep(
  val id: String,
  val title: String,
  val aartiId: String?,
  val detail: String,
  val recitationLines: List<String> = emptyList(),
)

data class DevotionalRoutineDefinition(
  val id: String,
  val title: String,
  val anchor: DevotionalRoutineAnchor,
  val timingNote: String,
  val steps: List<DevotionalRoutineStep>,
)

data class RoutineSpecialDayGuidance(
  val title: String,
  val detail: String,
  val suggestedAartiIds: List<String>,
)

/**
 * Offline, user-editable routine definitions. These are suggestions, not authoritative ritual
 * instructions; every sequence can be paused, adapted, or used silently as a reading guide.
 */
object NativeDevotionalRoutines {
  val eveningPrarthana = DevotionalRoutineDefinition(
    id = "evening-prarthana",
    title = "Evening prarthana",
    anchor = DevotionalRoutineAnchor.SUNSET,
    timingNote = "Begin around your selected city’s offline sunset estimate. Confirm local observance times when precision is important.",
    steps = listOf(
      DevotionalRoutineStep(
        "shubham-karoti",
        "Shubham Karoti",
        null,
        "Begin with a short light-and-gratitude prayer in your own familiar wording.",
        listOf(
          "Shubham Karoti Kalyanam",
          "Aarogyam Dhanasampada",
          "Shatru Buddhi Vinashaya",
          "Deepa Jyotir Namostute",
        ),
      ),
      DevotionalRoutineStep(
        "vakratunda",
        "Vakratunda Mahakaya",
        null,
        "Pause for the Ganesha invocation before the Aarti sequence.",
        listOf(
          "Vakratunda Mahakaya",
          "Suryakoti Samaprabha",
          "Nirvighnam Kurume Deva",
          "Sarva-Karyeshu Sarvada",
        ),
      ),
      DevotionalRoutineStep("sukhkarta", "Sukhkarta Dukhharta", "sukhkarta-dukhharta", "Read silently, recite, or open the local lyric view."),
      DevotionalRoutineStep("shirdi", "Shirdi Majhe Pandharpur", "shirdi-sai-aarti", "An optional Sai remembrance chosen for this personal routine."),
    ),
  )

  val brahmaMuhurta = DevotionalRoutineDefinition(
    id = "brahma-muhurta",
    title = "Brahma Muhurta start",
    anchor = DevotionalRoutineAnchor.BRAHMA_MUHURTA,
    timingNote = "The offline estimate begins 96 minutes before local sunrise. Use it as a gentle preparation window, not a judgement of your practice.",
    steps = listOf(
      DevotionalRoutineStep("wake", "Wake and settle", null, "Take a quiet moment before reaching for a screen."),
      DevotionalRoutineStep("breath", "Three unhurried breaths", null, "Let the day begin with a comfortable, steady pace."),
      DevotionalRoutineStep("dawn-reading", "Choose a dawn prayer", "kakad-aarti", "Open a local prayer or use the scripture reflection already in Practice."),
      DevotionalRoutineStep("intention", "Set a small intention", null, "Carry one compassionate intention into the day."),
    ),
  )

  val all = listOf(eveningPrarthana, brahmaMuhurta)

  fun specialDayGuidance(snapshot: PanchangSnapshot): RoutineSpecialDayGuidance? = when {
    snapshot.tithi.contains("Ekadashi", ignoreCase = true) -> RoutineSpecialDayGuidance(
      title = "Ekadashi suggestion",
      detail = "For a personal Ekadashi observance, you may choose a simple Pandurang or Vithoba remembrance. This is an offline estimate; follow your family or local Panchang for formal observance.",
      suggestedAartiIds = listOf("vitthal-aarti", "govind-bolo"),
    )
    snapshot.tithi.contains("Purnima", ignoreCase = true) -> RoutineSpecialDayGuidance(
      title = "Purnima suggestion",
      detail = "A quiet gratitude reading or a gentle evening lamp practice can complement the full-moon observance in your own tradition.",
      suggestedAartiIds = listOf("om-jai-jagdish-hare", "om-jai-lakshmi-mata"),
    )
    else -> null
  }
}
