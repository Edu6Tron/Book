package com.edu6tron.spiritualcompanion.nativepreview.data

import java.time.LocalDate

data class DailyGuidance(
  val id: String,
  val title: String,
  val reflection: String,
  val smallAction: String,
)

object NativeDailyGuidance {
  private val entries = listOf(
    DailyGuidance("stillness", "Begin with stillness", "A quiet beginning gives the day a steady centre.", "Take three unhurried breaths before your next task."),
    DailyGuidance("attention", "Offer full attention", "A small act becomes meaningful when it is done with care.", "Give one ordinary task your complete attention."),
    DailyGuidance("gratitude", "Notice what sustains you", "Gratitude turns a familiar day into a received gift.", "Name one person, place, or kindness you appreciate."),
    DailyGuidance("kindness", "Let practice become kindness", "Devotion is carried into the world through gentle action.", "Offer one sincere word or helpful gesture."),
    DailyGuidance("patience", "Make room for patience", "A pause can be more powerful than a hurried answer.", "Wait for one full breath before responding to difficulty."),
    DailyGuidance("service", "Serve without display", "Quiet service strengthens both the giver and the moment.", "Help with one task without needing recognition."),
    DailyGuidance("reflection", "Close the day with reflection", "Remembering the good in a day makes tomorrow easier to begin.", "Before rest, recall one moment you would like to carry forward."),
  )

  fun forDayOfYear(dayOfYear: Int): DailyGuidance = entries[Math.floorMod(dayOfYear - 1, entries.size)]

  fun forToday(): DailyGuidance = forDayOfYear(LocalDate.now().dayOfYear)
}
