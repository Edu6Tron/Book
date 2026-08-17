package com.edu6tron.spiritualcompanion.nativepreview.data

import com.edu6tron.spiritualcompanion.nativepreview.panchang.PanchangSnapshot
import java.time.LocalDate
import java.time.YearMonth

/** Explains how a calendar item entered the app so a calculated cue is never confused with a published date. */
enum class MaharashtraCalendarSourceTier(
  val label: String,
  val disclosure: String,
) {
  GOVERNMENT_PUBLISHED(
    label = "Government-published date",
    disclosure = "Bundled from the listed Maharashtra public-holiday source for 2026.",
  ),
  CURATED_DEVOTIONAL_GUIDE(
    label = "Curated devotional guide",
    disclosure = "A reading or practice suggestion, not an official date declaration.",
  ),
  LOCAL_PANCHANG_ESTIMATE(
    label = "Local Panchang estimate",
    disclosure = "Calculated offline for the selected place; confirm ritual-critical timing locally.",
  ),
  PERSONAL_PLAN(
    label = "Personal plan",
    disclosure = "Created by the person using this device.",
  ),
}

data class MaharashtraCalendarObservance(
  val id: String,
  val name: String,
  val date: LocalDate,
  val category: String,
  val source: String,
  val sourceUrl: String,
)

/**
 * A selected-day event contract for the original Maharashtra calendar.
 *
 * The model preserves date provenance, distinguishes exact civic dates from calculated Panchang
 * cues, and leaves room for separately reviewed devotional-guide and personal-plan integrations.
 */
data class MaharashtraRichCalendarEvent(
  val id: String,
  val title: String,
  val date: LocalDate,
  val category: String,
  val sourceTier: MaharashtraCalendarSourceTier,
  val detail: String,
  val sourceLabel: String,
  val sourceUrl: String? = null,
  val isEstimate: Boolean = false,
  val linkedGuideId: String? = null,
)

/**
 * Original offline Maharashtra calendar facts and calculated selected-day cues.
 *
 * Civic observances are fixed facts only for the bundled 2026 official list. Lunar cues are
 * deliberately calculated only for a selected date and saved place, rather than being hard-coded
 * into a commercial-almanac-style annual table.
 */
object MaharashtraCalendar {
  const val publicHolidaySource = "MMRDA public holidays — 2026"
  const val publicHolidaySourceUrl = "https://mmrda.maharashtra.gov.in/en/public-holidays"
  const val localPanchangSource = "Spiritual Companion offline Panchang calculation"

  val publicHolidays2026: List<MaharashtraCalendarObservance> = listOf(
    holiday("republic-day", "Republic Day", 1, 26),
    holiday("mahashivratri", "Mahashivratri", 2, 15),
    holiday("shivaji-jayanti", "Chhatrapati Shivaji Maharaj Jayanti", 2, 19),
    holiday("holi", "Holi (Second Day)", 3, 3),
    holiday("gudhi-padwa", "Gudhi Padwa", 3, 19),
    holiday("ramzan-id", "Ramzan-Id (Id-Ul-Fitra)", 3, 21),
    holiday("ram-navami", "Ram Navami", 3, 26),
    holiday("mahavir-janmakalyanak", "Mahavir Janmakalyanak", 3, 31),
    holiday("good-friday", "Good Friday", 4, 3),
    holiday("ambedkar-jayanti", "Dr. Babasaheb Ambedkar Jayanti", 4, 14),
    holiday("maharashtra-din", "Maharashtra Din", 5, 1),
    holiday("buddha-pournima", "Buddha Pournima", 5, 1),
    holiday("bakri-id", "Bakri Id (Id-Uz-Zuha)", 5, 28),
    holiday("moharum", "Moharum", 6, 26),
    holiday("independence-day", "Independence Day", 8, 15),
    holiday("parsi-new-year", "Parsi New Year (Shahenshahi)", 8, 15),
    holiday("id-e-milad", "Id-E-Milad", 8, 26),
    holiday("ganesh-chaturthi", "Ganesh Chaturthi", 9, 14),
    holiday("gandhi-jayanti", "Mahatma Gandhi Jayanti", 10, 2),
    holiday("dasara", "Dasara", 10, 20),
    holiday("diwali-amavasya", "Diwali Amavasya (Laxmi Pujan)", 11, 8),
    holiday("diwali-bali-pratipada", "Diwali (Bali Pratipada)", 11, 10),
    holiday("guru-nanak-jayanti", "Guru Nanak Jayanti", 11, 24),
    holiday("christmas", "Christmas", 12, 25),
  )

  fun observancesOn(date: LocalDate): List<MaharashtraCalendarObservance> =
    publicHolidays2026.filter { it.date == date }

  /** Returns a source-labelled, selected-day event feed. No hidden background lookup is performed. */
  fun richEventsOn(date: LocalDate, snapshot: PanchangSnapshot): List<MaharashtraRichCalendarEvent> = buildList {
    observancesOn(date).forEach { observance ->
      add(
        MaharashtraRichCalendarEvent(
          id = observance.id,
          title = observance.name,
          date = observance.date,
          category = observance.category,
          sourceTier = MaharashtraCalendarSourceTier.GOVERNMENT_PUBLISHED,
          detail = "Published civil observance for Maharashtra in the bundled 2026 list.",
          sourceLabel = observance.source,
          sourceUrl = observance.sourceUrl,
        ),
      )
    }
    addAll(panchangRichEvents(date, snapshot))
  }

  fun observanceDatesIn(month: YearMonth): Set<LocalDate> =
    publicHolidays2026.asSequence().filter { YearMonth.from(it.date) == month }.map { it.date }.toSet()

  /** Sunday-first grid familiar to Maharashtra wall calendars, padded to complete weeks. */
  fun monthGrid(month: YearMonth): List<LocalDate?> {
    val firstDate = month.atDay(1)
    val leadingEmptyCells = firstDate.dayOfWeek.value % 7
    val cells = MutableList<LocalDate?>(leadingEmptyCells) { null }
    repeat(month.lengthOfMonth()) { index -> cells += month.atDay(index + 1) }
    while (cells.size % 7 != 0) cells += null
    return cells
  }

  /** Compatibility summary for compact calendar cells; detailed provenance is exposed by [richEventsOn]. */
  fun panchangMarkers(snapshot: PanchangSnapshot): List<String> =
    panchangRichEvents(LocalDate.now(), snapshot).map { it.title }

  private fun panchangRichEvents(date: LocalDate, snapshot: PanchangSnapshot): List<MaharashtraRichCalendarEvent> = buildList {
    fun cue(id: String, title: String, detail: String) {
      add(
        MaharashtraRichCalendarEvent(
          id = "panchang-$id-${date}",
          title = title,
          date = date,
          category = "Personal devotional cue",
          sourceTier = MaharashtraCalendarSourceTier.LOCAL_PANCHANG_ESTIMATE,
          detail = detail,
          sourceLabel = localPanchangSource,
          isEstimate = true,
        ),
      )
    }
    when {
      snapshot.tithi.contains("Ekadashi", ignoreCase = true) -> cue(
        "ekadashi",
        "Ekadashi personal-practice cue",
        "The local Tithi estimate indicates Ekadashi. Consider your chosen Vithoba or reflective practice.",
      )
      snapshot.tithi.equals("Purnima", ignoreCase = true) -> cue(
        "purnima",
        "Purnima personal-practice cue",
        "The local Tithi estimate indicates Purnima. Consider a quiet reading or prayer practice.",
      )
      snapshot.tithi.equals("Amavasya", ignoreCase = true) -> cue(
        "amavasya",
        "Amavasya personal-practice cue",
        "The local Tithi estimate indicates Amavasya. Choose any personal remembrance practice that is meaningful to you.",
      )
      snapshot.tithi.contains("Chaturthi", ignoreCase = true) -> cue(
        "chaturthi",
        "Chaturthi personal-practice cue",
        "The local Tithi estimate indicates Chaturthi. Consider your chosen Ganapati reflection or Aarti.",
      )
    }
    if (snapshot.lunarMonthEstimate.equals("Shravana", ignoreCase = true)) {
      cue(
        "shravana",
        "Shravana devotional season estimate",
        "The local lunar-month estimate is Shravana. Regional observance dates and practices vary; confirm locally when needed.",
      )
    }
  }

  private fun holiday(id: String, name: String, month: Int, day: Int) = MaharashtraCalendarObservance(
    id = id,
    name = name,
    date = LocalDate.of(2026, month, day),
    category = "Maharashtra public holiday",
    source = publicHolidaySource,
    sourceUrl = publicHolidaySourceUrl,
  )
}
