package com.edu6tron.spiritualcompanion.nativepreview.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.edu6tron.spiritualcompanion.nativepreview.data.RitualAlarmEntity

private enum class NativeTab(val title: String) {
  TODAY("Today"),
  AARTIS("Aartis"),
  FESTIVALS("Festivals"),
  PRACTICE("Practice"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpiritualCompanionApp(
  state: DashboardUiState,
  onTogglePractice: (String) -> Unit,
  onIncrementJapa: () -> Unit,
  onResetJapa: () -> Unit,
  onToggleFavourite: (String) -> Unit,
  onSaveAlarm: (RitualAlarmEntity) -> Unit,
  onSetAlarmEnabled: (RitualAlarmEntity, Boolean) -> Unit,
  onDeleteAlarm: (RitualAlarmEntity) -> Unit,
  onPlayFallbackTone: () -> Unit,
  onStopTonePreview: () -> Unit,
) {
  var selectedTab by remember { mutableStateOf(NativeTab.TODAY) }
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    bottomBar = {
      NavigationBar {
        listOf(
          Triple(NativeTab.TODAY, Icons.Outlined.Home, "Today"),
          Triple(NativeTab.AARTIS, Icons.Outlined.LibraryMusic, "Aartis"),
          Triple(NativeTab.FESTIVALS, Icons.Outlined.CalendarMonth, "Festivals"),
          Triple(NativeTab.PRACTICE, Icons.Outlined.Spa, "Practice"),
        ).forEach { (tab, icon, label) ->
          NavigationBarItem(
            selected = selectedTab == tab,
            onClick = { selectedTab = tab },
            icon = { Icon(icon, contentDescription = label) },
            label = { Text(label) },
            colors = NavigationBarItemDefaults.colors(),
          )
        }
      }
    },
  ) { padding ->
    when (selectedTab) {
      NativeTab.TODAY -> DashboardScreen(
        state = state,
        contentPadding = padding,
        onTogglePractice = onTogglePractice,
        onIncrementJapa = onIncrementJapa,
        onOpenAartis = { selectedTab = NativeTab.AARTIS },
        onOpenFestivals = { selectedTab = NativeTab.FESTIVALS },
        alarms = state.ritualAlarms,
        onSaveAlarm = onSaveAlarm,
        onSetAlarmEnabled = onSetAlarmEnabled,
        onDeleteAlarm = onDeleteAlarm,
        onPlayFallbackTone = onPlayFallbackTone,
        onStopTonePreview = onStopTonePreview,
      )
      NativeTab.AARTIS -> AartiLibraryScreen(
        contentPadding = padding,
        favourites = state.favouriteIds,
        onToggleFavourite = onToggleFavourite,
      )
      NativeTab.FESTIVALS -> FestivalCalendarScreen(contentPadding = padding)
      NativeTab.PRACTICE -> PracticeScreen(
        contentPadding = padding,
        state = state,
        onTogglePractice = onTogglePractice,
        onIncrementJapa = onIncrementJapa,
        onResetJapa = onResetJapa,
      )
    }
  }
}
