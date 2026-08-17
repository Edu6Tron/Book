package com.edu6tron.spiritualcompanion.nativepreview.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import com.edu6tron.spiritualcompanion.nativepreview.panchang.OnlineAstronomyCache
import com.edu6tron.spiritualcompanion.nativepreview.panchang.OnlineAstronomyCacheCodec
import com.edu6tron.spiritualcompanion.nativepreview.media.PersonalLyricTimingCodec
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

data class DailyPractice(
  val id: String,
  val title: String,
  val detail: String,
  val completed: Boolean,
)

data class StoredSpiritualState(
  val practices: List<DailyPractice> = emptyList(),
  val favouriteIds: Set<String> = emptySet(),
  val japaCount: Int = 0,
  val ritualAlarms: List<RitualAlarmEntity> = emptyList(),
  val selectedMediaUri: String? = null,
  val selectedMediaLabel: String? = null,
  val savedLocation: String? = null,
  val readingComfort: ReadingComfort = ReadingComfort.STANDARD,
  val themeMode: ThemeMode = ThemeMode.LIGHT,
  val devotionalTheme: DevotionalTheme = DevotionalTheme.SACRED_SAFFRON,
  val eveningRoutineEnabled: Boolean = false,
  val brahmaMuhurtaRoutineEnabled: Boolean = false,
  val eveningRoutineProgress: RoutineDailyProgress = RoutineDailyProgress(),
  val brahmaMuhurtaRoutineProgress: RoutineDailyProgress = RoutineDailyProgress(),
  val onlineAstronomyCache: OnlineAstronomyCache? = null,
  val personalLyricTimingByAarti: Map<String, List<Long>> = emptyMap(),
)

@Singleton
class SpiritualRepository @Inject constructor(
  private val dailyPracticeDao: DailyPracticeDao,
  private val appStateDao: AppStateDao,
  private val ritualAlarmDao: RitualAlarmDao,
  private val mediaSelectionDao: MediaSelectionDao,
) {
  private val defaults = listOf(
    DailyPractice("pause", "Pause for one minute", "Settle your attention with three unhurried breaths.", false),
    DailyPractice("reading", "Read one meaningful line", "Choose an Aarti or a festival reflection that feels relevant today.", false),
    DailyPractice("kindness", "Offer one act of kindness", "Make your practice visible through a small, deliberate act.", false),
  )

  private val japaKey = "japa_count"
  private val selectedMediaId = "devotional_audio"
  private val savedLocationKey = "saved_location"
  private val readingComfortKey = "reading_comfort"
  private val themeModeKey = "theme_mode"
  private val devotionalThemeKey = "devotional_theme"
  private val eveningRoutineEnabledKey = "evening_routine_enabled"
  private val brahmaMuhurtaRoutineEnabledKey = "brahma_muhurta_routine_enabled"
  private val eveningRoutineProgressKey = "evening_routine_progress"
  private val brahmaMuhurtaRoutineProgressKey = "brahma_muhurta_routine_progress"
  private val onlineAstronomyCacheKey = "online_astronomy_cache_v1"
  private val personalLyricTimingKey = "personal_lyric_timing_v1"

  fun observeDailyPractices(): Flow<List<DailyPractice>> =
    dailyPracticeDao.observeAll().map { stored ->
      val storedById = stored.associateBy { it.id }
      defaults.map { default ->
        val saved = storedById[default.id]
        default.copy(completed = saved?.completed ?: default.completed)
      }
    }

  fun observeState(): Flow<StoredSpiritualState> = combine(
    observeDailyPractices(),
    appStateDao.observeFavouriteIds(),
    appStateDao.observeLong(japaKey),
    ritualAlarmDao.observeAll(),
    mediaSelectionDao.observe(selectedMediaId),
  ) { practices, favourites, japa, alarms, media ->
    StoredSpiritualState(
      practices = practices,
      favouriteIds = favourites.toSet(),
      japaCount = (japa ?: 0L).toInt().coerceAtLeast(0),
      ritualAlarms = alarms,
      selectedMediaUri = media?.uri,
      selectedMediaLabel = media?.label,
    )
  }.combine(appStateDao.observeString(savedLocationKey)) { state, savedLocation ->
    state.copy(savedLocation = savedLocation)
  }.combine(appStateDao.observeString(readingComfortKey)) { state, readingComfort ->
    state.copy(readingComfort = ReadingComfort.fromStored(readingComfort))
  }.combine(appStateDao.observeString(themeModeKey)) { state, themeMode ->
    state.copy(themeMode = ThemeMode.fromStored(themeMode))
  }.combine(appStateDao.observeString(devotionalThemeKey)) { state, devotionalTheme ->
    state.copy(devotionalTheme = DevotionalTheme.fromStored(devotionalTheme))
  }.combine(appStateDao.observeString(eveningRoutineEnabledKey)) { state, enabled ->
    state.copy(eveningRoutineEnabled = enabled.toStoredBoolean())
  }.combine(appStateDao.observeString(brahmaMuhurtaRoutineEnabledKey)) { state, enabled ->
    state.copy(brahmaMuhurtaRoutineEnabled = enabled.toStoredBoolean())
  }.combine(appStateDao.observeString(eveningRoutineProgressKey)) { state, progress ->
    state.copy(eveningRoutineProgress = RoutineDailyProgress.fromStored(progress))
  }.combine(appStateDao.observeString(brahmaMuhurtaRoutineProgressKey)) { state, progress ->
    state.copy(brahmaMuhurtaRoutineProgress = RoutineDailyProgress.fromStored(progress))
  }.combine(appStateDao.observeString(onlineAstronomyCacheKey)) { state, cache ->
    state.copy(onlineAstronomyCache = OnlineAstronomyCacheCodec.decode(cache))
  }.combine(appStateDao.observeString(personalLyricTimingKey)) { state, markers ->
    state.copy(personalLyricTimingByAarti = PersonalLyricTimingCodec.decode(markers))
  }

  suspend fun togglePractice(id: String) {
    val definition = defaults.firstOrNull { it.id == id } ?: return
    val saved = dailyPracticeDao.findById(id)
    dailyPracticeDao.upsert(
      DailyPracticeEntity(
        id = definition.id,
        title = definition.title,
        completed = !(saved?.completed ?: definition.completed),
        updatedAt = System.currentTimeMillis(),
      ),
    )
  }

  suspend fun toggleFavourite(aartiId: String) {
    if (appStateDao.isFavourite(aartiId)) {
      appStateDao.removeFavourite(aartiId)
    } else {
      appStateDao.saveFavourite(FavouriteAartiEntity(aartiId = aartiId))
    }
  }

  suspend fun incrementJapa(amount: Int = 1) {
    val current = appStateDao.getLong(japaKey) ?: 0L
    appStateDao.savePreference(AppPreferenceEntity(japaKey, current + amount.coerceIn(1, 108).toLong()))
  }

  suspend fun resetJapa() {
    appStateDao.savePreference(AppPreferenceEntity(japaKey, 0L))
  }

  suspend fun saveAlarm(alarm: RitualAlarmEntity) {
    ritualAlarmDao.upsert(alarm.copy(updatedAt = System.currentTimeMillis()))
  }

  suspend fun deleteAlarm(alarm: RitualAlarmEntity) {
    ritualAlarmDao.delete(alarm)
  }

  suspend fun saveSelectedMedia(uri: String, label: String) {
    mediaSelectionDao.upsert(
      MediaSelectionEntity(uri = uri, label = label, updatedAt = System.currentTimeMillis()),
    )
  }

  suspend fun clearSelectedMedia() {
    mediaSelectionDao.clear(selectedMediaId)
    appStateDao.deleteStringPreference(personalLyricTimingKey)
  }

  suspend fun savePersonalLyricTiming(aartiId: String, offsetsMs: List<Long>) {
    if (!aartiId.matches(Regex("[a-z0-9-]+")) || offsetsMs.isEmpty() || offsetsMs.any { it < 0L }) return
    val current = PersonalLyricTimingCodec.decode(appStateDao.getString(personalLyricTimingKey)).toMutableMap()
    current[aartiId] = offsetsMs
    val encoded = PersonalLyricTimingCodec.encode(current)
    if (encoded == null) appStateDao.deleteStringPreference(personalLyricTimingKey)
    else appStateDao.saveStringPreference(StringPreferenceEntity(personalLyricTimingKey, encoded))
  }

  suspend fun clearPersonalLyricTiming(aartiId: String) {
    val current = PersonalLyricTimingCodec.decode(appStateDao.getString(personalLyricTimingKey)).toMutableMap()
    current.remove(aartiId)
    val encoded = PersonalLyricTimingCodec.encode(current)
    if (encoded == null) appStateDao.deleteStringPreference(personalLyricTimingKey)
    else appStateDao.saveStringPreference(StringPreferenceEntity(personalLyricTimingKey, encoded))
  }

  suspend fun saveLocation(location: String) {
    val cleaned = location.trim()
    if (cleaned.isBlank()) {
      appStateDao.deleteStringPreference(savedLocationKey)
    } else {
      appStateDao.saveStringPreference(StringPreferenceEntity(savedLocationKey, cleaned))
    }
    appStateDao.deleteStringPreference(onlineAstronomyCacheKey)
  }

  suspend fun clearLocation() {
    appStateDao.deleteStringPreference(savedLocationKey)
    appStateDao.deleteStringPreference(onlineAstronomyCacheKey)
  }

  suspend fun saveOnlineAstronomyCache(cache: OnlineAstronomyCache) {
    appStateDao.saveStringPreference(
      StringPreferenceEntity(onlineAstronomyCacheKey, OnlineAstronomyCacheCodec.encode(cache)),
    )
  }

  suspend fun saveReadingComfort(readingComfort: ReadingComfort) {
    appStateDao.saveStringPreference(StringPreferenceEntity(readingComfortKey, readingComfort.storedValue))
  }

  suspend fun saveThemeMode(themeMode: ThemeMode) {
    appStateDao.saveStringPreference(StringPreferenceEntity(themeModeKey, themeMode.storedValue))
  }

  suspend fun saveDevotionalTheme(devotionalTheme: DevotionalTheme) {
    appStateDao.saveStringPreference(StringPreferenceEntity(devotionalThemeKey, devotionalTheme.storedValue))
  }

  suspend fun saveEveningRoutineEnabled(enabled: Boolean) {
    appStateDao.saveStringPreference(StringPreferenceEntity(eveningRoutineEnabledKey, enabled.toString()))
  }

  suspend fun saveBrahmaMuhurtaRoutineEnabled(enabled: Boolean) {
    appStateDao.saveStringPreference(StringPreferenceEntity(brahmaMuhurtaRoutineEnabledKey, enabled.toString()))
  }

  suspend fun setRoutineStepCompleted(routineId: String, stepId: String, completed: Boolean) {
    val preferenceKey = progressPreferenceKey(routineId) ?: return
    val today = LocalDate.now()
    val currentSteps = RoutineDailyProgress.fromStored(appStateDao.getString(preferenceKey))
      .completedStepsFor(today)
      .toMutableSet()
    if (completed) currentSteps += stepId else currentSteps -= stepId
    val updated = RoutineDailyProgress(date = today, completedStepIds = currentSteps)
    val storedValue = updated.toStoredValue()
    if (storedValue == null) {
      appStateDao.deleteStringPreference(preferenceKey)
    } else {
      appStateDao.saveStringPreference(StringPreferenceEntity(preferenceKey, storedValue))
    }
  }

  suspend fun resetRoutineProgress(routineId: String) {
    val preferenceKey = progressPreferenceKey(routineId) ?: return
    appStateDao.deleteStringPreference(preferenceKey)
  }

  private fun progressPreferenceKey(routineId: String): String? = when (routineId) {
    NativeDevotionalRoutines.eveningPrarthana.id -> eveningRoutineProgressKey
    NativeDevotionalRoutines.brahmaMuhurta.id -> brahmaMuhurtaRoutineProgressKey
    else -> null
  }

  private fun String?.toStoredBoolean(): Boolean = equals("true", ignoreCase = true)
}
