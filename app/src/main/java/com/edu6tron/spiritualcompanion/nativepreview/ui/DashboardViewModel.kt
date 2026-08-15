package com.edu6tron.spiritualcompanion.nativepreview.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu6tron.spiritualcompanion.nativepreview.alarm.RitualAlarmScheduler
import com.edu6tron.spiritualcompanion.nativepreview.data.DailyPractice
import com.edu6tron.spiritualcompanion.nativepreview.data.RitualAlarmEntity
import com.edu6tron.spiritualcompanion.nativepreview.data.SpiritualRepository
import com.edu6tron.spiritualcompanion.nativepreview.diagnostics.NativeDiagnostics
import com.edu6tron.spiritualcompanion.nativepreview.media.NativeDevotionalPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DashboardUiState(
  val practices: List<DailyPractice> = emptyList(),
  val favouriteIds: Set<String> = emptySet(),
  val japaCount: Int = 0,
  val ritualAlarms: List<RitualAlarmEntity> = emptyList(),
  val selectedMediaUri: String? = null,
  val selectedMediaLabel: String? = null,
  val savedLocation: String? = null,
  val playback: com.edu6tron.spiritualcompanion.nativepreview.media.DevotionalPlaybackState = com.edu6tron.spiritualcompanion.nativepreview.media.DevotionalPlaybackState(),
  val notice: String? = null,
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
  private val repository: SpiritualRepository,
  private val player: NativeDevotionalPlayer,
  @ApplicationContext private val context: Context,
) : ViewModel() {
  private val notice = MutableStateFlow<String?>(null)

  val state: StateFlow<DashboardUiState> = combine(repository.observeState(), player.playback, notice) { stored, playback, currentNotice ->
      DashboardUiState(
        practices = stored.practices,
        favouriteIds = stored.favouriteIds,
        japaCount = stored.japaCount,
        ritualAlarms = stored.ritualAlarms,
        selectedMediaUri = stored.selectedMediaUri,
        selectedMediaLabel = stored.selectedMediaLabel,
        savedLocation = stored.savedLocation,
        playback = playback,
        notice = currentNotice,
      )
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardUiState())

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

  fun stopTonePreview() = player.stop()

  fun saveSelectedMedia(uri: String, label: String) {
    launchSafely("save-local-media") { repository.saveSelectedMedia(uri, label) }
  }

  fun clearSelectedMedia() {
    launchSafely("clear-local-media") { repository.clearSelectedMedia() }
    player.stop()
  }

  fun playSelectedMedia(uri: String, label: String) = player.play(uri, label)

  fun previewAlarmTone(uri: String?) {
    if (uri.isNullOrBlank()) player.playBundledFallback() else player.play(uri, "Selected local alarm tone")
  }

  fun saveLocation(location: String) {
    launchSafely("save-city") { repository.saveLocation(location) }
  }

  fun clearLocation() {
    launchSafely("clear-city") { repository.clearLocation() }
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
    player.stop()
    super.onCleared()
  }
}
