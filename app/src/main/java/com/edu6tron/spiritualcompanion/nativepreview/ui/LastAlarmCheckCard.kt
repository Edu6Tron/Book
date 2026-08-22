package com.edu6tron.spiritualcompanion.nativepreview.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edu6tron.spiritualcompanion.nativepreview.alarm.AlarmDeliveryDiagnostic
import java.text.DateFormat
import java.util.Date

/** Displays only the private, label-free delivery stage saved on this device. */
@Composable
fun LastAlarmCheckCard(
  diagnostic: AlarmDeliveryDiagnostic?,
  onClear: () -> Unit,
) {
  Card(colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text("Last alarm check", style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(1f))
        if (diagnostic != null) TextButton(onClick = onClear) { Text("Clear") }
      }
      if (diagnostic == null) {
        Text(
          "No alarm-delivery stage has been recorded on this device yet. Run a short locked-screen test after enabling an alarm.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      } else {
        Text(diagnostic.stage.userFacingSummary, style = MaterialTheme.typography.bodySmall)
        Text(
          DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(diagnostic.occurredAtMillis)),
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
          "Stored only on this device: delivery stage and time. No alarm label, media path, location, account, or user text is recorded.",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
  }
}
