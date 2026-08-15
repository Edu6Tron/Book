package com.edu6tron.spiritualcompanion.nativepreview.ui

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.edu6tron.spiritualcompanion.nativepreview.data.DailyPractice
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
  state: DashboardUiState,
  onTogglePractice: (String) -> Unit,
) {
  Scaffold(
    topBar = {
      TopAppBar(title = { Text("Spiritual Companion") })
    },
  ) { padding ->
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(
        start = 20.dp,
        top = padding.calculateTopPadding() + 12.dp,
        end = 20.dp,
        bottom = 32.dp,
      ),
      verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
      item { PanchangHero() }
      item { TimingCard() }
      item { NativeCapabilitiesCard() }
      item {
        PracticeCard(
          practices = state.practices,
          onTogglePractice = onTogglePractice,
        )
      }
    }
  }
}

@Composable
private fun PanchangHero() {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
  ) {
    Column(
      modifier = Modifier.padding(22.dp),
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
        text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date()),
        style = MaterialTheme.typography.displaySmall,
        fontWeight = FontWeight.Bold,
      )
      Text(
        text = "Devotional clock • offline-first native preview",
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
      )
      Spacer(Modifier.height(8.dp))
      AssistChip(onClick = {}, label = { Text("Shukla Paksha") })
    }
  }
}

@Composable
private fun TimingCard() {
  Card(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.padding(18.dp)) {
      Text("Sacred windows", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
      Spacer(Modifier.height(12.dp))
      TimingRow("Brahma Muhurta", "04:12 AM – 05:00 AM")
      TimingRow("Sunrise", "05:54 AM")
      TimingRow("Sunset", "07:12 PM")
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
private fun NativeCapabilitiesCard() {
  Card(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.padding(18.dp)) {
      Text("Native foundation", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
      Spacer(Modifier.height(10.dp))
      CapabilityRow(Icons.Outlined.LibraryMusic, "Media3 playback engine ready for devotional audio")
      CapabilityRow(Icons.Outlined.MenuBook, "Room database keeps daily practices on device")
      CapabilityRow(Icons.Outlined.Alarm, "Alarm and full-screen native delivery planned next")
    }
  }
}

@Composable
private fun CapabilityRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
  Row(
    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
    Spacer(Modifier.size(10.dp))
    Text(label, style = MaterialTheme.typography.bodyMedium)
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
          Text(practice.title, modifier = Modifier.weight(1f))
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
