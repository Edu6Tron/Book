package com.edu6tron.spiritualcompanion.nativepreview.media

object LyricTiming {
  /**
   * Maps elapsed local-audio time to an approved displayed lyric line. User-selected
   * recordings can have different arrangements, so proportional progression is a clearly
   * labelled guided-reading fallback, never claimed as a verified recording transcript.
   */
  fun activeVerseIndex(positionMs: Long, durationMs: Long, verseCount: Int): Int {
    if (durationMs <= 0L || verseCount <= 0) return -1
    val boundedPosition = positionMs.coerceIn(0L, durationMs)
    return ((boundedPosition.toDouble() / durationMs.toDouble()) * verseCount)
      .toInt()
      .coerceIn(0, verseCount - 1)
  }

  /** A personal profile has one non-negative local-audio offset per displayed line. */
  fun isValidProfile(offsetsMs: List<Long>, lineCount: Int): Boolean =
    lineCount > 0 &&
      offsetsMs.size == lineCount &&
      offsetsMs.all { it >= 0L } &&
      offsetsMs.zipWithNext().all { (previous, next) -> next >= previous }

  /**
   * Resolves the currently highlighted line. A complete personal profile takes priority;
   * otherwise the caller should disclose that proportional guided pacing is being used.
   */
  fun activeLineIndex(
    positionMs: Long,
    durationMs: Long,
    lineCount: Int,
    personalOffsetsMs: List<Long> = emptyList(),
  ): Int {
    if (lineCount <= 0) return -1
    if (!isValidProfile(personalOffsetsMs, lineCount)) {
      return activeVerseIndex(positionMs, durationMs, lineCount)
    }
    val boundedPosition = positionMs.coerceAtLeast(0L)
    return personalOffsetsMs.indexOfLast { offset -> offset <= boundedPosition }
      .coerceAtLeast(0)
      .coerceAtMost(lineCount - 1)
  }

  fun offsetForLine(personalOffsetsMs: List<Long>, lineIndex: Int, lineCount: Int): Long? =
    personalOffsetsMs.takeIf { isValidProfile(it, lineCount) }?.getOrNull(lineIndex)

  /**
   * Replaces one marker with the current local-player position. Incomplete profiles use a
   * private in-memory -1 sentinel and are never eligible for persistence. A mistaken tap that
   * breaks the known marker order is rejected instead of creating unsafe seek positions.
   */
  fun withOffset(
    currentOffsetsMs: List<Long>,
    lineIndex: Int,
    positionMs: Long,
    lineCount: Int,
  ): List<Long>? {
    if (lineCount <= 0 || lineIndex !in 0 until lineCount || positionMs < 0L) return null
    val working = currentOffsetsMs
      .takeIf { it.size == lineCount && it.all { value -> value >= -1L } }
      ?.toMutableList()
      ?: MutableList(lineCount) { -1L }
    working[lineIndex] = positionMs
    for (index in 1 until working.size) {
      val previous = working[index - 1]
      val next = working[index]
      if (previous >= 0L && next >= 0L && next < previous) return null
    }
    return working
  }
}
