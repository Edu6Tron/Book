package com.edu6tron.spiritualcompanion.nativepreview.ui

import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.PauseCircleOutline
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.edu6tron.spiritualcompanion.nativepreview.alarm.RitualAlarmScheduler
import com.edu6tron.spiritualcompanion.nativepreview.alarm.RitualAlarmTiming
import com.edu6tron.spiritualcompanion.nativepreview.data.RitualAlarmEntity
import com.edu6tron.spiritualcompanion.nativepreview.data.days
import com.edu6tron.spiritualcompanion.nativepreview.data.isPaused
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import java.util.UUID

private val dayLabels = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

@Composable
fun RitualAlarmSection(
  alarms: List<RitualAlarmEntity>,
  onSave: (RitualAlarmEntity) -> Unit,
  onSetEnabled: (RitualAlarmEntity, Boolean) -> Unit,
  onDelete: (RitualAlarmEntity) -> Unit,
  onPause: (RitualAlarmEntity, Int) -> Unit,
  onResume: (RitualAlarmEntity) -> Unit,
  onPlayFallbackTone: () -> Unit,
  onPreviewTone: (String?) -> Unit,
  onStopTonePreview: () -> Unit,
  routineAlarmSuggestion: RoutineAlarmSuggestion?,
  onRoutineAlarmSuggestionConsumed: () -> Unit,
) {
  var editorFor by remember { mutableStateOf<RitualAlarmEntity?>(null) }
  var creating by remember { mutableStateOf(false) }
  var suggestionFor by remember { mutableStateOf<RoutineAlarmSuggestion?>(null) }
  var pausing by remember { mutableStateOf<RitualAlarmEntity?>(null) }
  val context = LocalContext.current
  LaunchedEffect(routineAlarmSuggestion) {
    routineAlarmSuggestion?.let { suggestion ->
      editorFor = null
      creating = false
      suggestionFor = suggestion
      onRoutineAlarmSuggestionConsumed()
    }
  }
  Card(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Outlined.Alarm, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text("Ritual alarms", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
          Text("Exact schedule • chosen local tone • full-screen Snooze and Stop", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        IconButton(onClick = { creating = true }) { Icon(Icons.Outlined.Alarm, contentDescription = "Add ritual alarm") }
      }
      if (!RitualAlarmScheduler.canScheduleExactAlarms(context)) {
        Text("Allow Alarms & reminders for reliable screen-off timing. Until then Android may defer a ritual alarm.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
        OutlinedButton(onClick = {
          val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:${context.packageName}"))
          if (intent.resolveActivity(context.packageManager) != null) context.startActivity(intent)
        }, modifier = Modifier.fillMaxWidth()) { Text("Allow exact alarms") }
      }
      val batteryOptimizationsIgnored = remember(context) {
        context.getSystemService(PowerManager::class.java)
          .isIgnoringBatteryOptimizations(context.packageName)
      }
      if (!batteryOptimizationsIgnored) {
        Text(
          "For reliable screen-off alarms, allow Spiritual Companion to run without battery restriction. This does not enable background internet activity.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.error,
        )
        OutlinedButton(onClick = {
          val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
          if (intent.resolveActivity(context.packageManager) != null) context.startActivity(intent)
        }, modifier = Modifier.fillMaxWidth()) { Text("Open battery settings") }
        Text(
          "On Realme devices, also set Battery usage to Unrestricted or allow background activity for Spiritual Companion.",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      if (alarms.isEmpty()) {
        Text("No ritual alarm yet. Add a Brahma Muhurta reminder and choose a local tone if you prefer one.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Button(onClick = { creating = true }, modifier = Modifier.fillMaxWidth()) { Text("Add Brahma Muhurta alarm") }
      } else {
        alarms.forEach { alarm ->
          val nextScheduledAt = RitualAlarmTiming.nextScheduledAt(alarm)
          Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
              Text("%02d:%02d".format(alarm.hour, alarm.minute), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
              Text(alarm.label, style = MaterialTheme.typography.bodyMedium)
              Text("${if (alarm.days().size == 7) "Every day" else "${alarm.days().size} days selected"} • ${alarmToneSummary(alarm.toneUri)}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              if (alarm.isPaused()) {
                Text("Paused until ${DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(alarm.pauseUntilMillis!!))}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
              } else if (!alarm.enabled) {
                Text("Turned off", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              } else if (nextScheduledAt != null) {
                Text("Next: ${DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(nextScheduledAt))}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
              }
            }
            Switch(
              checked = alarm.enabled,
              onCheckedChange = { onSetEnabled(alarm, it) },
              modifier = Modifier.semantics {
                contentDescription = "Enable ${alarm.label}"
                stateDescription = if (alarm.enabled) "Enabled" else "Disabled"
              },
            )
            IconButton(onClick = { if (alarm.isPaused()) onResume(alarm) else pausing = alarm }) {
              Icon(Icons.Outlined.PauseCircleOutline, contentDescription = if (alarm.isPaused()) "Resume ${alarm.label}" else "Pause ${alarm.label}")
            }
            IconButton(onClick = { editorFor = alarm }) { Icon(Icons.Outlined.Alarm, contentDescription = "Edit ${alarm.label}") }
            IconButton(onClick = { onDelete(alarm) }) { Icon(Icons.Outlined.DeleteOutline, contentDescription = "Delete ${alarm.label}") }
          }
        }
      }
      OutlinedButton(onClick = onPlayFallbackTone, modifier = Modifier.fillMaxWidth()) {
        Icon(Icons.Outlined.PlayCircleOutline, contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text("Preview bundled offline devotional tone")
      }
      TextButton(onClick = onStopTonePreview, modifier = Modifier.align(Alignment.End)) { Text("Stop preview") }
    }
  }
  if (creating || editorFor != null || suggestionFor != null) {
    AlarmEditorDialog(
      initial = editorFor,
      suggestion = suggestionFor,
      onDismiss = { creating = false; editorFor = null; suggestionFor = null },
      onPreviewTone = onPreviewTone,
      onStopTonePreview = onStopTonePreview,
      onSave = { onSave(it); creating = false; editorFor = null; suggestionFor = null },
    )
  }
  pausing?.let { alarm ->
    AlertDialog(
      onDismissRequest = { pausing = null },
      title = { Text("Pause ${alarm.label}") },
      text = { Text("Choose how long this ritual alarm should remain paused. It resumes automatically afterwards.") },
      confirmButton = { Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) { listOf(1, 3, 7).forEach { days -> TextButton(onClick = { onPause(alarm, days); pausing = null }) { Text("$days day${if (days == 1) "" else "s"}") } } } },
      dismissButton = { TextButton(onClick = { pausing = null }) { Text("Cancel") } },
    )
  }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AlarmEditorDialog(
  initial: RitualAlarmEntity?,
  suggestion: RoutineAlarmSuggestion?,
  onDismiss: () -> Unit,
  onPreviewTone: (String?) -> Unit,
  onStopTonePreview: () -> Unit,
  onSave: (RitualAlarmEntity) -> Unit,
) {
  var label by remember(initial, suggestion) { mutableStateOf(initial?.label ?: suggestion?.alarmLabel ?: "Brahma Muhurta") }
  val initialCalendar = remember(initial, suggestion) { Calendar.getInstance() }
  var hour by remember(initial, suggestion) { mutableStateOf(initial?.hour ?: suggestion?.time?.hour ?: initialCalendar.get(Calendar.HOUR_OF_DAY)) }
  var minute by remember(initial, suggestion) { mutableStateOf(initial?.minute ?: suggestion?.time?.minute ?: initialCalendar.get(Calendar.MINUTE)) }
  var selectedDays by remember(initial, suggestion) { mutableStateOf((initial?.days() ?: (0..6).toList()).toSet()) }
  var toneUri by remember(initial, suggestion) { mutableStateOf(initial?.toneUri) }
  var invalidTime by remember(initial, suggestion) { mutableStateOf(false) }
  var showTimePicker by remember(initial, suggestion) { mutableStateOf(false) }
  val context = LocalContext.current
  val tonePicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
    uri ?: return@rememberLauncherForActivityResult
    runCatching { context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION) }
    toneUri = uri.toString()
  }
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(if (initial != null) "Edit ritual alarm" else suggestion?.editorTitle ?: "New ritual alarm") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        suggestion?.let { timing ->
          Card(colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
              Text("Suggested from today’s offline timing", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
              Text("${timing.context.anchorDescription}: ${timing.time.displayText()}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
              Text(
                "This is a starting point. Confirm the saved clock time and review it as seasonal timings change.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
              )
            }
          }
        }
        OutlinedTextField(label, { label = it }, label = { Text("Label") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Text("Time", style = MaterialTheme.typography.labelLarge)
        OutlinedButton(
          onClick = { showTimePicker = true },
          modifier = Modifier.fillMaxWidth().semantics {
            contentDescription = "Choose alarm time, currently ${AlarmTimeSelection(hour, minute).displayText()}"
          },
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(AlarmTimeSelection(hour, minute).displayText(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Tap to choose time", style = MaterialTheme.typography.labelMedium)
          }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
          TextButton(onClick = {
            val now = AlarmTimeSelection.from(Calendar.getInstance())
            hour = now.hour
            minute = now.minute
          }) { Text("Use current time") }
          TextButton(onClick = { hour = AlarmTimeSelection.brahmaMuhurta.hour; minute = AlarmTimeSelection.brahmaMuhurta.minute }) { Text("Brahma Muhurta") }
          suggestion?.let { timing ->
            TextButton(onClick = { hour = timing.time.hour; minute = timing.time.minute }) { Text("Use suggestion") }
          }
        }
        Text("Repeat on", style = MaterialTheme.typography.labelLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
          dayLabels.forEachIndexed { index, day ->
            FilterChip(
              selected = index in selectedDays,
              onClick = { selectedDays = if (index in selectedDays) selectedDays - index else selectedDays + index },
              modifier = Modifier.semantics { contentDescription = "Repeat on $day" },
              label = { Text(day.first().toString()) },
            )
          }
        }
        Text("Choose media or tone to play", style = MaterialTheme.typography.labelLarge)
        Text(alarmToneSummary(toneUri), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedButton(onClick = { tonePicker.launch(arrayOf("audio/*")) }) { Text(if (toneUri == null) "Choose local tone" else "Change tone") }
          TextButton(onClick = { toneUri = null }) { Text("Use bundled") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          TextButton(onClick = { onPreviewTone(toneUri) }) { Text("Preview") }
          TextButton(onClick = onStopTonePreview) { Text("Stop preview") }
        }
        Text("Online provider media is not used as an alarm tone. If your selected local file becomes unavailable, the bundled devotional chime plays instead.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (invalidTime) Text("Enter an hour from 0–23, minute from 0–59, and select at least one day.", color = MaterialTheme.colorScheme.error)
      }
    },
    confirmButton = {
      Button(onClick = {
        if (hour !in 0..23 || minute !in 0..59 || selectedDays.isEmpty()) {
          invalidTime = true
        } else {
          onSave(RitualAlarmEntity(id = initial?.id ?: UUID.randomUUID().toString(), label = label.trim().ifBlank { "Ritual alarm" }, hour = hour, minute = minute, repeatDays = selectedDays.sorted().joinToString(","), enabled = initial?.enabled ?: true, toneUri = toneUri, afterAlertAartiId = initial?.afterAlertAartiId))
        }
      }) { Text("Save") }
    },
    dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
  )
  if (showTimePicker) {
    key(hour, minute) {
      val pickerState = rememberTimePickerState(initialHour = hour, initialMinute = minute, is24Hour = true)
      AlertDialog(
        onDismissRequest = { showTimePicker = false },
        title = { Text("Choose alarm time") },
        text = { TimePicker(state = pickerState) },
        confirmButton = {
          TextButton(onClick = {
            hour = pickerState.hour
            minute = pickerState.minute
            showTimePicker = false
          }) { Text("Done") }
        },
        dismissButton = { TextButton(onClick = { showTimePicker = false }) { Text("Cancel") } },
      )
    }
  }
}

private fun alarmToneSummary(toneUri: String?): String =
  if (toneUri.isNullOrBlank()) "Bundled offline devotional chime" else "Selected local audio tone"
