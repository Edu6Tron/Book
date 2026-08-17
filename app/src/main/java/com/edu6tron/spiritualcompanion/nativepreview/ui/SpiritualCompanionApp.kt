package com.edu6tron.spiritualcompanion.nativepreview.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.edu6tron.spiritualcompanion.nativepreview.data.DevotionalTheme
import com.edu6tron.spiritualcompanion.nativepreview.data.ReadingComfort
import com.edu6tron.spiritualcompanion.nativepreview.data.RitualAlarmEntity
import com.edu6tron.spiritualcompanion.nativepreview.data.ThemeMode
import com.edu6tron.spiritualcompanion.nativepreview.media.OfflineSoundscape
import com.edu6tron.spiritualcompanion.nativepreview.media.DevotionalPlaybackState

private enum class NativeTab(val title: String) {
  TODAY("Today"),
  AARTIS("Aartis"),
  DISCOVER("Discover"),
  FESTIVALS("Festivals"),
  PRACTICE("Practice"),
  SETTINGS("Settings"),
  ROUTINE("My routines"),
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
  onToggleSelectedMediaPlayback: () -> Unit,
  onSeekSelectedMediaTo: (Long) -> Unit,
  onSeekSelectedMediaBy: (Long) -> Unit,
  onSavePersonalLyricTiming: (String, List<Long>) -> Unit,
  onClearPersonalLyricTiming: (String) -> Unit,
  onSaveLocation: (String) -> Unit,
  onClearLocation: () -> Unit,
  onRefreshOnlineTimings: () -> Unit,
  onSaveReadingComfort: (ReadingComfort) -> Unit,
  onSaveThemeMode: (ThemeMode) -> Unit,
  onSaveDevotionalTheme: (DevotionalTheme) -> Unit,
  onSaveEveningRoutineEnabled: (Boolean) -> Unit,
  onSaveBrahmaMuhurtaRoutineEnabled: (Boolean) -> Unit,
  onSetRoutineStepCompleted: (String, String, Boolean) -> Unit,
  onResetRoutineProgress: (String) -> Unit,
  onOpenNotificationSettings: () -> Unit,
  onOpenExactAlarmSettings: () -> Unit,
  onDismissNotice: () -> Unit,
) {
  var selectedTab by remember { mutableStateOf(NativeTab.TODAY) }
  var requestedAartiId by remember { mutableStateOf<String?>(null) }
  var routineAlarmSuggestion by remember { mutableStateOf<RoutineAlarmSuggestion?>(null) }
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
      if (selectedTab != NativeTab.SETTINGS && selectedTab != NativeTab.ROUTINE) {
        Column {
          if (state.playback.isPlaying) {
            SharedPlaybackBar(playback = state.playback, onStop = onStopTonePreview)
          }
          NavigationBar(
          containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
          tonalElevation = 10.dp,
        ) {
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
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                selectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                indicatorColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer,
                unselectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
              ),
            )
          }
        }
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
        onOpenRoutines = { selectedTab = NativeTab.ROUTINE },
        onOpenSettings = { selectedTab = NativeTab.SETTINGS },
        onOpenExactAlarmSettings = onOpenExactAlarmSettings,
        onRefreshOnlineTimings = onRefreshOnlineTimings,
        timingRefreshInProgress = state.timingRefreshInProgress,
        onSaveAlarm = onSaveAlarm,
        onSetAlarmEnabled = onSetAlarmEnabled,
        onDeleteAlarm = onDeleteAlarm,
        onPauseAlarm = onPauseAlarm,
        onResumeAlarm = onResumeAlarm,
        onPlayFallbackTone = onPlayFallbackTone,
        onPreviewAlarmTone = onPreviewAlarmTone,
        onStopTonePreview = onStopTonePreview,
        routineAlarmSuggestion = routineAlarmSuggestion,
        onRoutineAlarmSuggestionConsumed = { routineAlarmSuggestion = null },
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
        onTogglePlayback = onToggleSelectedMediaPlayback,
        onSeekTo = onSeekSelectedMediaTo,
        onSeekBy = onSeekSelectedMediaBy,
        personalLyricTimingByAarti = state.content.personalLyricTimingByAarti,
        onSavePersonalLyricTiming = onSavePersonalLyricTiming,
        onClearPersonalLyricTiming = onClearPersonalLyricTiming,
        savedLocation = state.content.savedLocation,
        onSaveLocation = onSaveLocation,
        onClearLocation = onClearLocation,
        initialAartiId = requestedAartiId,
        onInitialAartiConsumed = { requestedAartiId = null },
      )
      NativeTab.DISCOVER -> DiscoverDevotionalsScreen(
        contentPadding = padding,
        onOpenAartis = { selectedTab = NativeTab.AARTIS },
      )
      NativeTab.FESTIVALS -> FestivalCalendarScreen(
        contentPadding = padding,
        savedLocation = state.content.savedLocation,
        onlineAstronomyCache = state.content.onlineAstronomyCache,
      )
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
        devotionalTheme = state.content.devotionalTheme,
        readingComfort = state.content.readingComfort,
        onSaveThemeMode = onSaveThemeMode,
        onSaveDevotionalTheme = onSaveDevotionalTheme,
        onSaveReadingComfort = onSaveReadingComfort,
        onOpenNotificationSettings = onOpenNotificationSettings,
        onNavigateBack = { selectedTab = NativeTab.TODAY },
      )
      NativeTab.ROUTINE -> DevotionalRoutineScreen(
        content = state.content,
        contentPadding = padding,
        onNavigateBack = { selectedTab = NativeTab.TODAY },
        onOpenAarti = { aartiId ->
          requestedAartiId = aartiId
          selectedTab = NativeTab.AARTIS
        },
        onSaveEveningRoutineEnabled = onSaveEveningRoutineEnabled,
        onSaveBrahmaMuhurtaRoutineEnabled = onSaveBrahmaMuhurtaRoutineEnabled,
        onSetRoutineStepCompleted = onSetRoutineStepCompleted,
        onResetRoutineProgress = onResetRoutineProgress,
        onCreateRoutineAlarm = { suggestion ->
          routineAlarmSuggestion = suggestion
          selectedTab = NativeTab.TODAY
        },
      )
    }
  }
}

@Composable
private fun SharedPlaybackBar(playback: DevotionalPlaybackState, onStop: () -> Unit) {
  Card(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
  ) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
      Text(
        "Playing on this device",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.secondary,
      )
      Text(
        playback.sourceLabel ?: "Local devotional audio",
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
      TextButton(onClick = onStop, modifier = Modifier.fillMaxWidth()) {
        Text("Stop playback")
      }
    }
  }
}
