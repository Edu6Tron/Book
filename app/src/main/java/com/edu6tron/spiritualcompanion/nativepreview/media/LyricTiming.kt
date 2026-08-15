package com.edu6tron.spiritualcompanion.nativepreview.media

/**
 * Maps elapsed local-audio time proportionally to a lyric verse. This is intentionally
 * approximate because user-selected recordings may use different arrangements.
 */
object LyricTiming {
  fun activeVerseIndex(positionMs: Long, durationMs: Long, verseCount: Int): Int {
    if (durationMs <= 0L || verseCount <= 0) return -1
    val boundedPosition = positionMs.coerceIn(0L, durationMs)
    return ((boundedPosition.toDouble() / durationMs.toDouble()) * verseCount)
      .toInt()
      .coerceIn(0, verseCount - 1)
  }
}
