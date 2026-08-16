package com.edu6tron.spiritualcompanion.nativepreview.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.WbTwilight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.edu6tron.spiritualcompanion.nativepreview.data.DevotionalRoutineAnchor
import com.edu6tron.spiritualcompanion.nativepreview.data.DevotionalRoutineDefinition
import com.edu6tron.spiritualcompanion.nativepreview.data.DevotionalRoutineStep
import com.edu6tron.spiritualcompanion.nativepreview.data.NativeCatalogue
import com.edu6tron.spiritualcompanion.nativepreview.data.NativeDevotionalRoutines
import com.edu6tron.spiritualcompanion.nativepreview.data.RoutineSpecialDayGuidance
import com.edu6tron.spiritualcompanion.nativepreview.panchang.PanchangCalculator
import com.edu6tron.spiritualcompanion.nativepreview.panchang.PanchangSnapshot
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.delay

/**
 * Offline-first personal routine guidance. It does not claim ritual authority and has no network
 * dependency; only user-entered city data is used to calculate the timing estimates.
 */
@Composable
fun DevotionalRoutineScreen(
  content: DashboardContentState,
  contentPadding: PaddingValues,
  onNavigateBack: () -> Unit,
  onOpenAarti: (String) -> Unit,
  onSaveEveningRoutineEnabled: (Boolean) -> Unit,
  onSaveBrahmaMuhurtaRoutineEnabled: (Boolean) -> Unit,
) {
  val now by produceState(initialValue = LocalDateTime.now()) {
    while (true) {
      value = LocalDateTime.now()
      delay(60_000L)
    }
  }
  val panchang = remember(content.savedLocation, now.toLocalDate()) {
    PanchangCalculator.calculate(now.toLocalDate(), content.savedLocation)
  }
  val specialGuidance = remember(panchang.tithi) {
    NativeDevotionalRoutines.specialDayGuidance(panchang)
  }
  var selectedRecitation by remember { mutableStateOf<DevotionalRoutineStep?>(null) }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(
      start = 20.dp,
      top = contentPadding.calculateTopPadding() + 12.dp,
      end = 20.dp,
      bottom = contentPadding.calculateBottomPadding() + 20.dp,
    ),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    item {
      RoutineHeader(onNavigateBack)
    }
    item {
      RoutineTimingCard(panchang)
    }
    item {
      RoutineDefinitionCard(
        definition = NativeDevotionalRoutines.eveningPrarthana,
        enabled = content.eveningRoutineEnabled,
        snapshot = panchang,
        onEnabledChange = onSaveEveningRoutineEnabled,
        onOpenAarti = onOpenAarti,
        onOpenRecitation = { selectedRecitation = it },
      )
    }
    item {
      RoutineDefinitionCard(
        definition = NativeDevotionalRoutines.brahmaMuhurta,
        enabled = content.brahmaMuhurtaRoutineEnabled,
        snapshot = panchang,
        onEnabledChange = onSaveBrahmaMuhurtaRoutineEnabled,
        onOpenAarti = onOpenAarti,
        onOpenRecitation = { selectedRecitation = it },
      )
    }
    specialGuidance?.let { guidance ->
      item {
        SpecialDayGuidanceCard(guidance = guidance, onOpenAarti = onOpenAarti)
      }
    }
    item {
      PersonalSuggestionCard()
    }
  }

  selectedRecitation?.let { step ->
    RoutineRecitationDialog(step = step, onDismiss = { selectedRecitation = null })
  }
}

@Composable
private fun RoutineHeader(onNavigateBack: () -> Unit) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    IconButton(onClick = onNavigateBack) {
      Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back to Today")
    }
    Column(modifier = Modifier.padding(start = 6.dp)) {
      Text("My routines", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
      Text("A quiet, timing-aware guide for your day", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}

@Composable
private fun RoutineTimingCard(snapshot: PanchangSnapshot) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
  ) {
    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Text("Today’s anchors", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
      TimingLine("Brahma Muhurta begins", snapshot.brahmaMuhurtaStart.routineTime())
      TimingLine("Sunset estimate", snapshot.sunset.routineTime())
      Text(
        if (snapshot.usesRecognisedCity) {
          "Offline estimate for ${snapshot.placeLabel}."
        } else {
          "Set a supported city in Aartis for a local offline estimate."
        },
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
      )
    }
  }
}

@Composable
private fun TimingLine(label: String, time: String) {
  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
    Text(label, color = MaterialTheme.colorScheme.onPrimaryContainer)
    Text(time, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
  }
}

@Composable
private fun RoutineDefinitionCard(
  definition: DevotionalRoutineDefinition,
  enabled: Boolean,
  snapshot: PanchangSnapshot,
  onEnabledChange: (Boolean) -> Unit,
  onOpenAarti: (String) -> Unit,
  onOpenRecitation: (DevotionalRoutineStep) -> Unit,
) {
  val anchorTime = when (definition.anchor) {
    DevotionalRoutineAnchor.SUNSET -> snapshot.sunset.routineTime()
    DevotionalRoutineAnchor.BRAHMA_MUHURTA -> snapshot.brahmaMuhurtaStart.routineTime()
  }
  val icon = when (definition.anchor) {
    DevotionalRoutineAnchor.SUNSET -> Icons.Outlined.WbTwilight
    DevotionalRoutineAnchor.BRAHMA_MUHURTA -> Icons.Outlined.NightsStay
  }
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
  ) {
    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(26.dp), tint = MaterialTheme.colorScheme.primary)
        Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
          Text(definition.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
          Text("${definition.anchor.title} · $anchorTime", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
        }
        Switch(checked = enabled, onCheckedChange = onEnabledChange)
      }
      Text(definition.timingNote, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      definition.steps.forEachIndexed { index, step ->
        RoutineStepRow(
          number = index + 1,
          step = step,
          onOpenAarti = onOpenAarti,
          onOpenRecitation = onOpenRecitation,
        )
      }
      Text(
        if (enabled) {
          "This personal routine is enabled on this device. Use the Today alarm controls to choose a separate exact reminder."
        } else {
          "Turn this on to save the routine as part of your personal daily plan."
        },
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}

@Composable
private fun RoutineStepRow(
  number: Int,
  step: DevotionalRoutineStep,
  onOpenAarti: (String) -> Unit,
  onOpenRecitation: (DevotionalRoutineStep) -> Unit,
) {
  Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
    Text(
      number.toString(),
      modifier = Modifier.size(28.dp).padding(top = 4.dp),
      style = MaterialTheme.typography.labelLarge,
      color = MaterialTheme.colorScheme.primary,
    )
    Column(modifier = Modifier.weight(1f)) {
      Text(step.title, fontWeight = FontWeight.SemiBold)
      Text(step.detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      when {
        step.aartiId != null -> TextButton(onClick = { onOpenAarti(step.aartiId) }) {
          Icon(Icons.AutoMirrored.Outlined.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(Modifier.size(6.dp))
          Text("Open lyrics")
        }
        step.recitationLines.isNotEmpty() -> TextButton(onClick = { onOpenRecitation(step) }) {
          Icon(Icons.AutoMirrored.Outlined.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(Modifier.size(6.dp))
          Text("Read prayer")
        }
      }
    }
  }
}

@Composable
private fun SpecialDayGuidanceCard(
  guidance: RoutineSpecialDayGuidance,
  onOpenAarti: (String) -> Unit,
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
  ) {
    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Text(guidance.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
      Text(guidance.detail, color = MaterialTheme.colorScheme.onTertiaryContainer)
      guidance.suggestedAartiIds.forEach { aartiId ->
        val title = NativeCatalogue.aartis.firstOrNull { it.id == aartiId }?.title ?: "Open local Aarti"
        TextButton(onClick = { onOpenAarti(aartiId) }) {
          Icon(Icons.AutoMirrored.Outlined.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(Modifier.size(6.dp))
          Text(title)
        }
      }
    }
  }
}

@Composable
private fun PersonalSuggestionCard() {
  Card(modifier = Modifier.fillMaxWidth()) {
    Text(
      "These are personal routine suggestions, not ritual instructions. Adapt the order, language, and duration to your tradition, family practice, and local published Panchang.",
      modifier = Modifier.padding(18.dp),
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}

@Composable
private fun RoutineRecitationDialog(step: DevotionalRoutineStep, onDismiss: () -> Unit) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(step.title) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Read at your own pace. This is a personal devotional reading guide.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        step.recitationLines.forEach { line ->
          Text(line, style = MaterialTheme.typography.titleMedium)
        }
      }
    },
    confirmButton = { TextButton(onClick = onDismiss) { Text("Close") } },
  )
}

private fun LocalTime?.routineTime(): String = this?.format(
  DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault()),
) ?: "Not available"
