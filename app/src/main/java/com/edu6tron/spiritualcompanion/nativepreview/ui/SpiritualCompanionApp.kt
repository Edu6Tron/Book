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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.edu6tron.spiritualcompanion.nativepreview.data.ReadingComfort
import com.edu6tron.spiritualcompanion.nativepreview.data.RitualAlarmEntity
import com.edu6tron.spiritualcompanion.nativepreview.data.ThemeMode
import com.edu6tron.spiritualcompanion.nativepreview.media.OfflineSoundscape

private enum class NativeTab(val title: String) {
  TODAY("Today"),
  AARTIS("Aartis"),
  DISCOVER("Discover"),
  FESTIVALS("Festivals"),
  PRACTICE("Practice"),
  SETTINGS("Settings"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpiritualCompanionApp(
  state: DashboardUiState,
  onTogglePractice: (String) -> Unit,
  onIncrementJapa: (Int) -> Unit,
  onResetJapa: () -> Unit,
  onToggleFavourite: (String) -> Unit,
  onSaveAlarm: (RitualAlarmEntity) -> Unit,
  onSetAlarmEnabled: (RitualAlarmEntity, Boolean) -> Unit,
  onDeleteAlarm: (RitualAlarmEntity) -> Unit,
  onPauseAlarm: (RitualAlarmEntity, Int) -> Unit,
  onResumeAlarm: (RitualAlarmEntity) -> Unit,
  onPlayFallbackTone: () -> Unit,
  onPlayOfflineSoundscape: (OfflineSoundscape) -> Unit,
  onPreviewAlarmTone: (String?) -> Unit,
  onStopTonePreview: () -> Unit,
  onSaveSelectedMedia: (String, String) -> Unit,
  onClearSelectedMedia: () -> Unit,
  onPlaySelectedMedia: (String, String) -> Unit,
  onSaveLocation: (String) -> Unit,
  onClearLocation: () -> Unit,
  onSaveReadingComfort: (ReadingComfort) -> Unit,
  onSaveThemeMode: (ThemeMode) -> Unit,
  onOpenNotificationSettings: () -> Unit,
  onOpenExactAlarmSettings: () -> Unit,
  onDismissNotice: () -> Unit,
) {
  var selectedTab by remember { mutableStateOf(NativeTab.TODAY) }
  val snackbarHostState = remember { SnackbarHostState() }
  LaunchedEffect(state.notice) {
    state.notice?.let { message ->
      snackbarHostState.showSnackbar(message)
      onDismissNotice()
    }
  }
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    bottomBar = {
      NavigationBar {
        listOf(
          Triple(NativeTab.TODAY, Icons.Outlined.Home, "Today"),
          Triple(NativeTab.AARTIS, Icons.Outlined.LibraryMusic, "Aartis"),
          Triple(NativeTab.DISCOVER, Icons.Outlined.AutoAwesome, "Discover"),
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
        content = state.content,
        contentPadding = padding,
        onTogglePractice = onTogglePractice,
        onIncrementJapa = { onIncrementJapa(1) },
        onOpenAartis = { selectedTab = NativeTab.AARTIS },
        onOpenFestivals = { selectedTab = NativeTab.FESTIVALS },
        onOpenDiscover = { selectedTab = NativeTab.DISCOVER },
        onOpenSettings = { selectedTab = NativeTab.SETTINGS },
        onOpenExactAlarmSettings = onOpenExactAlarmSettings,
        onSaveAlarm = onSaveAlarm,
        onSetAlarmEnabled = onSetAlarmEnabled,
        onDeleteAlarm = onDeleteAlarm,
        onPauseAlarm = onPauseAlarm,
        onResumeAlarm = onResumeAlarm,
        onPlayFallbackTone = onPlayFallbackTone,
        onPreviewAlarmTone = onPreviewAlarmTone,
        onStopTonePreview = onStopTonePreview,
      )
      NativeTab.AARTIS -> AartiLibraryScreen(
        contentPadding = padding,
        favourites = state.content.favouriteIds,
        onToggleFavourite = onToggleFavourite,
        selectedMediaUri = state.content.selectedMediaUri,
        selectedMediaLabel = state.content.selectedMediaLabel,
        onSaveSelectedMedia = onSaveSelectedMedia,
        onClearSelectedMedia = onClearSelectedMedia,
        onPlaySelectedMedia = onPlaySelectedMedia,
        playback = state.playback,
        onStopPlayback = onStopTonePreview,
        savedLocation = state.content.savedLocation,
        onSaveLocation = onSaveLocation,
        onClearLocation = onClearLocation,
      )
      NativeTab.DISCOVER -> DiscoverDevotionalsScreen(
        contentPadding = padding,
        onOpenAartis = { selectedTab = NativeTab.AARTIS },
      )
      NativeTab.FESTIVALS -> FestivalCalendarScreen(contentPadding = padding)
      NativeTab.PRACTICE -> PracticeScreen(
        contentPadding = padding,
        content = state.content,
        onTogglePractice = onTogglePractice,
        onIncrementJapa = onIncrementJapa,
        onResetJapa = onResetJapa,
        readingComfort = state.content.readingComfort,
        onSaveReadingComfort = onSaveReadingComfort,
        playback = state.playback,
        onPlayOfflineSoundscape = onPlayOfflineSoundscape,
        onStopPlayback = onStopTonePreview,
      )
      NativeTab.SETTINGS -> SettingsScreen(
        contentPadding = padding,
        themeMode = state.content.themeMode,
        readingComfort = state.content.readingComfort,
        onSaveThemeMode = onSaveThemeMode,
        onSaveReadingComfort = onSaveReadingComfort,
        onOpenNotificationSettings = onOpenNotificationSettings,
        onNavigateBack = { selectedTab = NativeTab.TODAY },
      )
    }
  }
}
