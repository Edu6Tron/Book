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
      SpiritualCompanionNativeTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
          SpiritualCompanionRoot()
        }
      }
    }
  }
}

@Composable
private fun SpiritualCompanionRoot(viewModel: DashboardViewModel = hiltViewModel()) {
  val state by viewModel.state.collectAsStateWithLifecycle()
  SpiritualCompanionApp(
    state = state,
    onTogglePractice = viewModel::togglePractice,
    onIncrementJapa = viewModel::incrementJapa,
    onResetJapa = viewModel::resetJapa,
    onToggleFavourite = viewModel::toggleFavourite,
    onSaveAlarm = viewModel::saveAlarm,
    onSetAlarmEnabled = viewModel::setAlarmEnabled,
    onDeleteAlarm = viewModel::deleteAlarm,
    onPlayFallbackTone = viewModel::playFallbackTone,
    onStopTonePreview = viewModel::stopTonePreview,
  )
}
