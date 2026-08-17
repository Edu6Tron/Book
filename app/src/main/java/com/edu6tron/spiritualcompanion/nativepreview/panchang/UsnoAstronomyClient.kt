package com.edu6tron.spiritualcompanion.nativepreview.panchang

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

data class OnlineTimingRefreshResult(
  val locationCacheKey: String,
  val timings: List<OnlineAstronomyTiming>,
  val requestedDays: Int,
)

/**
 * Uses the U.S. Naval Observatory public astronomy endpoint only when the user taps refresh.
 * It sends derived bundled coordinates and a date, never a city name, user text, or device ID.
 */
@Singleton
class UsnoAstronomyClient @Inject constructor() {
  suspend fun refreshNextThirtyOneDays(
    location: PanchangTimingLocation,
    firstDate: LocalDate = LocalDate.now(),
  ): OnlineTimingRefreshResult = withContext(Dispatchers.IO) {
    val requestedDates = (0 until REFRESH_DAYS).map { firstDate.plusDays(it.toLong()) }
    val timings = requestedDates.mapNotNull { date ->
      runCatching { fetchOneDay(location, date) }.getOrNull()
    }
    OnlineTimingRefreshResult(
      locationCacheKey = location.cacheKey,
      timings = timings,
      requestedDays = requestedDates.size,
    )
  }

  internal fun requestUrl(location: PanchangTimingLocation, date: LocalDate): String = Uri.Builder()
    .scheme("https")
    .authority("aa.usno.navy.mil")
    .appendPath("api")
    .appendPath("rstt")
    .appendPath("oneday")
    .appendQueryParameter("date", date.toString())
    .appendQueryParameter("coords", "${location.latitude},${location.longitude}")
    .appendQueryParameter("tz", "5.5")
    .build()
    .toString()

  internal fun parseResponse(expectedDate: LocalDate, response: String): OnlineAstronomyTiming {
    val data = JSONObject(response).getJSONObject("properties").getJSONObject("data")
    val responseDate = LocalDate.of(data.getInt("year"), data.getInt("month"), data.getInt("day"))
    if (responseDate != expectedDate) throw IOException("timing_source_date_mismatch")
    val sunData = data.getJSONArray("sundata")
    val moonData = data.getJSONArray("moondata")
    val sunrise = eventTime(sunData, "Rise")
    val sunset = eventTime(sunData, "Set")
    if (sunrise == null || sunset == null) throw IOException("timing_source_solar_events_unavailable")
    return OnlineAstronomyTiming(
      date = responseDate,
      sunrise = sunrise,
      sunset = sunset,
      moonrise = eventTime(moonData, "Rise"),
      moonset = eventTime(moonData, "Set"),
    )
  }

  private fun fetchOneDay(location: PanchangTimingLocation, date: LocalDate): OnlineAstronomyTiming {
    val connection = (URL(requestUrl(location, date)).openConnection() as HttpURLConnection).apply {
      requestMethod = "GET"
      connectTimeout = NETWORK_TIMEOUT_MILLIS
      readTimeout = NETWORK_TIMEOUT_MILLIS
      instanceFollowRedirects = false
      setRequestProperty("Accept", "application/json")
    }
    return try {
      if (connection.responseCode !in 200..299) throw IOException("timing_source_unavailable")
      connection.inputStream.bufferedReader().use { reader -> parseResponse(date, reader.readText()) }
    } finally {
      connection.disconnect()
    }
  }

  private fun eventTime(events: org.json.JSONArray, phenomenon: String): LocalTime? {
    for (index in 0 until events.length()) {
      val event = events.optJSONObject(index) ?: continue
      if (event.optString("phen") == phenomenon) {
        return runCatching { LocalTime.parse(event.getString("time")) }.getOrNull()
      }
    }
    return null
  }

  private companion object {
    const val REFRESH_DAYS = 31
    const val NETWORK_TIMEOUT_MILLIS = 8_000
  }
}
