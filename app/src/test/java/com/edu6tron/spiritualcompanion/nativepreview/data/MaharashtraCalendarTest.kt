package com.edu6tron.spiritualcompanion.nativepreview.data

import com.edu6tron.spiritualcompanion.nativepreview.panchang.PanchangSnapshot
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MaharashtraCalendarTest {
  @Test
  fun `bundled Maharashtra public holiday list preserves all official 2026 entries`() {
    assertEquals(24, MaharashtraCalendar.publicHolidays2026.size)
    assertEquals(
      listOf("Maharashtra Din", "Buddha Pournima"),
      MaharashtraCalendar.observancesOn(LocalDate.of(2026, 5, 1)).map { it.name },
    )
    assertEquals(
      setOf(LocalDate.of(2026, 9, 14)),
      MaharashtraCalendar.observanceDatesIn(YearMonth.of(2026, 9)),
    )
  }

  @Test
  fun `month grid is Sunday first and contains complete weeks`() {
    val march2026 = MaharashtraCalendar.monthGrid(YearMonth.of(2026, 3))
    assertEquals(35, march2026.size)
    assertEquals(LocalDate.of(2026, 3, 1), march2026.first())
    assertEquals(LocalDate.of(2026, 3, 31), march2026.filterNotNull().last())
  }

  @Test
  fun `panchang markers retain personal guidance rather than ritual authority`() {
    val snapshot = PanchangSnapshot(
      placeLabel = "Pune",
      usesRecognisedCity = true,
      sunrise = LocalTime.of(6, 30),
      sunset = LocalTime.of(18, 30),
      moonrise = null,
      moonset = null,
      brahmaMuhurtaStart = LocalTime.of(4, 54),
      brahmaMuhurtaEnd = LocalTime.of(5, 42),
      paksha = "Shukla Paksha",
      tithi = "Shukla Ekadashi",
      nakshatra = "Rohini",
      lunarMonthEstimate = "Shravana",
      sakaDate = "Saka 1948",
    )

    val markers = MaharashtraCalendar.panchangMarkers(snapshot)

    assertTrue(markers.contains("Ekadashi personal-practice cue"))
    assertTrue(markers.contains("Shravana season estimate"))
  }
}
