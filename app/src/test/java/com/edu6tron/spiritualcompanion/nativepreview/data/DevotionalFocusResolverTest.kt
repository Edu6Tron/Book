package com.edu6tron.spiritualcompanion.nativepreview.data

import com.edu6tron.spiritualcompanion.nativepreview.panchang.PanchangSnapshot
import java.time.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Test

class DevotionalFocusResolverTest {
  @Test
  fun `uses the active Brahma Muhurta window as the immediate focus`() {
    val focus = DevotionalFocusResolver.resolve(
      snapshot = snapshot(),
      now = LocalTime.of(4, 45),
      brahmaMuhurtaEnabled = true,
      eveningRoutineEnabled = true,
    )

    assertEquals("NOW", focus.label)
    assertEquals("Brahma Muhurta", focus.title)
  }

  @Test
  fun `shows evening prarthana as the next enabled focus before sunset`() {
    val focus = DevotionalFocusResolver.resolve(
      snapshot = snapshot(),
      now = LocalTime.of(17, 10),
      brahmaMuhurtaEnabled = false,
      eveningRoutineEnabled = true,
    )

    assertEquals("NEXT", focus.label)
    assertEquals("Evening Prarthana · 6:00 PM", focus.title)
  }

  @Test
  fun `keeps the focus personal when no routine is enabled`() {
    val focus = DevotionalFocusResolver.resolve(
      snapshot = snapshot(),
      now = LocalTime.NOON,
      brahmaMuhurtaEnabled = false,
      eveningRoutineEnabled = false,
    )

    assertEquals("PERSONAL PLAN", focus.label)
  }

  private fun snapshot() = PanchangSnapshot(
    placeLabel = "India reference",
    usesRecognisedCity = false,
    sunrise = LocalTime.of(6, 0),
    sunset = LocalTime.of(18, 30),
    moonrise = LocalTime.of(19, 0),
    moonset = LocalTime.of(5, 30),
    brahmaMuhurtaStart = LocalTime.of(4, 24),
    brahmaMuhurtaEnd = LocalTime.of(5, 12),
    paksha = "Shukla Paksha",
    tithi = "Ekadashi",
    nakshatra = "Rohini",
    lunarMonthEstimate = "Shravana",
    sakaDate = "25 Shravana 1948 Saka",
  )
}
