package com.edu6tron.spiritualcompanion.nativepreview.data

import com.edu6tron.spiritualcompanion.nativepreview.panchang.PanchangSnapshot
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
    val markers = MaharashtraCalendar.panchangMarkers(snapshot(tithi = "Shukla Ekadashi", lunarMonth = "Shravana"))

    assertTrue(markers.contains("Ekadashi personal-practice cue"))
    assertTrue(markers.contains("Shravana devotional season estimate"))
  }

  @Test
  fun `rich events identify published Maharashtra government observances`() {
    val events = MaharashtraCalendar.richEventsOn(LocalDate.of(2026, 5, 1), snapshot())
    val governmentEvents = events.filter { it.sourceTier == MaharashtraCalendarSourceTier.GOVERNMENT_PUBLISHED }

    assertEquals(listOf("Maharashtra Din", "Buddha Pournima"), governmentEvents.map { it.title })
    assertTrue(governmentEvents.all { !it.isEstimate })
    assertTrue(governmentEvents.all { it.sourceLabel.isNotBlank() && it.sourceUrl != null })
  }

  @Test
  fun `rich events expose Ekadashi Purnima and Chaturthi as local estimates`() {
    listOf("Shukla Ekadashi", "Purnima", "Krishna Chaturthi").forEach { tithi ->
      val events = MaharashtraCalendar.richEventsOn(LocalDate.of(2026, 7, 12), snapshot(tithi = tithi))
      val calculatedEvents = events.filter { it.sourceTier == MaharashtraCalendarSourceTier.LOCAL_PANCHANG_ESTIMATE }

      assertTrue("Expected a local Panchang cue for $tithi", calculatedEvents.isNotEmpty())
      assertTrue(calculatedEvents.all { it.isEstimate })
      assertTrue(calculatedEvents.all { it.sourceLabel == MaharashtraCalendar.localPanchangSource })
    }
  }

  @Test
  fun `source tiers always provide a user-facing label and disclosure`() {
    MaharashtraCalendarSourceTier.entries.forEach { tier ->
      assertTrue(tier.label.isNotBlank())
      assertTrue(tier.disclosure.isNotBlank())
    }
  }

  @Test
  fun `government events are facts while Panchang cues are clearly estimates`() {
    val governmentEvent = MaharashtraCalendar.richEventsOn(LocalDate.of(2026, 1, 26), snapshot()).single()
    val calculatedEvent = MaharashtraCalendar.richEventsOn(
      LocalDate.of(2026, 7, 12),
      snapshot(tithi = "Purnima"),
    ).single { it.sourceTier == MaharashtraCalendarSourceTier.LOCAL_PANCHANG_ESTIMATE }

    assertFalse(governmentEvent.isEstimate)
    assertTrue(calculatedEvent.isEstimate)
  }

  private fun snapshot(tithi: String = "Dashami", lunarMonth: String = "Ashadha") = PanchangSnapshot(
      placeLabel = "Pune",
      usesRecognisedCity = true,
      sunrise = LocalTime.of(6, 30),
      sunset = LocalTime.of(18, 30),
      moonrise = null,
      moonset = null,
      brahmaMuhurtaStart = LocalTime.of(4, 54),
      brahmaMuhurtaEnd = LocalTime.of(5, 42),
      paksha = "Shukla Paksha",
      tithi = tithi,
      nakshatra = "Rohini",
      lunarMonthEstimate = lunarMonth,
      sakaDate = "Saka 1948",
    )
}
