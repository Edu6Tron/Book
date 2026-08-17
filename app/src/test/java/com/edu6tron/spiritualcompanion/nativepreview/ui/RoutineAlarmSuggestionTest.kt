package com.edu6tron.spiritualcompanion.nativepreview.ui

import com.edu6tron.spiritualcompanion.nativepreview.data.DevotionalRoutineAnchor
import java.time.LocalDate
import java.time.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RoutineAlarmSuggestionTest {
  @Test
  fun `brahma muhurta context prefills the offline estimate and matching alarm label`() {
    val suggestion = RoutineAlarmSuggestion.from(
      context = RoutineAlarmContext.from(DevotionalRoutineAnchor.BRAHMA_MUHURTA),
      estimatedTime = LocalTime.of(4, 18),
      referenceDate = LocalDate.of(2026, 8, 17),
    )

    requireNotNull(suggestion)
    assertEquals(AlarmTimeSelection(4, 18), suggestion.time)
    assertEquals("Brahma Muhurta", suggestion.alarmLabel)
    assertEquals("Brahma Muhurta begins", suggestion.context.anchorDescription)
  }

  @Test
  fun `sunset context prefills the offline estimate and evening reminder label`() {
    val suggestion = RoutineAlarmSuggestion.from(
      context = RoutineAlarmContext.from(DevotionalRoutineAnchor.SUNSET),
      estimatedTime = LocalTime.of(18, 52),
      referenceDate = LocalDate.of(2026, 8, 17),
    )

    requireNotNull(suggestion)
    assertEquals(AlarmTimeSelection(18, 52), suggestion.time)
    assertEquals("Evening Prarthana", suggestion.alarmLabel)
    assertEquals("Sunset estimate", suggestion.context.anchorDescription)
  }

  @Test
  fun `missing offline estimate never creates an alarm prefill`() {
    assertNull(
      RoutineAlarmSuggestion.from(
        context = RoutineAlarmContext.BRAHMA_MUHURTA,
        estimatedTime = null,
        referenceDate = LocalDate.of(2026, 8, 17),
      ),
    )
  }
}
