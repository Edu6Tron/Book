package com.edu6tron.spiritualcompanion.nativepreview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.edu6tron.spiritualcompanion.nativepreview.data.DailyPractice
import com.edu6tron.spiritualcompanion.nativepreview.data.RitualAlarmEntity
import com.edu6tron.spiritualcompanion.nativepreview.util.PracticeMath
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
  state: DashboardUiState,
  contentPadding: PaddingValues,
  onTogglePractice: (String) -> Unit,
  onIncrementJapa: () -> Unit,
  onOpenAartis: () -> Unit,
  onOpenFestivals: () -> Unit,
  alarms: List<RitualAlarmEntity>,
  onSaveAlarm: (RitualAlarmEntity) -> Unit,
  onSetAlarmEnabled: (RitualAlarmEntity, Boolean) -> Unit,
  onDeleteAlarm: (RitualAlarmEntity) -> Unit,
  onPauseAlarm: (RitualAlarmEntity, Int) -> Unit,
  onResumeAlarm: (RitualAlarmEntity) -> Unit,
  onPlayFallbackTone: () -> Unit,
  onPreviewAlarmTone: (String?) -> Unit,
  onStopTonePreview: () -> Unit,
) {
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
    item { DashboardHeading() }
    item { PanchangHero() }
    item { TimingCard() }
    item {
      RitualAlarmSection(
        alarms = alarms,
        onSave = onSaveAlarm,
        onSetEnabled = onSetAlarmEnabled,
        onDelete = onDeleteAlarm,
        onPause = onPauseAlarm,
        onResume = onResumeAlarm,
        onPlayFallbackTone = onPlayFallbackTone,
        onPreviewTone = onPreviewAlarmTone,
        onStopTonePreview = onStopTonePreview,
      )
    }
    item { DailyEntryCard(state.japaCount, onIncrementJapa) }
    item {
      ExploreCard(
        onOpenAartis = onOpenAartis,
        onOpenFestivals = onOpenFestivals,
      )
    }
    item {
      PracticeCard(
        practices = state.practices,
        onTogglePractice = onTogglePractice,
      )
    }
  }
}

@Composable
private fun DashboardHeading() {
  Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
    Text("Spiritual Companion", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    Text("Your quiet daily space", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
  }
}

@Composable
private fun PanchangHero() {
  val now by produceState(initialValue = LocalDateTime.now()) {
    while (true) {
      value = LocalDateTime.now()
      delay(1_000)
    }
  }
  val paksha = if (now.dayOfMonth <= 15) "Shukla Paksha" else "Krishna Paksha"
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    shape = RoundedCornerShape(28.dp),
  ) {
    Column(
      modifier = Modifier.padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Icon(
        imageVector = Icons.Outlined.AutoAwesome,
        contentDescription = null,
        modifier = Modifier.size(28.dp),
        tint = MaterialTheme.colorScheme.primary,
      )
      Spacer(Modifier.height(8.dp))
      Text(
        text = now.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())),
        style = MaterialTheme.typography.displaySmall,
        fontWeight = FontWeight.Bold,
      )
      Text(
        text = now.format(DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale.getDefault())),
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
      )
      Spacer(Modifier.height(8.dp))
      AssistChip(onClick = {}, label = { Text(paksha) })
    }
  }
}

@Composable
private fun TimingCard() {
  Card(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.padding(18.dp)) {
      Text("Sacred windows", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
      Text("Indicative offline timings • configure a city for precise Panchang", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Spacer(Modifier.height(12.dp))
      TimingRow("Brahma Muhurta", "04:24 AM – 05:12 AM")
      TimingRow("Sunrise", "06:04 AM")
      TimingRow("Sunset", "06:42 PM")
      TimingRow("Nakshatra", "Rohini • Tithi 11")
    }
  }
}

@Composable
private fun TimingRow(label: String, time: String) {
  Row(
    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Text(time, fontWeight = FontWeight.SemiBold)
  }
}

@Composable
private fun DailyEntryCard(japaCount: Int, onIncrementJapa: () -> Unit) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
  ) {
    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Text("Japa today", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
      Text("$japaCount", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
      Text("${PracticeMath.remainingToNextMala(japaCount)} repetitions to your next 108", color = MaterialTheme.colorScheme.onSecondaryContainer)
      Button(onClick = onIncrementJapa, modifier = Modifier.fillMaxWidth()) { Text("Count one repetition") }
    }
  }
}

@Composable
private fun ExploreCard(onOpenAartis: () -> Unit, onOpenFestivals: () -> Unit) {
  ElevatedCard(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Text("Continue your practice", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
      Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedButton(onClick = onOpenAartis, modifier = Modifier.weight(1f)) {
          Icon(Icons.Outlined.LibraryMusic, contentDescription = null)
          Spacer(Modifier.size(6.dp))
          Text("Aartis")
        }
        OutlinedButton(onClick = onOpenFestivals, modifier = Modifier.weight(1f)) {
          Icon(Icons.Outlined.CalendarMonth, contentDescription = null)
          Spacer(Modifier.size(6.dp))
          Text("Festivals")
        }
      }
    }
  }
}

@Composable
private fun PracticeCard(
  practices: List<DailyPractice>,
  onTogglePractice: (String) -> Unit,
) {
  Card(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.padding(18.dp)) {
      Text("Today’s practice", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
      Spacer(Modifier.height(8.dp))
      practices.forEach { practice ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Checkbox(
            checked = practice.completed,
            onCheckedChange = { onTogglePractice(practice.id) },
          )
          Column(modifier = Modifier.weight(1f)) {
            Text(practice.title, textDecoration = if (practice.completed) androidx.compose.ui.text.style.TextDecoration.LineThrough else null)
            Text(practice.detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
          if (practice.completed) {
            Icon(
              imageVector = Icons.Outlined.CheckCircle,
              contentDescription = "Completed",
              tint = MaterialTheme.colorScheme.primary,
            )
          }
        }
      }
    }
  }
}
