package com.edu6tron.spiritualcompanion.nativepreview.panchang

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.Year
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

/**
 * Offline astronomical estimate for the dashboard. It uses a user-entered city only; it never
 * requests GPS or calls a remote service. Values are suitable for daily guidance and should be
 * checked against a local published Panchang for a ritual requiring official local observance.
 */
data class PanchangSnapshot(
  val placeLabel: String,
  val usesRecognisedCity: Boolean,
  val sunrise: LocalTime?,
  val sunset: LocalTime?,
  val moonrise: LocalTime?,
  val moonset: LocalTime?,
  val brahmaMuhurtaStart: LocalTime?,
  val brahmaMuhurtaEnd: LocalTime?,
  val paksha: String,
  val tithi: String,
  val nakshatra: String,
  val lunarMonthEstimate: String,
  val sakaDate: String,
  val timingSource: PanchangTimingSource = PanchangTimingSource.OFFLINE_ESTIMATE,
)

data class PanchangTimingLocation(
  val cacheKey: String,
  val latitude: Double,
  val longitude: Double,
)

private data class GeoLocation(
  val label: String,
  val latitude: Double,
  val longitude: Double,
  val aliases: Set<String>,
)

private data class LunarPosition(val longitude: Double, val latitude: Double)

object PanchangCalculator {
  private const val DEG = PI / 180.0
  private const val SYNODIC_MONTH = 29.530588853
  private const val NEW_MOON_EPOCH = 2451550.09765
  private const val MOON_ALTITUDE_THRESHOLD = -0.3

  private val indiaReference = GeoLocation(
    label = "India reference (set your city)",
    latitude = 28.6139,
    longitude = 77.2090,
    aliases = emptySet(),
  )

  private val cities = listOf(
    GeoLocation("Pune, Maharashtra", 18.5204, 73.8567, setOf("pune", "maharashtra")),
    GeoLocation("Mumbai, Maharashtra", 19.0760, 72.8777, setOf("mumbai", "bombay")),
    GeoLocation("Nashik, Maharashtra", 19.9975, 73.7898, setOf("nashik", "nasik")),
    GeoLocation("Varanasi, Uttar Pradesh", 25.3176, 82.9739, setOf("varanasi", "kashi", "uttar pradesh", "up")),
    GeoLocation("Ayodhya, Uttar Pradesh", 26.7922, 82.1998, setOf("ayodhya")),
    GeoLocation("Tirupati, Andhra Pradesh", 13.6288, 79.4192, setOf("tirupati", "andhra", "andhra pradesh")),
    GeoLocation("Bhubaneswar, Odisha", 20.2961, 85.8245, setOf("bhubaneswar", "odisha", "orissa")),
    GeoLocation("Puri, Odisha", 19.8135, 85.8312, setOf("puri", "jagannath")),
    GeoLocation("New Delhi", 28.6139, 77.2090, setOf("delhi", "new delhi")),
    GeoLocation("Bengaluru, Karnataka", 12.9716, 77.5946, setOf("bengaluru", "bangalore", "karnataka")),
    GeoLocation("Hyderabad, Telangana", 17.3850, 78.4867, setOf("hyderabad", "telangana")),
    GeoLocation("Chennai, Tamil Nadu", 13.0827, 80.2707, setOf("chennai", "tamil nadu", "madras")),
    GeoLocation("Madurai, Tamil Nadu", 9.9252, 78.1198, setOf("madurai")),
    GeoLocation("Kolkata, West Bengal", 22.5726, 88.3639, setOf("kolkata", "calcutta", "west bengal")),
    GeoLocation("Ahmedabad, Gujarat", 23.0225, 72.5714, setOf("ahmedabad", "gujarat")),
    GeoLocation("Shimla, Himachal Pradesh", 31.1048, 77.1734, setOf("shimla", "himachal", "himachal pradesh")),
    GeoLocation("Kochi, Kerala", 9.9312, 76.2673, setOf("kochi", "kerala", "cochin")),
    GeoLocation("Patna, Bihar", 25.5941, 85.1376, setOf("patna", "bihar")),
  )

  private val tithiNames = listOf(
    "Pratipada", "Dwitiya", "Tritiya", "Chaturthi", "Panchami", "Shashthi", "Saptami",
    "Ashtami", "Navami", "Dashami", "Ekadashi", "Dwadashi", "Trayodashi", "Chaturdashi", "Purnima",
  )
  private val nakshatraNames = listOf(
    "Ashwini", "Bharani", "Krittika", "Rohini", "Mrigashirsha", "Ardra", "Punarvasu",
    "Pushya", "Ashlesha", "Magha", "Purva Phalguni", "Uttara Phalguni", "Hasta", "Chitra",
    "Swati", "Vishakha", "Anuradha", "Jyeshtha", "Mula", "Purva Ashadha", "Uttara Ashadha",
    "Shravana", "Dhanishtha", "Shatabhisha", "Purva Bhadrapada", "Uttara Bhadrapada", "Revati",
  )
  private val lunarMonthNames = listOf(
    "Chaitra", "Vaishakha", "Jyeshtha", "Ashadha", "Shravana", "Bhadrapada",
    "Ashwin", "Kartika", "Margashirsha", "Pausha", "Magha", "Phalguna",
  )
  private val sakaMonthNames = listOf(
    "Chaitra", "Vaisakha", "Jyeshtha", "Ashadha", "Shravana", "Bhadra",
    "Ashwin", "Kartika", "Agrahayana", "Pausha", "Magha", "Phalguna",
  )

  fun calculate(
    date: LocalDate,
    cityOrState: String?,
    zoneId: ZoneId = ZoneId.systemDefault(),
  ): PanchangSnapshot {
    val resolution = resolveLocation(cityOrState)
    val location = resolution.first
    val sunrise = solarEvent(date, location, zoneId, rising = true)
    val sunset = solarEvent(date, location, zoneId, rising = false)
    val (moonrise, moonset) = moonEvents(date, location, zoneId)
    val instant = date.atTime(12, 0).atZone(zoneId).toInstant()
    val jd = julianDay(instant)
    val sunLongitude = solarLongitude(jd)
    val moon = lunarPosition(jd)
    val elongation = normalize(moon.longitude - sunLongitude)
    val tithiNumber = floor(elongation / 12.0).toInt().coerceIn(0, 29) + 1
    val paksha = if (tithiNumber <= 15) "Shukla Paksha" else "Krishna Paksha"
    val tithi = when (tithiNumber) {
      15 -> "Purnima"
      30 -> "Amavasya"
      else -> "${if (tithiNumber <= 15) "Shukla" else "Krishna"} ${tithiNames[(tithiNumber - 1) % 15]}"
    }
    val nakshatra = nakshatraNames[floor(normalize(moon.longitude) / (360.0 / 27.0)).toInt().coerceIn(0, 26)]
    val lunation = floor((jd - NEW_MOON_EPOCH) / SYNODIC_MONTH).toLong()
    val lunarMonthEstimate = lunarMonthNames[((lunation % 12 + 12) % 12).toInt()]
    val brahmaEnd = sunrise?.minusMinutes(48)
    val brahmaStart = sunrise?.minusMinutes(96)
    return PanchangSnapshot(
      placeLabel = resolution.second,
      usesRecognisedCity = resolution.third,
      sunrise = sunrise,
      sunset = sunset,
      moonrise = moonrise,
      moonset = moonset,
      brahmaMuhurtaStart = brahmaStart,
      brahmaMuhurtaEnd = brahmaEnd,
      paksha = paksha,
      tithi = tithi,
      nakshatra = nakshatra,
      lunarMonthEstimate = lunarMonthEstimate,
      sakaDate = sakaDate(date),
    )
  }

  /** Cities with bundled coordinates for offline Panchang estimates. */
  fun supportedPlaceLabels(): List<String> = cities.map { it.label }

  /**
   * Provides only bundled coordinates for an explicitly selected supported place. Unrecognised
   * free-text locations never trigger online timing lookups.
   */
  fun onlineTimingLocation(cityOrState: String?): PanchangTimingLocation? {
    val resolution = resolveLocation(cityOrState)
    if (!resolution.third) return null
    val location = resolution.first
    return PanchangTimingLocation(
      cacheKey = "place-${location.label.lowercase().hashCode().toUInt().toString(16)}",
      latitude = location.latitude,
      longitude = location.longitude,
    )
  }

  private fun resolveLocation(input: String?): Triple<GeoLocation, String, Boolean> {
    val normalized = input.orEmpty().lowercase().trim()
    if (normalized.isBlank()) return Triple(indiaReference, indiaReference.label, false)
    val match = cities.firstOrNull { city -> normalized == city.label.lowercase() }
      ?: cities.firstOrNull { city -> city.aliases.any { normalized.contains(it) } }
    return if (match != null) Triple(match, match.label, true)
    else Triple(indiaReference, "$input · India reference", false)
  }

  private fun solarEvent(date: LocalDate, location: GeoLocation, zoneId: ZoneId, rising: Boolean): LocalTime? {
    val day = date.dayOfYear.toDouble()
    val longitudeHour = location.longitude / 15.0
    val approximateTime = day + ((if (rising) 6.0 else 18.0) - longitudeHour) / 24.0
    val meanAnomaly = 0.9856 * approximateTime - 3.289
    val sunLongitude = normalize(meanAnomaly + 1.916 * sinDeg(meanAnomaly) + 0.020 * sinDeg(2 * meanAnomaly) + 282.634)
    var rightAscension = atan2(0.91764 * tan(sunLongitude * DEG), 1.0) / DEG
    rightAscension = normalize(rightAscension)
    rightAscension += floor(sunLongitude / 90.0) * 90.0 - floor(rightAscension / 90.0) * 90.0
    rightAscension /= 15.0
    val sinDeclination = 0.39782 * sinDeg(sunLongitude)
    val cosDeclination = cos(asin(sinDeclination))
    val cosHourAngle = (cosDeg(90.833) - sinDeclination * sinDeg(location.latitude)) /
      (cosDeclination * cosDeg(location.latitude))
    if (cosHourAngle !in -1.0..1.0) return null
    val hourAngle = (if (rising) 360.0 - acos(cosHourAngle) / DEG else acos(cosHourAngle) / DEG) / 15.0
    val localMeanTime = hourAngle + rightAscension - 0.06571 * approximateTime - 6.622
    val universalTime = mod24(localMeanTime - longitudeHour)
    return LocalDateTime.of(date, LocalTime.MIDNIGHT)
      .toInstant(ZoneOffset.UTC)
      .plusSeconds((universalTime * 3_600.0).roundToInt().toLong())
      .atZone(zoneId)
      .toLocalTime()
  }

  private fun moonEvents(date: LocalDate, location: GeoLocation, zoneId: ZoneId): Pair<LocalTime?, LocalTime?> {
    val start = date.atStartOfDay(zoneId)
    var previous = moonAltitude(start.toInstant(), location) - MOON_ALTITUDE_THRESHOLD
    var rise: LocalTime? = null
    var set: LocalTime? = null
    for (minute in 10..(24 * 60) step 10) {
      val currentDateTime = start.plusMinutes(minute.toLong())
      val current = moonAltitude(currentDateTime.toInstant(), location) - MOON_ALTITUDE_THRESHOLD
      if (rise == null && previous < 0.0 && current >= 0.0) {
        rise = interpolateEvent(start, minute, previous, current)
      }
      if (set == null && previous >= 0.0 && current < 0.0) {
        set = interpolateEvent(start, minute, previous, current)
      }
      previous = current
    }
    return rise to set
  }

  private fun interpolateEvent(start: java.time.ZonedDateTime, minute: Int, before: Double, after: Double): LocalTime {
    val fraction = abs(before) / (abs(before) + abs(after))
    val eventMinute = minute - 10 + (10 * fraction).roundToInt()
    return start.plusMinutes(eventMinute.toLong()).toLocalTime()
  }

  private fun moonAltitude(instant: Instant, location: GeoLocation): Double {
    val jd = julianDay(instant)
    val lunar = lunarPosition(jd)
    val obliquity = (23.4393 - 3.563E-7 * (jd - 2451543.5)) * DEG
    val longitude = lunar.longitude * DEG
    val latitude = lunar.latitude * DEG
    val x = cos(longitude) * cos(latitude)
    val y = sin(longitude) * cos(latitude)
    val z = sin(latitude)
    val rightAscension = atan2(y * cos(obliquity) - z * sin(obliquity), x) / DEG
    val declination = asin(y * sin(obliquity) + z * cos(obliquity))
    val sidereal = normalize(280.46061837 + 360.98564736629 * (jd - 2451545.0) + location.longitude)
    val hourAngle = normalizeSigned(sidereal - rightAscension) * DEG
    val latitudeRadians = location.latitude * DEG
    return asin(
      sin(latitudeRadians) * sin(declination) + cos(latitudeRadians) * cos(declination) * cos(hourAngle),
    ) / DEG
  }

  private fun solarLongitude(jd: Double): Double {
    val days = jd - 2451543.5
    val perihelion = 282.9404 + 4.70935E-5 * days
    val eccentricity = 0.016709 - 1.151E-9 * days
    val meanAnomaly = normalize(356.0470 + 0.9856002585 * days) * DEG
    var eccentricAnomaly = meanAnomaly + eccentricity * sin(meanAnomaly)
    repeat(5) {
      eccentricAnomaly -= (eccentricAnomaly - eccentricity * sin(eccentricAnomaly) - meanAnomaly) /
        (1 - eccentricity * cos(eccentricAnomaly))
    }
    val x = cos(eccentricAnomaly) - eccentricity
    val y = sqrt(1 - eccentricity * eccentricity) * sin(eccentricAnomaly)
    return normalize(atan2(y, x) / DEG + perihelion)
  }

  private fun lunarPosition(jd: Double): LunarPosition {
    val days = jd - 2451543.5
    val node = normalize(125.1228 - 0.0529538083 * days)
    val inclination = 5.1454
    val perihelion = normalize(318.0634 + 0.1643573223 * days)
    val semiMajorAxis = 60.2666
    val eccentricity = 0.054900
    val meanAnomaly = normalize(115.3654 + 13.0649929509 * days)
    val meanRadians = meanAnomaly * DEG
    var eccentricAnomaly = meanRadians + eccentricity * sin(meanRadians)
    repeat(5) {
      eccentricAnomaly -= (eccentricAnomaly - eccentricity * sin(eccentricAnomaly) - meanRadians) /
        (1 - eccentricity * cos(eccentricAnomaly))
    }
    val xOrbital = semiMajorAxis * (cos(eccentricAnomaly) - eccentricity)
    val yOrbital = semiMajorAxis * sqrt(1 - eccentricity * eccentricity) * sin(eccentricAnomaly)
    val trueAnomaly = atan2(yOrbital, xOrbital) / DEG
    val radius = sqrt(xOrbital * xOrbital + yOrbital * yOrbital)
    val nodeRadians = node * DEG
    val argumentRadians = (trueAnomaly + perihelion) * DEG
    val inclinationRadians = inclination * DEG
    val x = radius * (cos(nodeRadians) * cos(argumentRadians) - sin(nodeRadians) * sin(argumentRadians) * cos(inclinationRadians))
    val y = radius * (sin(nodeRadians) * cos(argumentRadians) + cos(nodeRadians) * sin(argumentRadians) * cos(inclinationRadians))
    val z = radius * sin(argumentRadians) * sin(inclinationRadians)
    var longitude = atan2(y, x) / DEG
    var latitude = atan2(z, sqrt(x * x + y * y)) / DEG
    val sunLongitude = solarLongitude(jd)
    val meanLongitude = normalize(node + perihelion + meanAnomaly)
    val elongation = normalize(meanLongitude - sunLongitude)
    val argumentLatitude = normalize(meanLongitude - node)
    longitude += -1.274 * sinDeg(meanAnomaly - 2 * elongation) + 0.658 * sinDeg(2 * elongation) -
      0.186 * sinDeg(normalize(356.0470 + 0.9856002585 * days)) - 0.059 * sinDeg(2 * meanAnomaly - 2 * elongation) -
      0.057 * sinDeg(meanAnomaly - 2 * elongation + normalize(356.0470 + 0.9856002585 * days)) +
      0.053 * sinDeg(meanAnomaly + 2 * elongation) + 0.046 * sinDeg(2 * elongation - normalize(356.0470 + 0.9856002585 * days)) +
      0.041 * sinDeg(meanAnomaly - normalize(356.0470 + 0.9856002585 * days)) - 0.035 * sinDeg(elongation) -
      0.031 * sinDeg(meanAnomaly + normalize(356.0470 + 0.9856002585 * days)) - 0.015 * sinDeg(2 * argumentLatitude - 2 * elongation)
    latitude += -0.173 * sinDeg(argumentLatitude - 2 * elongation) - 0.055 * sinDeg(meanAnomaly - argumentLatitude - 2 * elongation) -
      0.046 * sinDeg(meanAnomaly + argumentLatitude - 2 * elongation) + 0.033 * sinDeg(argumentLatitude + 2 * elongation)
    return LunarPosition(normalize(longitude), latitude)
  }

  private fun sakaDate(date: LocalDate): String {
    var startYear = date.year
    var sakaStart = sakaNewYear(startYear)
    if (date.isBefore(sakaStart)) {
      startYear -= 1
      sakaStart = sakaNewYear(startYear)
    }
    var remaining = java.time.temporal.ChronoUnit.DAYS.between(sakaStart, date).toInt()
    val monthLengths = listOf(if (Year.isLeap(startYear.toLong())) 31 else 30, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 30)
    var monthIndex = 0
    while (monthIndex < monthLengths.lastIndex && remaining >= monthLengths[monthIndex]) {
      remaining -= monthLengths[monthIndex]
      monthIndex += 1
    }
    return "${remaining + 1} ${sakaMonthNames[monthIndex]} ${startYear - 78} Saka"
  }

  private fun sakaNewYear(year: Int): LocalDate = LocalDate.of(year, 3, if (Year.isLeap(year.toLong())) 21 else 22)
  private fun julianDay(instant: Instant): Double = instant.epochSecond / 86_400.0 + 2_440_587.5
  private fun normalize(value: Double): Double = ((value % 360.0) + 360.0) % 360.0
  private fun normalizeSigned(value: Double): Double = ((value + 540.0) % 360.0) - 180.0
  private fun mod24(value: Double): Double = ((value % 24.0) + 24.0) % 24.0
  private fun sinDeg(value: Double): Double = sin(value * DEG)
  private fun cosDeg(value: Double): Double = cos(value * DEG)
}
