package com.edu6tron.spiritualcompanion.nativepreview.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NativeScriptureReflectionTest {
  @Test
  fun cycleIsDeterministicAndContainsUsableOfflineContent() {
    assertEquals(NativeScriptureReflection.forDayOfYear(1), NativeScriptureReflection.forDayOfYear(8))
    assertEquals(NativeScriptureReflection.forDayOfYear(0), NativeScriptureReflection.forDayOfYear(7))
    (1..7).map(NativeScriptureReflection::forDayOfYear).forEach { entry ->
      assertTrue(entry.theme.isNotBlank())
      assertTrue(entry.reflection.isNotBlank())
      assertTrue(entry.source.isNotBlank())
      assertTrue(entry.smallAction.isNotBlank())
    }
  }
}
