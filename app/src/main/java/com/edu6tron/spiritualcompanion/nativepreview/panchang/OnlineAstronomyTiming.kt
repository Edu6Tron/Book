package com.edu6tron.spiritualcompanion.nativepreview.panchang

import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalTime

/**
 * A cached astronomical rise/set response fetched only after a user explicitly requests it.
 * The cache identifier is derived from a bundled place rather than storing the user's text input.
 */
data class OnlineAstronomyTiming(
  val date: LocalDate,
  val sunrise: LocalTime?,
  val sunset: LocalTime?,
  val moonrise: LocalTime?,
  val moonset: LocalTime?,
)

data class OnlineAstronomyCache(
  val locationCacheKey: String,
  val refreshedAtEpochMillis: Long,
  val entries: List<OnlineAstronomyTiming>,
) {
  fun timingFor(date: LocalDate, expectedLocationCacheKey: String?): OnlineAstronomyTiming? =
    entries.firstOrNull { it.date == date && expectedLocationCacheKey == locationCacheKey }

  fun coverageEnd(): LocalDate? = entries.maxOfOrNull { it.date }
}

enum class PanchangTimingSource {
  OFFLINE_ESTIMATE,
  ONLINE_ASTRONOMICAL_REFERENCE,
}

fun PanchangSnapshot.withOnlineAstronomyTiming(timing: OnlineAstronomyTiming?): PanchangSnapshot {
  if (timing == null) return this
  val effectiveSunrise = timing.sunrise ?: sunrise
  return copy(
    sunrise = effectiveSunrise,
    sunset = timing.sunset ?: sunset,
    moonrise = timing.moonrise ?: moonrise,
    moonset = timing.moonset ?: moonset,
    brahmaMuhurtaStart = effectiveSunrise?.minusMinutes(96),
    brahmaMuhurtaEnd = effectiveSunrise?.minusMinutes(48),
    timingSource = PanchangTimingSource.ONLINE_ASTRONOMICAL_REFERENCE,
  )
}

/** Serialises timing data only. It intentionally stores no city label, raw user input, or request URL. */
object OnlineAstronomyCacheCodec {
  private const val version = 1

  fun encode(cache: OnlineAstronomyCache): String = JSONObject().apply {
    put("version", version)
    put("locationCacheKey", cache.locationCacheKey)
    put("refreshedAtEpochMillis", cache.refreshedAtEpochMillis)
    put("entries", JSONArray().apply {
      cache.entries.sortedBy { it.date }.forEach { timing ->
        put(JSONObject().apply {
          put("date", timing.date.toString())
          put("sunrise", timing.sunrise?.toString())
          put("sunset", timing.sunset?.toString())
          put("moonrise", timing.moonrise?.toString())
          put("moonset", timing.moonset?.toString())
        })
      }
    })
  }.toString()

  fun decode(raw: String?): OnlineAstronomyCache? = runCatching {
    val objectValue = JSONObject(raw ?: return null)
    if (objectValue.optInt("version") != version) return null
    val cacheKey = objectValue.optString("locationCacheKey").takeIf { it.isNotBlank() } ?: return null
    val refreshedAt = objectValue.optLong("refreshedAtEpochMillis", 0L).takeIf { it > 0L } ?: return null
    val entries = buildList {
      val array = objectValue.optJSONArray("entries") ?: return@buildList
      for (index in 0 until array.length()) {
        val entry = array.optJSONObject(index) ?: continue
        val date = entry.optString("date").toLocalDateOrNull() ?: continue
        add(
          OnlineAstronomyTiming(
            date = date,
            sunrise = entry.optString("sunrise").toLocalTimeOrNull(),
            sunset = entry.optString("sunset").toLocalTimeOrNull(),
            moonrise = entry.optString("moonrise").toLocalTimeOrNull(),
            moonset = entry.optString("moonset").toLocalTimeOrNull(),
          ),
        )
      }
    }
    if (entries.isEmpty()) null else OnlineAstronomyCache(cacheKey, refreshedAt, entries)
  }.getOrNull()

  private fun String.toLocalDateOrNull(): LocalDate? = runCatching { LocalDate.parse(this) }.getOrNull()
  private fun String.toLocalTimeOrNull(): LocalTime? = runCatching { LocalTime.parse(this) }.getOrNull()
}
