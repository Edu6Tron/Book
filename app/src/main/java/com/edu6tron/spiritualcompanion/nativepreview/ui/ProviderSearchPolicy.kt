package com.edu6tron.spiritualcompanion.nativepreview.ui

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Keeps provider discovery user-initiated and separate from all locally stored alarm media.
 * This class deliberately contains no history, analytics, or background-fetch behaviour.
 */
object ProviderSearchPolicy {
  const val defaultQuery = "devotional Aarti lyrics"

  fun normaliseQuery(query: String): String = query.trim().ifBlank { defaultQuery }

  fun providerSearchUrl(query: String): String {
    val encodedQuery = URLEncoder.encode(normaliseQuery(query), StandardCharsets.UTF_8.toString())
    return "https://www.youtube.com/results?search_query=$encodedQuery"
  }
}
