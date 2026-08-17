package com.edu6tron.spiritualcompanion.nativepreview.data

import com.edu6tron.spiritualcompanion.nativepreview.panchang.PanchangSnapshot
import java.time.LocalDate
import java.time.YearMonth

data class MaharashtraCalendarObservance(
  val id: String,
  val name: String,
  val date: LocalDate,
  val category: String,
  val source: String,
  val sourceUrl: String,
)

/**
 * Bundled, source-labelled calendar facts for the Maharashtra public-holiday list.
 *
 * These dates are deliberately separate from the offline Panchang estimate. They are exact only
 * for the published 2026 government list; lunar dates remain clearly labelled as estimates.
 */
object MaharashtraCalendar {
  const val publicHolidaySource = "MMRDA public holidays — 2026"
  const val publicHolidaySourceUrl = "https://mmrda.maharashtra.gov.in/en/public-holidays"

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

  fun panchangMarkers(snapshot: PanchangSnapshot): List<String> = buildList {
    when {
      snapshot.tithi.contains("Ekadashi", ignoreCase = true) -> add("Ekadashi personal-practice cue")
      snapshot.tithi.equals("Purnima", ignoreCase = true) -> add("Purnima personal-practice cue")
      snapshot.tithi.equals("Amavasya", ignoreCase = true) -> add("Amavasya personal-practice cue")
    }
    if (snapshot.lunarMonthEstimate == "Shravana" && snapshot.tithi.contains("Shukla", ignoreCase = true)) {
      add("Shravana season estimate")
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
