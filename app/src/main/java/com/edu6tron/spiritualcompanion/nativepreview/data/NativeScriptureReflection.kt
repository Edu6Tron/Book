package com.edu6tron.spiritualcompanion.nativepreview.data

import java.time.LocalDate

data class ScriptureReflection(
  val id: String,
  val theme: String,
  val reflection: String,
  val source: String,
  val smallAction: String,
)

/**
 * Short, plain-language reflections inspired by familiar scriptural passages.
 * They are intentionally not presented as authoritative translations or ritual instruction.
 */
object NativeScriptureReflection {
  private val entries = listOf(
    ScriptureReflection("steady-action", "Steady action", "Give sincere attention to the work in front of you; let the outcome unfold without carrying it as a burden.", "Bhagavad Gita 2.47 — plain-language reflection", "Choose one task and finish its next small step with full attention."),
    ScriptureReflection("inner-lift", "Lift the mind gently", "A disciplined mind can become a friend when it is guided with patience rather than force.", "Bhagavad Gita 6.5 — plain-language reflection", "When attention wanders, return once to your breath and the present task."),
    ScriptureReflection("goodwill", "Practise goodwill", "Freedom from needless hostility begins with a small choice to meet others with goodwill.", "Bhagavad Gita 12.13 — plain-language reflection", "Offer one helpful response without needing the last word."),
    ScriptureReflection("truthful-living", "Speak and live truthfully", "Truthfulness and right conduct gain strength when they are practised in ordinary moments.", "Taittiriya Upanishad 1.11 — plain-language reflection", "Let one message or promise today be clear, kind, and reliable."),
    ScriptureReflection("shared-world", "Care for the shared world", "Receive what is needed with gratitude, and remember that life is sustained through interdependence.", "Isha Upanishad 1 — plain-language reflection", "Use one shared resource carefully and avoid waste."),
    ScriptureReflection("choose-light", "Choose clarity", "A movement toward what is true, clear, and life-giving can begin with one honest moment.", "Brihadaranyaka Upanishad 1.3.28 — plain-language reflection", "Name one worry and choose one constructive action instead of repeating it."),
    ScriptureReflection("fearless-step", "Take a courageous step", "The deeper self is invited forward by attentive effort, discernment, and a willingness to grow.", "Katha Upanishad 1.3.14 — plain-language reflection", "Begin the small helpful task you have been postponing."),
  )

  fun forDayOfYear(dayOfYear: Int): ScriptureReflection = entries[Math.floorMod(dayOfYear - 1, entries.size)]

  fun forToday(): ScriptureReflection = forDayOfYear(LocalDate.now().dayOfYear)
}
