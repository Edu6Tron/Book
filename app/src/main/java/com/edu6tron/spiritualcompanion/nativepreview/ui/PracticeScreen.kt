package com.edu6tron.spiritualcompanion.nativepreview.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.edu6tron.spiritualcompanion.nativepreview.R
import com.edu6tron.spiritualcompanion.nativepreview.data.DailyGuidance
import com.edu6tron.spiritualcompanion.nativepreview.data.NativeDailyGuidance
import com.edu6tron.spiritualcompanion.nativepreview.data.NativeScriptureReflection
import com.edu6tron.spiritualcompanion.nativepreview.data.ReadingComfort
import com.edu6tron.spiritualcompanion.nativepreview.data.ScriptureReflection
import com.edu6tron.spiritualcompanion.nativepreview.media.DevotionalPlaybackState
import com.edu6tron.spiritualcompanion.nativepreview.media.OfflineSoundscape

@Composable
fun PracticeScreen(
  contentPadding: PaddingValues,
  content: DashboardContentState,
  onTogglePractice: (String) -> Unit,
  onIncrementJapa: (Int) -> Unit,
  onResetJapa: () -> Unit,
  readingComfort: ReadingComfort,
  onSaveReadingComfort: (ReadingComfort) -> Unit,
  playback: DevotionalPlaybackState,
  onPlayOfflineSoundscape: (OfflineSoundscape) -> Unit,
  onStopPlayback: () -> Unit,
) {
  val dailyGuidance = remember { NativeDailyGuidance.forToday() }
  val scriptureReflection = remember { NativeScriptureReflection.forToday() }
  var showReadingComfort by remember { mutableStateOf(false) }
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(
      start = 20.dp,
      top = contentPadding.calculateTopPadding() + 16.dp,
      end = 20.dp,
      bottom = contentPadding.calculateBottomPadding() + 20.dp,
    ),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    item {
      Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text("Daily practice", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Small repeatable acts, kept on this device", color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
    item(contentType = "daily-guidance") { DailyGuidanceCard(dailyGuidance) }
    item(contentType = "scripture-reflection") { ScriptureReflectionCard(scriptureReflection) }
    item(contentType = "soundscape") {
      SacredDawnSoundscapeCard(
        playback = playback,
        onPlaySoundscape = onPlayOfflineSoundscape,
        onStop = onStopPlayback,
      )
    }
    item(contentType = "japa") { JapaCounterCard(content.japaCount, onIncrementJapa, onResetJapa) }
    item(contentType = "reading-comfort") {
      ReadingComfortCard(readingComfort = readingComfort, onOpen = { showReadingComfort = true })
    }
    item { Text("Today’s checklist", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold) }
    items(content.practices, key = { it.id }, contentType = { "practice" }) { practice ->
      Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
          Checkbox(checked = practice.completed, onCheckedChange = { onTogglePractice(practice.id) })
          Spacer(Modifier.size(8.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(practice.title, fontWeight = FontWeight.Medium)
            Text(practice.detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    }
  }
  if (showReadingComfort) {
    ReadingComfortDialog(
      selected = readingComfort,
      onSelect = {
        onSaveReadingComfort(it)
        showReadingComfort = false
      },
      onDismiss = { showReadingComfort = false },
    )
  }
}

@Composable
private fun DailyGuidanceCard(guidance: DailyGuidance) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
  ) {
    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
      Text("Daily reflection", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSecondaryContainer)
      Text(guidance.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
      Text(guidance.reflection, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
      Text("Try this: ${guidance.smallAction}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSecondaryContainer)
    }
  }
}

@Composable
private fun ScriptureReflectionCard(reflection: ScriptureReflection) {
  Card(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
      Text("Scripture reflection", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
      Text(reflection.theme, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
      Text(reflection.reflection, style = MaterialTheme.typography.bodyMedium)
      Text("Try this: ${reflection.smallAction}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Text(reflection.source, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Text("A brief reflection, not an authoritative translation or ritual instruction.", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}

@Composable
private fun SacredDawnSoundscapeCard(
  playback: DevotionalPlaybackState,
  onPlaySoundscape: (OfflineSoundscape) -> Unit,
  onStop: () -> Unit,
) {
  Card(modifier = Modifier.fillMaxWidth()) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Box(modifier = Modifier.fillMaxWidth().height(132.dp)) {
        Image(
          painter = painterResource(R.drawable.sacred_dawn_mandala),
          contentDescription = "Sunrise temple illustration for Sacred dawn ambience",
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop,
        )
      }
      Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Text("Offline soundscapes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text(
          "Three original, device-bundled soundscapes for japa and reflection. They never replace your alarm tone.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        OfflineSoundscape.entries.forEach { soundscape ->
          val isThisSoundscape = playback.sourceLabel == soundscape.playbackLabel
          Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(soundscape.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Text(soundscape.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Button(onClick = { onPlaySoundscape(soundscape) }) {
                Text(if (isThisSoundscape && playback.isPlaying) "Restart" else "Play")
              }
              if (isThisSoundscape && (playback.isPlaying || playback.positionMs > 0L)) {
                OutlinedButton(onClick = onStop) { Text("Stop") }
              }
            }
            if (isThisSoundscape) {
              Text(
                if (playback.isPlaying) "Playing offline on this device" else playback.message ?: "Ready offline",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun ReadingComfortCard(readingComfort: ReadingComfort, onOpen: () -> Unit) {
  Card(modifier = Modifier.fillMaxWidth()) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
      Icon(Icons.Outlined.Settings, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
      Spacer(Modifier.size(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text("Reading comfort", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text("Text size: ${readingComfort.label}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
      OutlinedButton(onClick = onOpen) { Text("Adjust") }
    }
  }
}

@Composable
private fun ReadingComfortDialog(
  selected: ReadingComfort,
  onSelect: (ReadingComfort) -> Unit,
  onDismiss: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Reading comfort") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Choose the text size that is most comfortable for reading Aartis, festival notes, and daily guidance.")
        ReadingComfort.entries.forEach { option ->
          FilterChip(
            selected = selected == option,
            onClick = { onSelect(option) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(option.label) },
          )
        }
      }
    },
    confirmButton = { TextButton(onClick = onDismiss) { Text("Done") } },
  )
}

@Composable
private fun JapaCounterCard(count: Int, onIncrement: (Int) -> Unit, onReset: () -> Unit) {
  val currentMala = count % 108
  val completedMalas = count / 108
  val remaining = if (currentMala == 0) 108 else 108 - currentMala
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
  ) {
    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Icon(Icons.Outlined.TouchApp, contentDescription = null, modifier = Modifier.size(30.dp), tint = MaterialTheme.colorScheme.primary)
      Text("$count", style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold)
      Text("Japa repetitions today", color = MaterialTheme.colorScheme.onPrimaryContainer)
      LinearProgressIndicator(
        progress = { currentMala / 108f },
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary,
        trackColor = MaterialTheme.colorScheme.surface,
      )
      Text(
        if (completedMalas == 0) "$remaining repetitions to complete this mala" else "$completedMalas mala${if (completedMalas == 1) "" else "s"} complete • $remaining to the next",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
      )
      Button(onClick = { onIncrement(1) }, modifier = Modifier.fillMaxWidth()) { Text("Count one repetition") }
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(onClick = { onIncrement(11) }, modifier = Modifier.weight(1f)) { Text("+11") }
        OutlinedButton(onClick = { onIncrement(108) }, modifier = Modifier.weight(1f)) { Text("Complete mala") }
      }
      OutlinedButton(onClick = onReset, modifier = Modifier.fillMaxWidth()) {
        Icon(Icons.Outlined.Refresh, contentDescription = null)
        Spacer(Modifier.size(6.dp))
        Text("Reset today’s count")
      }
    }
  }
}
