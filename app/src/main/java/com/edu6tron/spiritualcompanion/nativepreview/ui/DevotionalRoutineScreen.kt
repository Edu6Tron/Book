package com.edu6tron.spiritualcompanion.nativepreview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edu6tron.spiritualcompanion.nativepreview.data.DevotionalRoutineAnchor
import com.edu6tron.spiritualcompanion.nativepreview.data.DevotionalRoutineDefinition
import com.edu6tron.spiritualcompanion.nativepreview.data.DevotionalRoutineStep
import com.edu6tron.spiritualcompanion.nativepreview.data.NativeCatalogue
import com.edu6tron.spiritualcompanion.nativepreview.data.NativeDevotionalRoutines
import com.edu6tron.spiritualcompanion.nativepreview.data.RoutineDailyProgress
import com.edu6tron.spiritualcompanion.nativepreview.data.RoutineSpecialDayGuidance
import com.edu6tron.spiritualcompanion.nativepreview.panchang.PanchangCalculator
import com.edu6tron.spiritualcompanion.nativepreview.panchang.PanchangSnapshot
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.delay

/**
 * Offline-first personal routine guidance. It keeps timing, progress, and readings on-device,
 * and intentionally does not present itself as an authority on ritual observance.
 */
@Composable
fun DevotionalRoutineScreen(
  content: DashboardContentState,
  contentPadding: PaddingValues,
  onNavigateBack: () -> Unit,
  onOpenAarti: (String) -> Unit,
  onSaveEveningRoutineEnabled: (Boolean) -> Unit,
  onSaveBrahmaMuhurtaRoutineEnabled: (Boolean) -> Unit,
  onSetRoutineStepCompleted: (String, String, Boolean) -> Unit,
  onResetRoutineProgress: (String) -> Unit,
  onOpenAlarmTools: () -> Unit,
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
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    item { RoutineHeader(onNavigateBack) }
    item { RoutineTimingCard(snapshot = panchang, now = now, onOpenAlarmTools = onOpenAlarmTools) }
    item {
      RoutineDefinitionCard(
        definition = NativeDevotionalRoutines.eveningPrarthana,
        enabled = content.eveningRoutineEnabled,
        progress = content.eveningRoutineProgress,
        snapshot = panchang,
        today = now.toLocalDate(),
        now = now,
        onEnabledChange = onSaveEveningRoutineEnabled,
        onSetStepCompleted = { stepId, completed ->
          onSetRoutineStepCompleted(NativeDevotionalRoutines.eveningPrarthana.id, stepId, completed)
        },
        onResetProgress = { onResetRoutineProgress(NativeDevotionalRoutines.eveningPrarthana.id) },
        onOpenAarti = onOpenAarti,
        onOpenRecitation = { selectedRecitation = it },
      )
    }
    item {
      RoutineDefinitionCard(
        definition = NativeDevotionalRoutines.brahmaMuhurta,
        enabled = content.brahmaMuhurtaRoutineEnabled,
        progress = content.brahmaMuhurtaRoutineProgress,
        snapshot = panchang,
        today = now.toLocalDate(),
        now = now,
        onEnabledChange = onSaveBrahmaMuhurtaRoutineEnabled,
        onSetStepCompleted = { stepId, completed ->
          onSetRoutineStepCompleted(NativeDevotionalRoutines.brahmaMuhurta.id, stepId, completed)
        },
        onResetProgress = { onResetRoutineProgress(NativeDevotionalRoutines.brahmaMuhurta.id) },
        onOpenAarti = onOpenAarti,
        onOpenRecitation = { selectedRecitation = it },
      )
    }
    specialGuidance?.let { guidance ->
      item { SpecialDayGuidanceCard(guidance = guidance, onOpenAarti = onOpenAarti) }
    }
    item { PersonalSuggestionCard() }
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
    FilledTonalIconButton(onClick = onNavigateBack) {
      Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back to Today")
    }
    Column(modifier = Modifier.padding(start = 12.dp)) {
      Text(
        "DAILY DEVOTIONAL RHYTHM",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        letterSpacing = 0.8.sp,
      )
      Text("My routines", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
      Text("A quiet, timing-aware guide for your day", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}

@Composable
private fun RoutineTimingCard(
  snapshot: PanchangSnapshot,
  now: LocalDateTime,
  onOpenAlarmTools: () -> Unit,
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    shape = RoundedCornerShape(30.dp),
  ) {
    Column(
      modifier = Modifier
        .background(
          Brush.linearGradient(
            listOf(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.secondaryContainer),
          ),
        )
        .padding(20.dp),
      verticalArrangement = Arrangement.spacedBy(9.dp),
    ) {
      Text("Today’s anchors", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
      TimingLine("Brahma Muhurta begins", snapshot.brahmaMuhurtaStart.routineTime())
      TimingLine("Sunset estimate", snapshot.sunset.routineTime())
      Text(
        nextRoutineMoment(snapshot, now),
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
      )
      Text(
        if (snapshot.usesRecognisedCity) {
          "Offline estimate for ${snapshot.placeLabel}."
        } else {
          "Set a supported city in Aartis for a local offline estimate."
        },
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
      )
      TextButton(onClick = onOpenAlarmTools) { Text("Open reminder controls") }
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
  progress: RoutineDailyProgress,
  snapshot: PanchangSnapshot,
  today: LocalDate,
  now: LocalDateTime,
  onEnabledChange: (Boolean) -> Unit,
  onSetStepCompleted: (String, Boolean) -> Unit,
  onResetProgress: () -> Unit,
  onOpenAarti: (String) -> Unit,
  onOpenRecitation: (DevotionalRoutineStep) -> Unit,
) {
  val anchorTime = when (definition.anchor) {
    DevotionalRoutineAnchor.SUNSET -> snapshot.sunset
    DevotionalRoutineAnchor.BRAHMA_MUHURTA -> snapshot.brahmaMuhurtaStart
  }
  val icon = when (definition.anchor) {
    DevotionalRoutineAnchor.SUNSET -> Icons.Outlined.WbTwilight
    DevotionalRoutineAnchor.BRAHMA_MUHURTA -> Icons.Outlined.NightsStay
  }
  val completedSteps = progress.completedStepsFor(today)
  val completedCount = definition.steps.count { it.id in completedSteps }
  val isComplete = completedCount == definition.steps.size
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
      containerColor = if (definition.anchor == DevotionalRoutineAnchor.SUNSET) {
        MaterialTheme.colorScheme.surfaceContainerLow
      } else {
        MaterialTheme.colorScheme.secondaryContainer
      },
    ),
    shape = RoundedCornerShape(28.dp),
  ) {
    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(13.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(26.dp), tint = MaterialTheme.colorScheme.primary)
        Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
          Text(definition.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
          Text(
            "${definition.anchor.title} · ${anchorTime.routineTime()} · ${routineMomentStatus(anchorTime, now)}",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge,
          )
        }
        Switch(checked = enabled, onCheckedChange = onEnabledChange)
      }
      Text(definition.timingNote, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      LinearProgressIndicator(
        progress = { completedCount.toFloat() / definition.steps.size.coerceAtLeast(1) },
        modifier = Modifier.fillMaxWidth(),
      )
      Text(
        if (isComplete) {
          "All ${definition.steps.size} steps are marked for today. Keep the rest of this moment unhurried."
        } else {
          "$completedCount of ${definition.steps.size} steps marked for today. Mark a step only when it serves your own practice."
        },
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
      definition.steps.forEachIndexed { index, step ->
        RoutineStepRow(
          number = index + 1,
          step = step,
          completed = step.id in completedSteps,
          onCompletedChange = { onSetStepCompleted(step.id, it) },
          onOpenAarti = onOpenAarti,
          onOpenRecitation = onOpenRecitation,
        )
      }
      if (completedSteps.isNotEmpty()) {
        OutlinedButton(onClick = onResetProgress, modifier = Modifier.fillMaxWidth()) {
          Text("Clear today’s marks")
        }
      }
      Text(
        if (enabled) {
          "This routine is enabled on this device. Create or adjust its separate exact reminder in Today."
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
  completed: Boolean,
  onCompletedChange: (Boolean) -> Unit,
  onOpenAarti: (String) -> Unit,
  onOpenRecitation: (DevotionalRoutineStep) -> Unit,
) {
  Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
    Checkbox(checked = completed, onCheckedChange = onCompletedChange)
    Surface(
      modifier = Modifier.padding(start = 4.dp, end = 8.dp),
      shape = RoundedCornerShape(10.dp),
      color = if (completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
    ) {
      Text(
        number.toString(),
        modifier = Modifier.size(28.dp).padding(top = 5.dp),
        style = MaterialTheme.typography.labelLarge,
        color = if (completed) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
      )
    }
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
    shape = RoundedCornerShape(28.dp),
  ) {
    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    shape = RoundedCornerShape(24.dp),
  ) {
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

private fun nextRoutineMoment(snapshot: PanchangSnapshot, now: LocalDateTime): String {
  val candidates = listOfNotNull(
    snapshot.brahmaMuhurtaStart?.let { it to "Brahma Muhurta" },
    snapshot.sunset?.let { it to "Evening Prarthana" },
  )
  val next = candidates.minByOrNull { (time, _) -> durationUntilNext(time, now) }
    ?: return "Choose a supported city to see the next timing."
  return "Next: ${next.second} ${routineMomentStatus(next.first, now)}"
}

private fun routineMomentStatus(anchor: LocalTime?, now: LocalDateTime): String {
  anchor ?: return "timing unavailable"
  val remaining = durationUntilNext(anchor, now)
  return when {
    remaining <= 1L -> "is due now"
    remaining < 60L -> "in $remaining min"
    remaining < 24 * 60L -> "in ${remaining / 60}h ${remaining % 60}m"
    else -> "tomorrow"
  }
}

private fun durationUntilNext(anchor: LocalTime, now: LocalDateTime): Long {
  val scheduledToday = now.toLocalDate().atTime(anchor)
  val next = if (scheduledToday.isAfter(now)) scheduledToday else scheduledToday.plusDays(1)
  return Duration.between(now, next).toMinutes().coerceAtLeast(0L)
}

private fun LocalTime?.routineTime(): String = this?.format(
  DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault()),
) ?: "Not available"
