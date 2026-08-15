package com.edu6tron.spiritualcompanion.nativepreview.panchang

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class PanchangCalculatorTest {
  private val indiaZone = ZoneId.of("Asia/Kolkata")

  @Test
  fun recognisedCityProvidesLocalAstronomicalContext() {
    val snapshot = PanchangCalculator.calculate(LocalDate.of(2026, 8, 15), "Pune, Maharashtra", indiaZone)

    assertTrue(snapshot.usesRecognisedCity)
    assertEquals("Pune, Maharashtra", snapshot.placeLabel)
    assertNotNull(snapshot.sunrise)
    assertNotNull(snapshot.sunset)
    assertNotNull(snapshot.brahmaMuhurtaStart)
    assertNotNull(snapshot.brahmaMuhurtaEnd)
    assertTrue(snapshot.brahmaMuhurtaStart!!.isBefore(snapshot.brahmaMuhurtaEnd))
    assertTrue(snapshot.brahmaMuhurtaEnd!!.isBefore(snapshot.sunrise))
    assertTrue(snapshot.nakshatra.isNotBlank())
    assertTrue(snapshot.tithi.isNotBlank())
  }

  @Test
  fun unsupportedPlaceFallsBackWithoutGpsOrNetworkAssumptions() {
    val snapshot = PanchangCalculator.calculate(LocalDate.of(2026, 8, 15), "My village", indiaZone)

    assertFalse(snapshot.usesRecognisedCity)
    assertTrue(snapshot.placeLabel.contains("My village"))
    assertTrue(snapshot.sakaDate.contains("Saka"))
  }

  @Test
  fun sakaCalendarSwitchesOnTheIndianNationalCalendarNewYear() {
    val before = PanchangCalculator.calculate(LocalDate.of(2026, 3, 21), "Delhi", indiaZone)
    val after = PanchangCalculator.calculate(LocalDate.of(2026, 3, 22), "Delhi", indiaZone)

    assertTrue(before.sakaDate.contains("1947 Saka"))
    assertTrue(after.sakaDate.contains("1948 Saka"))
  }
}
