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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PracticeScreen(
  contentPadding: PaddingValues,
  state: DashboardUiState,
  onTogglePractice: (String) -> Unit,
  onIncrementJapa: () -> Unit,
  onResetJapa: () -> Unit,
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
    item {
      Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text("Daily practice", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Small repeatable acts, kept on this device", color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
    item { JapaCounterCard(state.japaCount, onIncrementJapa, onResetJapa) }
    item { Text("Today’s checklist", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold) }
    items(state.practices, key = { it.id }) { practice ->
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
}

@Composable
private fun JapaCounterCard(count: Int, onIncrement: () -> Unit, onReset: () -> Unit) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
  ) {
    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Icon(Icons.Outlined.TouchApp, contentDescription = null, modifier = Modifier.size(30.dp), tint = MaterialTheme.colorScheme.primary)
      Text("$count", style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold)
      Text("Japa repetitions today", color = MaterialTheme.colorScheme.onPrimaryContainer)
      Button(onClick = onIncrement, modifier = Modifier.fillMaxWidth()) { Text("Count one repetition") }
      OutlinedButton(onClick = onReset, modifier = Modifier.fillMaxWidth()) {
        Icon(Icons.Outlined.Refresh, contentDescription = null)
        Spacer(Modifier.size(6.dp))
        Text("Reset today’s count")
      }
    }
  }
}
