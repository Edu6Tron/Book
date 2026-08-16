package com.edu6tron.spiritualcompanion.nativepreview.data

import com.edu6tron.spiritualcompanion.nativepreview.R
import java.time.LocalDate

/**
 * Small, optional practice prompts kept entirely in the app bundle.
 * They are invitations for reflection, not ritual instructions.
 */
data class GuidedPracticeJourney(
  val id: String,
  val title: String,
  val moment: String,
  val durationLabel: String,
  val summary: String,
  val intention: String,
  val steps: List<String>,
  val artworkResId: Int,
  val artworkDescription: String,
)

object NativeGuidedPracticeJourneys {
  val all: List<GuidedPracticeJourney> = listOf(
    GuidedPracticeJourney(
      id = "dawn_sankalpa",
      title = "Dawn sankalpa",
      moment = "Begin gently",
      durationLabel = "5–8 min",
      summary = "A quiet beginning to set one kind, practical intention for the day.",
      intention = "May my next small action be steady and kind.",
      steps = listOf(
        "Find a comfortable seat and take three unhurried breaths.",
        "Notice one thing for which you feel thankful this morning.",
        "Choose one realistic act of care for yourself or another person.",
        "Carry that intention into the next part of your day.",
      ),
      artworkResId = R.drawable.dawn_sankalpa_scene,
      artworkDescription = "Dawn lamp and temple landscape illustration",
    ),
    GuidedPracticeJourney(
      id = "evening_deepa",
      title = "Evening deepa pause",
      moment = "Close with gratitude",
      durationLabel = "6–10 min",
      summary = "A screen-free pause to notice what can be released before the evening settles.",
      intention = "May I meet this evening with gratitude and ease.",
      steps = listOf(
        "Set aside your screen and soften the light around you if you can.",
        "Recall one effort you made today, however small.",
        "Offer silent gratitude for one person, place, or moment.",
        "Let one unfinished concern wait until tomorrow.",
      ),
      artworkResId = R.drawable.evening_deepa_scene,
      artworkDescription = "Glowing evening lamps in a temple courtyard illustration",
    ),
    GuidedPracticeJourney(
      id = "japa_garden",
      title = "Japa garden focus",
      moment = "Return to the count",
      durationLabel = "8–12 min",
      summary = "A simple preparation for a calm count, your chosen prayer, or a few minutes of silence.",
      intention = "May my attention return gently whenever it wanders.",
      steps = listOf(
        "Choose a quiet place and settle into an easy posture.",
        "Decide on a small, unpressured count or a short period of silence.",
        "Return to your chosen word, breath, or count without judging distractions.",
        "Pause at the end and notice one quality you wish to carry forward.",
      ),
      artworkResId = R.drawable.japa_garden_scene,
      artworkDescription = "Mala beside lotus leaves in a dawn garden illustration",
    ),
  )

  fun featuredFor(date: LocalDate): GuidedPracticeJourney = all[date.dayOfYear % all.size]
}
