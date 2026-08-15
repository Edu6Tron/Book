package com.edu6tron.spiritualcompanion.nativepreview.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu6tron.spiritualcompanion.nativepreview.alarm.RitualAlarmScheduler
import com.edu6tron.spiritualcompanion.nativepreview.data.DailyPractice
import com.edu6tron.spiritualcompanion.nativepreview.data.RitualAlarmEntity
import com.edu6tron.spiritualcompanion.nativepreview.data.SpiritualRepository
import com.edu6tron.spiritualcompanion.nativepreview.media.NativeDevotionalPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DashboardUiState(
  val practices: List<DailyPractice> = emptyList(),
  val favouriteIds: Set<String> = emptySet(),
  val japaCount: Int = 0,
  val ritualAlarms: List<RitualAlarmEntity> = emptyList(),
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
  private val repository: SpiritualRepository,
  private val player: NativeDevotionalPlayer,
  @ApplicationContext private val context: Context,
) : ViewModel() {
  val state: StateFlow<DashboardUiState> = repository.observeState()
    .map { stored -> DashboardUiState(stored.practices, stored.favouriteIds, stored.japaCount, stored.ritualAlarms) }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardUiState())

  fun togglePractice(id: String) {
    viewModelScope.launch {
      repository.togglePractice(id)
    }
  }

  fun toggleFavourite(aartiId: String) {
    viewModelScope.launch { repository.toggleFavourite(aartiId) }
  }

  fun incrementJapa() {
    viewModelScope.launch { repository.incrementJapa() }
  }

  fun resetJapa() {
    viewModelScope.launch { repository.resetJapa() }
  }

  fun saveAlarm(alarm: RitualAlarmEntity) {
    viewModelScope.launch {
      repository.saveAlarm(alarm)
      if (alarm.enabled) RitualAlarmScheduler.scheduleNext(context, alarm) else RitualAlarmScheduler.cancel(context, alarm.id)
    }
  }

  fun setAlarmEnabled(alarm: RitualAlarmEntity, enabled: Boolean) = saveAlarm(alarm.copy(enabled = enabled))

  fun deleteAlarm(alarm: RitualAlarmEntity) {
    viewModelScope.launch {
      RitualAlarmScheduler.cancel(context, alarm.id)
      repository.deleteAlarm(alarm)
    }
  }

  fun playFallbackTone() = player.playBundledFallback()

  fun stopTonePreview() = player.stop()

  override fun onCleared() {
    player.stop()
    super.onCleared()
  }
}
