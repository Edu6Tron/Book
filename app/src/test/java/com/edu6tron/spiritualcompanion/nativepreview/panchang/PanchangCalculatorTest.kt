package com.edu6tron.spiritualcompanion.nativepreview.panchang

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
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
  fun expandedRegionalCitiesResolveFromBundledCoordinates() {
    listOf("Shimla, Himachal Pradesh", "Madurai, Tamil Nadu", "Kochi, Kerala", "Patna, Bihar").forEach { place ->
      val snapshot = PanchangCalculator.calculate(LocalDate.of(2026, 8, 15), place, indiaZone)
      assertTrue("Expected $place to be recognised", snapshot.usesRecognisedCity)
      assertEquals(place, snapshot.placeLabel)
      assertNotNull(snapshot.sunrise)
    }
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

  @Test
  fun onlineTimingOverlayReplacesRiseSetAndRecalculatesBrahmaMuhurta() {
    val date = LocalDate.of(2026, 8, 17)
    val offline = PanchangCalculator.calculate(date, "Pune, Maharashtra", indiaZone)
    val refreshed = offline.withOnlineAstronomyTiming(
      OnlineAstronomyTiming(
        date = date,
        sunrise = LocalTime.of(6, 12),
        sunset = LocalTime.of(18, 57),
        moonrise = LocalTime.of(8, 4),
        moonset = LocalTime.of(20, 42),
      ),
    )

    assertEquals(PanchangTimingSource.ONLINE_ASTRONOMICAL_REFERENCE, refreshed.timingSource)
    assertEquals(LocalTime.of(6, 12), refreshed.sunrise)
    assertEquals(LocalTime.of(18, 57), refreshed.sunset)
    assertEquals(LocalTime.of(8, 4), refreshed.moonrise)
    assertEquals(LocalTime.of(20, 42), refreshed.moonset)
    assertEquals(LocalTime.of(4, 36), refreshed.brahmaMuhurtaStart)
    assertEquals(LocalTime.of(5, 24), refreshed.brahmaMuhurtaEnd)
  }

  @Test
  fun onlineTimingCacheIsPlaceBoundAndDoesNotStorePlaceLabel() {
    val puneLocation = PanchangCalculator.onlineTimingLocation("Pune, Maharashtra")
    val mumbaiLocation = PanchangCalculator.onlineTimingLocation("Mumbai, Maharashtra")
    assertNotNull(puneLocation)
    assertNotNull(mumbaiLocation)
    assertFalse(puneLocation!!.cacheKey == mumbaiLocation!!.cacheKey)

    val cache = OnlineAstronomyCache(
      locationCacheKey = puneLocation.cacheKey,
      refreshedAtEpochMillis = 1_786_400_000_000L,
      entries = listOf(
        OnlineAstronomyTiming(LocalDate.of(2026, 8, 17), LocalTime.of(6, 12), LocalTime.of(18, 57), null, null),
      ),
    )
    val encoded = OnlineAstronomyCacheCodec.encode(cache)

    assertFalse(encoded.contains("Pune", ignoreCase = true))
    assertNotNull(OnlineAstronomyCacheCodec.decode(encoded)?.timingFor(LocalDate.of(2026, 8, 17), puneLocation.cacheKey))
    assertNull(OnlineAstronomyCacheCodec.decode(encoded)?.timingFor(LocalDate.of(2026, 8, 17), mumbaiLocation.cacheKey))
    assertEquals(LocalDate.of(2026, 8, 17), OnlineAstronomyCacheCodec.decode(encoded)?.coverageEnd())
  }

  @Test
  fun unsupportedFreeTextCannotTriggerOnlineTimingLookup() {
    assertNull(PanchangCalculator.onlineTimingLocation("My private village"))
  }

  @Test
  fun officialResponseParserReadsRiseSetWithoutNetworkAccess() {
    val timing = UsnoAstronomyClient().parseResponse(
      expectedDate = LocalDate.of(2026, 8, 17),
      response = """
        {"properties":{"data":{"year":2026,"month":8,"day":17,
        "sundata":[{"phen":"Rise","time":"06:12"},{"phen":"Set","time":"18:57"}],
        "moondata":[{"phen":"Rise","time":"08:04"},{"phen":"Set","time":"20:42"}]}}}
      """.trimIndent(),
    )

    assertEquals(LocalTime.of(6, 12), timing.sunrise)
    assertEquals(LocalTime.of(18, 57), timing.sunset)
    assertEquals(LocalTime.of(8, 4), timing.moonrise)
    assertEquals(LocalTime.of(20, 42), timing.moonset)
  }

  @Test
  fun officialResponseParserRejectsUnexpectedDateInsteadOfCachingIt() {
    val mismatchedResponse = """
      {"properties":{"data":{"year":2026,"month":8,"day":18,
      "sundata":[{"phen":"Rise","time":"06:12"},{"phen":"Set","time":"18:57"}],
      "moondata":[]}}}
    """.trimIndent()

    val failure = runCatching {
      UsnoAstronomyClient().parseResponse(LocalDate.of(2026, 8, 17), mismatchedResponse)
    }.exceptionOrNull()

    assertNotNull(failure)
  }
}
