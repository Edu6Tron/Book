package com.edu6tron.spiritualcompanion.nativepreview.data

import com.edu6tron.spiritualcompanion.nativepreview.panchang.PanchangSnapshot
import java.time.LocalDate
import java.time.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DevotionalRoutineTest {
  @Test
  fun `evening prarthana preserves the requested local sequence`() {
    val routine = NativeDevotionalRoutines.eveningPrarthana

    assertEquals(DevotionalRoutineAnchor.SUNSET, routine.anchor)
    assertEquals(
      listOf("shubham-karoti", "vakratunda", "sukhkarta", "shirdi"),
      routine.steps.map { it.id },
    )
    assertEquals("sukhkarta-dukhharta", routine.steps[2].aartiId)
    assertEquals("shirdi-sai-aarti", routine.steps[3].aartiId)
    assertEquals(
      listOf("Shubham Karoti Kalyanam", "Aarogyam Dhanasampada"),
      routine.steps.first().recitationLines.take(2),
    )
  }

  @Test
  fun `brahma muhurta routine uses a gentle preparation anchor`() {
    val routine = NativeDevotionalRoutines.brahmaMuhurta

    assertEquals(DevotionalRoutineAnchor.BRAHMA_MUHURTA, routine.anchor)
    assertEquals("wake", routine.steps.first().id)
    assertTrue(routine.timingNote.contains("96 minutes", ignoreCase = true))
  }

  @Test
  fun `ekadashi guidance is transparent and points to local catalogue items`() {
    val guidance = NativeDevotionalRoutines.specialDayGuidance(
      snapshot(tithi = "Ekadashi"),
    )

    assertNotNull(guidance)
    assertEquals("Ekadashi suggestion", guidance?.title)
    assertEquals(listOf("vitthal-aarti", "govind-bolo"), guidance?.suggestedAartiIds)
    assertTrue(guidance?.detail?.contains("offline estimate", ignoreCase = true) == true)
  }

  @Test
  fun `purnima guidance remains an optional offline suggestion`() {
    val guidance = NativeDevotionalRoutines.specialDayGuidance(snapshot(tithi = "Purnima"))

    assertNotNull(guidance)
    assertEquals("Purnima suggestion", guidance?.title)
    assertEquals(listOf("om-jai-jagdish-hare", "om-jai-lakshmi-mata"), guidance?.suggestedAartiIds)
    assertTrue(guidance?.detail?.contains("own tradition", ignoreCase = true) == true)
  }

  @Test
  fun `daily routine progress is stable today and naturally resets for a new day`() {
    val today = LocalDate.of(2026, 8, 16)
    val progress = RoutineDailyProgress(today, setOf("vakratunda", "shubham-karoti"))

    val restored = RoutineDailyProgress.fromStored(progress.toStoredValue())

    assertEquals(setOf("vakratunda", "shubham-karoti"), restored.completedStepsFor(today))
    assertEquals(emptySet<String>(), restored.completedStepsFor(today.plusDays(1)))
    assertEquals("2026-08-16|shubham-karoti,vakratunda", progress.toStoredValue())
  }

  @Test
  fun `invalid routine progress is ignored safely`() {
    val restored = RoutineDailyProgress.fromStored("not-a-date|wake")

    assertEquals(emptySet<String>(), restored.completedStepsFor(LocalDate.of(2026, 8, 16)))
  }

  private fun snapshot(tithi: String) = PanchangSnapshot(
    placeLabel = "India reference",
    usesRecognisedCity = false,
    sunrise = LocalTime.of(6, 0),
    sunset = LocalTime.of(18, 30),
    moonrise = LocalTime.of(19, 0),
    moonset = LocalTime.of(5, 30),
    brahmaMuhurtaStart = LocalTime.of(4, 24),
    brahmaMuhurtaEnd = LocalTime.of(5, 12),
    tithi = tithi,
    nakshatra = "Rohini",
    paksha = "Shukla Paksha",
    lunarMonthEstimate = "Shravana",
    sakaDate = "25 Shravana 1948 Saka",
  )
}
