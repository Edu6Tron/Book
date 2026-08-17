package com.edu6tron.spiritualcompanion.nativepreview.media

/**
 * Compact local-only codec for personal line offsets. It deliberately stores only stable
 * catalogue identifiers and millisecond values; it never stores a media URI, file name, lyric
 * text, location, or user-entered search term.
 */
object PersonalLyricTimingCodec {
  fun encode(profiles: Map<String, List<Long>>): String? = profiles
    .filter { (aartiId, offsets) ->
      aartiId.matches(Regex("[a-z0-9-]+")) && offsets.isNotEmpty() && offsets.all { it >= 0L }
    }
    .toSortedMap()
    .entries
    .joinToString(separator = ";") { (aartiId, offsets) -> "$aartiId=${offsets.joinToString(separator = ",")}" }
    .takeIf { it.isNotBlank() }

  fun decode(stored: String?): Map<String, List<Long>> = stored
    ?.split(';')
    ?.mapNotNull { entry ->
      val separator = entry.indexOf('=')
      if (separator <= 0) return@mapNotNull null
      val aartiId = entry.substring(0, separator)
      val offsets = entry.substring(separator + 1)
        .split(',')
        .mapNotNull { value -> value.toLongOrNull()?.takeIf { it >= 0L } }
      if (!aartiId.matches(Regex("[a-z0-9-]+")) || offsets.isEmpty()) null else aartiId to offsets
    }
    ?.toMap()
    .orEmpty()
}
