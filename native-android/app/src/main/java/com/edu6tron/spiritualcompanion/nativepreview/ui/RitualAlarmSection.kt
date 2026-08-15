package com.edu6tron.spiritualcompanion.nativepreview.ui

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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.edu6tron.spiritualcompanion.nativepreview.alarm.RitualAlarmScheduler
import com.edu6tron.spiritualcompanion.nativepreview.data.RitualAlarmEntity
import com.edu6tron.spiritualcompanion.nativepreview.data.days
import com.edu6tron.spiritualcompanion.nativepreview.data.isPaused
import java.text.DateFormat
import java.util.Date
import java.util.UUID

private val dayLabels = listOf("S", "M", "T", "W", "T", "F", "S")

@Composable
fun RitualAlarmSection(
  alarms: List<RitualAlarmEntity>,
  onSave: (RitualAlarmEntity) -> Unit,
  onSetEnabled: (RitualAlarmEntity, Boolean) -> Unit,
  onDelete: (RitualAlarmEntity) -> Unit,
  onPause: (RitualAlarmEntity, Int) -> Unit,
  onResume: (RitualAlarmEntity) -> Unit,
  onPlayFallbackTone: () -> Unit,
  onStopTonePreview: () -> Unit,
) {
  var editorFor by remember { mutableStateOf<RitualAlarmEntity?>(null) }
  var creating by remember { mutableStateOf(false) }
  var pausing by remember { mutableStateOf<RitualAlarmEntity?>(null) }
  val context = LocalContext.current
  Card(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Outlined.Alarm, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text("Ritual alarms", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
          Text("Native exact schedule • full-screen Snooze and Stop", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        IconButton(onClick = { creating = true }) { Icon(Icons.Outlined.Alarm, contentDescription = "Add ritual alarm") }
      }
      if (!RitualAlarmScheduler.canScheduleExactAlarms(context)) {
        Text(
          "Allow Alarms & reminders for the most reliable screen-off timing. Until then Android may defer a ritual alarm.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.error,
        )
        OutlinedButton(onClick = {
          val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:${context.packageName}"))
          if (intent.resolveActivity(context.packageManager) != null) context.startActivity(intent)
        }, modifier = Modifier.fillMaxWidth()) { Text("Allow exact alarms") }
      }
      if (alarms.isEmpty()) {
        Text("No ritual alarm yet. Add a Brahma Muhurta reminder, then permit Alarms & reminders when Android asks.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Button(onClick = { creating = true }, modifier = Modifier.fillMaxWidth()) { Text("Add Brahma Muhurta alarm") }
      } else {
        alarms.forEach { alarm ->
          Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
              Text("%02d:%02d".format(alarm.hour, alarm.minute), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
              Text(alarm.label, style = MaterialTheme.typography.bodyMedium)
              Text(if (alarm.days().size == 7) "Every day • bundled devotional fallback" else "${alarm.days().size} days selected • bundled devotional fallback", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              if (alarm.isPaused()) {
                Text(
                  "Paused until ${DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(alarm.pauseUntilMillis!!))}",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.primary,
                )
              }
            }
            Switch(checked = alarm.enabled, onCheckedChange = { onSetEnabled(alarm, it) })
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
        Text("Preview offline devotional tone")
      }
      TextButton(onClick = onStopTonePreview, modifier = Modifier.align(Alignment.End)) { Text("Stop preview") }
    }
  }
  if (creating || editorFor != null) {
    AlarmEditorDialog(
      initial = editorFor,
      onDismiss = { creating = false; editorFor = null },
      onSave = {
        onSave(it)
        creating = false
        editorFor = null
      },
    )
  }
  pausing?.let { alarm ->
    AlertDialog(
      onDismissRequest = { pausing = null },
      title = { Text("Pause ${alarm.label}") },
      text = { Text("Choose how long this ritual alarm should remain paused. It resumes automatically afterwards.") },
      confirmButton = {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          listOf(1, 3, 7).forEach { days ->
            TextButton(onClick = { onPause(alarm, days); pausing = null }) { Text("$days day${if (days == 1) "" else "s"}") }
          }
        }
      },
      dismissButton = { TextButton(onClick = { pausing = null }) { Text("Cancel") } },
    )
  }
}

@Composable
private fun AlarmEditorDialog(initial: RitualAlarmEntity?, onDismiss: () -> Unit, onSave: (RitualAlarmEntity) -> Unit) {
  var label by remember(initial) { mutableStateOf(initial?.label ?: "Brahma Muhurta") }
  var hour by remember(initial) { mutableStateOf((initial?.hour ?: 4).toString()) }
  var minute by remember(initial) { mutableStateOf((initial?.minute ?: 30).toString()) }
  var selectedDays by remember(initial) { mutableStateOf((initial?.days() ?: (0..6).toList()).toSet()) }
  var invalidTime by remember { mutableStateOf(false) }
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(if (initial == null) "New ritual alarm" else "Edit ritual alarm") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(label, { label = it }, label = { Text("Label") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
          OutlinedTextField(hour, { hour = it.filter(Char::isDigit).take(2) }, label = { Text("Hour") }, modifier = Modifier.weight(1f), singleLine = true)
          OutlinedTextField(minute, { minute = it.filter(Char::isDigit).take(2) }, label = { Text("Minute") }, modifier = Modifier.weight(1f), singleLine = true)
        }
        Text("Repeat on", style = MaterialTheme.typography.labelLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
          dayLabels.forEachIndexed { index, day ->
            FilterChip(
              selected = index in selectedDays,
              onClick = { selectedDays = if (index in selectedDays) selectedDays - index else selectedDays + index },
              label = { Text(day) },
            )
          }
        }
        Text("Online results are kept for after-alert devotional playback only. The alarm itself always uses your local tone or this bundled offline chime.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (invalidTime) Text("Enter an hour from 0–23, minute from 0–59, and select at least one day.", color = MaterialTheme.colorScheme.error)
      }
    },
    confirmButton = {
      Button(onClick = {
        val parsedHour = hour.toIntOrNull()
        val parsedMinute = minute.toIntOrNull()
        if (parsedHour == null || parsedHour !in 0..23 || parsedMinute == null || parsedMinute !in 0..59 || selectedDays.isEmpty()) {
          invalidTime = true
        } else {
          onSave(
            RitualAlarmEntity(
              id = initial?.id ?: UUID.randomUUID().toString(),
              label = label.trim().ifBlank { "Ritual alarm" },
              hour = parsedHour,
              minute = parsedMinute,
              repeatDays = selectedDays.sorted().joinToString(","),
              enabled = initial?.enabled ?: true,
              toneUri = initial?.toneUri,
              afterAlertAartiId = initial?.afterAlertAartiId,
            ),
          )
        }
      }) { Text("Save") }
    },
    dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
  )
}
