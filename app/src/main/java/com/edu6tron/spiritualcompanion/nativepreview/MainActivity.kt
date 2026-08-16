package com.edu6tron.spiritualcompanion.nativepreview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.edu6tron.spiritualcompanion.nativepreview.ui.SpiritualCompanionApp
import com.edu6tron.spiritualcompanion.nativepreview.ui.DashboardViewModel
import com.edu6tron.spiritualcompanion.nativepreview.ui.SpiritualCompanionNativeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      SpiritualCompanionRoot()
    }
  }
}

@Composable
private fun SpiritualCompanionRoot(viewModel: DashboardViewModel = hiltViewModel()) {
  val state by viewModel.state.collectAsStateWithLifecycle()
  SpiritualCompanionNativeTheme(
    readingComfort = state.content.readingComfort,
    themeMode = state.content.themeMode,
  ) {
    Surface(color = MaterialTheme.colorScheme.background) {
      SpiritualCompanionApp(
        state = state,
        onTogglePractice = viewModel::togglePractice,
        onIncrementJapa = viewModel::incrementJapa,
        onResetJapa = viewModel::resetJapa,
        onToggleFavourite = viewModel::toggleFavourite,
        onSaveAlarm = viewModel::saveAlarm,
        onSetAlarmEnabled = viewModel::setAlarmEnabled,
        onDeleteAlarm = viewModel::deleteAlarm,
        onPauseAlarm = viewModel::pauseAlarm,
        onResumeAlarm = viewModel::resumeAlarm,
        onPlayFallbackTone = viewModel::playFallbackTone,
        onPlayOfflineSoundscape = viewModel::playOfflineSoundscape,
        onPreviewAlarmTone = viewModel::previewAlarmTone,
        onStopTonePreview = viewModel::stopTonePreview,
        onSaveSelectedMedia = viewModel::saveSelectedMedia,
        onClearSelectedMedia = viewModel::clearSelectedMedia,
        onPlaySelectedMedia = viewModel::playSelectedMedia,
        onSaveLocation = viewModel::saveLocation,
        onClearLocation = viewModel::clearLocation,
        onSaveReadingComfort = viewModel::saveReadingComfort,
        onSaveThemeMode = viewModel::saveThemeMode,
        onSaveEveningRoutineEnabled = viewModel::saveEveningRoutineEnabled,
        onSaveBrahmaMuhurtaRoutineEnabled = viewModel::saveBrahmaMuhurtaRoutineEnabled,
        onSetRoutineStepCompleted = viewModel::setRoutineStepCompleted,
        onResetRoutineProgress = viewModel::resetRoutineProgress,
        onOpenNotificationSettings = viewModel::openNotificationSettings,
        onOpenExactAlarmSettings = viewModel::openExactAlarmSettings,
        onDismissNotice = viewModel::dismissNotice,
      )
    }
  }
}
