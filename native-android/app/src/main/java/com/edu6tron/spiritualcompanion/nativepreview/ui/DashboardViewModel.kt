package com.edu6tron.spiritualcompanion.nativepreview.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu6tron.spiritualcompanion.nativepreview.data.DailyPractice
import com.edu6tron.spiritualcompanion.nativepreview.data.SpiritualRepository
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
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
  private val repository: SpiritualRepository,
) : ViewModel() {
  val state: StateFlow<DashboardUiState> = repository.observeState()
    .map { stored -> DashboardUiState(stored.practices, stored.favouriteIds, stored.japaCount) }
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
}
