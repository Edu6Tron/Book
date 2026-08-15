package com.edu6tron.spiritualcompanion.nativepreview.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DiscoverDevotionalsScreen(
  contentPadding: PaddingValues,
  onOpenAartis: () -> Unit,
) {
  var query by rememberSaveable { mutableStateOf("") }
  val context = LocalContext.current
  val suggestedQueries = listOf("Ganesh Aarti", "Shiva Aarti", "Krishna bhajan", "Devi stotram", "Morning mantra")

  fun openProviderSearch() {
    val requested = query.trim().ifBlank { "devotional Aarti lyrics" }
    val uri = Uri.parse("https://www.youtube.com/results?search_query=${Uri.encode(requested)}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    if (intent.resolveActivity(context.packageManager) != null) context.startActivity(intent)
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(
      start = 20.dp,
      top = contentPadding.calculateTopPadding() + 16.dp,
      end = 20.dp,
      bottom = contentPadding.calculateBottomPadding() + 20.dp,
    ),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    item {
      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Discover devotionals", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Search is always started by you. The app does not fetch or refresh online media in the background.", color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
    item {
      Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text("Search an authorised provider", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
          Text("Open a devotional search in your installed provider or browser. Media from an online provider is never used as an alarm tone.", color = MaterialTheme.colorScheme.onSurfaceVariant)
          OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("What would you like to hear?") },
          )
          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(suggestedQueries) { suggestion ->
              AssistChip(onClick = { query = suggestion }, label = { Text(suggestion) })
            }
          }
          Button(onClick = ::openProviderSearch) {
            Icon(Icons.Outlined.OpenInNew, contentDescription = null)
            Text(" Search online")
          }
        }
      }
    }
    item {
      Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Keep a devotional available offline", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
          Text("For continuous, offline playback and a personal alarm tone, import an audio file you have permission to use. It stays on your device and is managed in the Aartis tab.", color = MaterialTheme.colorScheme.onSurfaceVariant)
          Button(onClick = onOpenAartis) { Text("Open devotional player") }
        }
      }
    }
  }
}
