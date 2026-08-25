package com.edu6tron.spiritualcompanion.nativepreview.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.Immutable
import com.edu6tron.spiritualcompanion.nativepreview.alarm.RitualAlarmScheduler
import com.edu6tron.spiritualcompanion.nativepreview.media.AartiAudioAssociation
import com.edu6tron.spiritualcompanion.nativepreview.data.DailyPractice
import com.edu6tron.spiritualcompanion.nativepreview.data.DevotionalTheme
import com.edu6tron.spiritualcompanion.nativepreview.data.ReadingComfort
import com.edu6tron.spiritualcompanion.nativepreview.data.RitualAlarmEntity
import com.edu6tron.spiritualcompanion.nativepreview.data.RoutineDailyProgress
import com.edu6tron.spiritualcompanion.nativepreview.data.SpiritualRepository
import com.edu6tron.spiritualcompanion.nativepreview.data.ThemeMode
import com.edu6tron.spiritualcompanion.nativepreview.diagnostics.NativeDiagnostics
import com.edu6tron.spiritualcompanion.nativepreview.media.NativeDevotionalPlayer
import com.edu6tron.spiritualcompanion.nativepreview.media.OfflineSoundscape
import com.edu6tron.spiritualcompanion.nativepreview.panchang.OnlineAstronomyCache
import com.edu6tron.spiritualcompanion.nativepreview.panchang.PanchangCalculator
import com.edu6tron.spiritualcompanion.nativepreview.panchang.UsnoAstronomyClient
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Immutable
data class DashboardContentState(
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
  val aartiAudioByAarti: Map<String, AartiAudioAssociation> = emptyMap(),
)

@Immutable
data class DashboardUiState(
  val content: DashboardContentState = DashboardContentState(),
  val playback: com.edu6tron.spiritualcompanion.nativepreview.media.DevotionalPlaybackState = com.edu6tron.spiritualcompanion.nativepreview.media.DevotionalPlaybackState(),
  val notice: String? = null,
  val timingRefreshInProgress: Boolean = false,
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
  private val repository: SpiritualRepository,
  private val player: NativeDevotionalPlayer,
  private val usnoAstronomyClient: UsnoAstronomyClient,
  @ApplicationContext private val context: Context,
) : ViewModel() {
  private val notice = MutableStateFlow<String?>(null)
  private val timingRefreshInProgress = MutableStateFlow(false)

  val content: StateFlow<DashboardContentState> = repository.observeState()
    .map { stored ->
      DashboardContentState(
        practices = stored.practices,
        favouriteIds = stored.favouriteIds,
        japaCount = stored.japaCount,
        ritualAlarms = stored.ritualAlarms,
        selectedMediaUri = stored.selectedMediaUri,
        selectedMediaLabel = stored.selectedMediaLabel,
        savedLocation = stored.savedLocation,
        readingComfort = stored.readingComfort,
        themeMode = stored.themeMode,
        devotionalTheme = stored.devotionalTheme,
        eveningRoutineEnabled = stored.eveningRoutineEnabled,
        brahmaMuhurtaRoutineEnabled = stored.brahmaMuhurtaRoutineEnabled,
        eveningRoutineProgress = stored.eveningRoutineProgress,
        brahmaMuhurtaRoutineProgress = stored.brahmaMuhurtaRoutineProgress,
        onlineAstronomyCache = stored.onlineAstronomyCache,
        personalLyricTimingByAarti = stored.personalLyricTimingByAarti,
        aartiAudioByAarti = stored.aartiAudioByAarti,
      )
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardContentState())

  private val dashboardState = combine(content, player.playback, notice) { content, playback, currentNotice ->
    DashboardUiState(content = content, playback = playback, notice = currentNotice)
  }

  val state: StateFlow<DashboardUiState> = combine(dashboardState, timingRefreshInProgress) { dashboard, refreshing ->
    dashboard.copy(timingRefreshInProgress = refreshing)
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardUiState())

  fun togglePractice(id: String) {
    launchSafely("daily-practice") { repository.togglePractice(id) }
  }

  fun toggleFavourite(aartiId: String) {
    launchSafely("aarti-favourite") { repository.toggleFavourite(aartiId) }
  }

  fun incrementJapa(amount: Int = 1) {
    launchSafely("japa-count") { repository.incrementJapa(amount) }
  }

  fun resetJapa() {
    launchSafely("japa-reset") { repository.resetJapa() }
  }

  fun saveAlarm(alarm: RitualAlarmEntity) {
    launchSafely("ritual-alarm") {
      repository.saveAlarm(alarm)
      val exactAvailable = if (alarm.enabled) RitualAlarmScheduler.scheduleNext(context, alarm) else {
        RitualAlarmScheduler.cancel(context, alarm.id)
        true
      }
      if (alarm.enabled && !exactAvailable) {
        notice.value = "Alarm saved. Allow Alarms & reminders for exact timing."
      } else if (alarm.enabled && !RitualAlarmScheduler.isIgnoringBatteryOptimizations(context)) {
        notice.value = "Alarm saved. Allow unrestricted battery use to reduce screen-off delays."
      }
    }
  }

  fun setAlarmEnabled(alarm: RitualAlarmEntity, enabled: Boolean) = saveAlarm(alarm.copy(enabled = enabled))

  fun pauseAlarm(alarm: RitualAlarmEntity, days: Int) {
    saveAlarm(
      alarm.copy(
        pauseUntilMillis = System.currentTimeMillis() + days.coerceIn(1, 30) * 86_400_000L,
        updatedAt = System.currentTimeMillis(),
      ),
    )
  }

  fun resumeAlarm(alarm: RitualAlarmEntity) = saveAlarm(
    alarm.copy(pauseUntilMillis = null, updatedAt = System.currentTimeMillis()),
  )

  fun deleteAlarm(alarm: RitualAlarmEntity) {
    launchSafely("delete-ritual-alarm") {
      RitualAlarmScheduler.cancel(context, alarm.id)
      repository.deleteAlarm(alarm)
    }
  }

  fun playFallbackTone() = player.playBundledFallback()

  fun playOfflineSoundscape(soundscape: OfflineSoundscape) = player.playOfflineSoundscape(soundscape)

  fun stopTonePreview() = player.stop()

  fun saveSelectedMedia(uri: String, label: String) {
    launchSafely("save-local-media") { repository.saveSelectedMedia(uri, label) }
  }

  fun clearSelectedMedia() {
    launchSafely("clear-local-media") { repository.clearSelectedMedia() }
    player.stop()
  }

  fun assignSelectedMediaToAarti(aartiId: String, uri: String, label: String) {
    launchSafely("assign-aarti-audio") { repository.assignAudioToAarti(aartiId, uri, label) }
  }

  fun clearSelectedMediaForAarti(aartiId: String) {
    launchSafely("clear-aarti-audio") { repository.clearAudioForAarti(aartiId) }
  }

  fun playSelectedMedia(uri: String, label: String) = player.play(uri, label)

  fun toggleSelectedMediaPlayback() = player.togglePlayPause()

  fun seekSelectedMediaTo(positionMs: Long) = player.seekTo(positionMs)

  fun seekSelectedMediaBy(deltaMs: Long) = player.seekBy(deltaMs)

  fun savePersonalLyricTiming(aartiId: String, offsetsMs: List<Long>) {
    launchSafely("save-lyric-timing") { repository.savePersonalLyricTiming(aartiId, offsetsMs) }
  }

  fun clearPersonalLyricTiming(aartiId: String) {
    launchSafely("clear-lyric-timing") { repository.clearPersonalLyricTiming(aartiId) }
  }

  fun previewAlarmTone(uri: String?) {
    if (uri.isNullOrBlank()) player.playBundledFallback() else player.play(uri, "Selected local alarm tone")
  }

  fun saveLocation(location: String) {
    launchSafely("save-city") { repository.saveLocation(location) }
  }

  fun clearLocation() {
    launchSafely("clear-city") { repository.clearLocation() }
  }

  fun refreshOnlineAstronomyTimings() {
    if (timingRefreshInProgress.value) return
    val location = PanchangCalculator.onlineTimingLocation(content.value.savedLocation)
    if (location == null) {
      notice.value = "Choose a supported city before refreshing online timing data."
      return
    }
    viewModelScope.launch {
      timingRefreshInProgress.value = true
      runCatching {
        usnoAstronomyClient.refreshNextThirtyOneDays(location)
      }.onSuccess { result ->
        if (result.timings.isEmpty()) {
          notice.value = "Online timing data is unavailable. Offline estimates remain in use."
        } else {
          repository.saveOnlineAstronomyCache(
            OnlineAstronomyCache(
              locationCacheKey = result.locationCacheKey,
              refreshedAtEpochMillis = System.currentTimeMillis(),
              entries = result.timings,
            ),
          )
          notice.value = if (result.timings.size == result.requestedDays) {
            "Online astronomical timing data saved for the next ${result.requestedDays} days."
          } else {
            "Online timing data saved for ${result.timings.size} days; offline estimates cover the rest."
          }
        }
      }.onFailure { error ->
        NativeDiagnostics.recordFailure("online-timing-refresh", error)
        notice.value = "Online timing data could not be refreshed. Offline estimates remain in use."
      }
      timingRefreshInProgress.value = false
    }
  }

  fun saveReadingComfort(readingComfort: ReadingComfort) {
    launchSafely("reading-comfort") { repository.saveReadingComfort(readingComfort) }
  }

  fun saveThemeMode(themeMode: ThemeMode) {
    launchSafely("theme-mode") { repository.saveThemeMode(themeMode) }
  }

  fun saveDevotionalTheme(devotionalTheme: DevotionalTheme) {
    launchSafely("devotional-theme") { repository.saveDevotionalTheme(devotionalTheme) }
  }

  fun saveEveningRoutineEnabled(enabled: Boolean) {
    launchSafely("evening-routine") { repository.saveEveningRoutineEnabled(enabled) }
  }

  fun saveBrahmaMuhurtaRoutineEnabled(enabled: Boolean) {
    launchSafely("brahma-muhurta-routine") { repository.saveBrahmaMuhurtaRoutineEnabled(enabled) }
  }

  fun setRoutineStepCompleted(routineId: String, stepId: String, completed: Boolean) {
    launchSafely("routine-progress") { repository.setRoutineStepCompleted(routineId, stepId, completed) }
  }

  fun resetRoutineProgress(routineId: String) {
    launchSafely("routine-progress-reset") { repository.resetRoutineProgress(routineId) }
  }

  fun openNotificationSettings() {
    runCatching {
      context.startActivity(
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
          .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
          .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
      )
    }.onFailure {
      notice.value = "Android notification settings could not be opened."
    }
  }

  fun openExactAlarmSettings() {
    runCatching {
      val intent = Intent(
        Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
        Uri.parse("package:${context.packageName}"),
      ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      check(intent.resolveActivity(context.packageManager) != null) {
        "Exact-alarm settings are unavailable"
      }
      context.startActivity(intent)
    }.onFailure {
      notice.value = "Android exact-alarm settings could not be opened."
    }
  }

  fun dismissNotice() {
    notice.value = null
  }

  private fun launchSafely(component: String, block: suspend () -> Unit) {
    viewModelScope.launch {
      runCatching { block() }.onFailure { error ->
        NativeDiagnostics.recordFailure(component, error)
        notice.value = "That action could not be completed. Please try again."
      }
    }
  }

  override fun onCleared() {
    player.release()
    super.onCleared()
  }
}
