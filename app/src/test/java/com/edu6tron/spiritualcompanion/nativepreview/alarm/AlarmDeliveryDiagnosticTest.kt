package com.edu6tron.spiritualcompanion.nativepreview.alarm

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AlarmDeliveryDiagnosticTest {
  @Test
  fun decodesOnlyKnownStableStageTokens() {
    AlarmDeliveryStage.entries.forEach { stage ->
      assertEquals(stage, AlarmDeliveryStage.fromStoredValue(stage.storedValue))
    }

    assertNull(AlarmDeliveryStage.fromStoredValue("unknown"))
    assertNull(AlarmDeliveryStage.fromStoredValue(""))
    assertNull(AlarmDeliveryStage.fromStoredValue(null))
  }

  @Test
  fun stageTokensCannotContainPersonalAlarmOrMediaData() {
    val renderedStages = AlarmDeliveryStage.entries.joinToString(" ") {
      "${it.storedValue} ${it.userFacingSummary}"
    }.lowercase()

    assertTrue(AlarmDeliveryStage.entries.all { it.storedValue.matches(Regex("[a-z_]+")) })
    assertFalse(renderedStages.contains("content://"))
    assertFalse(renderedStages.contains("file://"))
    assertFalse(renderedStages.contains("youtube"))
    assertFalse(renderedStages.contains("location"))
    assertFalse(renderedStages.contains("account"))
    assertFalse(renderedStages.contains("alarm id"))
    assertFalse(renderedStages.contains("exception"))
  }

  @Test
  fun playbackStagesDistinguishLocalAndBundledOfflineSuccess() {
    assertEquals(
      "local_tone_started",
      AlarmDeliveryStage.LOCAL_TONE_STARTED.storedValue,
    )
    assertEquals(
      "fallback_tone_started",
      AlarmDeliveryStage.FALLBACK_TONE_STARTED.storedValue,
    )
    assertEquals(
      "playback_failed",
      AlarmDeliveryStage.PLAYBACK_FAILED.storedValue,
    )
  }
}
