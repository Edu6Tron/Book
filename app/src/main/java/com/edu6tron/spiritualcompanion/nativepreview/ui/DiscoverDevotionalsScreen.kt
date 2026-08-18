package com.edu6tron.spiritualcompanion.nativepreview.ui

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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DiscoverDevotionalsScreen(
  contentPadding: PaddingValues,
  onOpenAartis: () -> Unit,
  onOpenYouTube: (String) -> Unit,
) {
  var query by rememberSaveable { mutableStateOf("") }
  var lastOpenedQuery by rememberSaveable { mutableStateOf<String?>(null) }
  val suggestedQueries = listOf(
    "Ganesh Aarti lyrics",
    "Shiva Aarti",
    "Krishna bhajan",
    "Devi stotram",
    "Morning mantra",
  )

  fun openSearch() {
    val resolvedQuery = ProviderSearchPolicy.normaliseQuery(query)
    lastOpenedQuery = resolvedQuery
    onOpenYouTube(resolvedQuery)
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
    item(key = "discover-heading", contentType = "heading") {
      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Discover devotionals", style = MaterialTheme.typography.headlineSmall)
        Text(
          "Search starts only when you choose it. There is no background fetch, location tracking, or stored search history.",
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
    item(key = "provider-search", contentType = "search") {
      Card(modifier = Modifier.fillMaxWidth()) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          Text("Search online", style = MaterialTheme.typography.titleMedium)
          Text(
            "Official YouTube opens inside Spiritual Companion. Its controls, branding, account menus, and advertising remain provided by YouTube.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("What would you like to hear?") },
            supportingText = { Text("Online media is streamed by its provider and cannot be used as an alarm tone.") },
          )
          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(suggestedQueries, key = { it }) { suggestion ->
              AssistChip(onClick = { query = suggestion }, label = { Text(suggestion) })
            }
          }
          Button(onClick = ::openSearch, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Outlined.Search, contentDescription = null)
            Text(" Search YouTube here")
          }
        }
      }
    }
    if (lastOpenedQuery != null) {
      item(key = "provider-return", contentType = "status") {
        Card(modifier = Modifier.fillMaxWidth()) {
          Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
          ) {
            Text("Return to your practice anytime", style = MaterialTheme.typography.titleMedium)
            Text(
              "YouTube stays separate from your devotional data. Close YouTube or use Back to return here.",
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            TextButton(onClick = ::openSearch) {
              Icon(Icons.Outlined.Search, contentDescription = null)
              Text(" Reopen search")
            }
          }
        }
      }
    }
    item(key = "offline-player", contentType = "offline") {
      Card(modifier = Modifier.fillMaxWidth()) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Text("Keep a devotional available offline", style = MaterialTheme.typography.titleMedium)
          Text(
            "For uninterrupted offline playback and an alarm tone, choose an audio file you have permission to use. It stays on your device.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Button(onClick = onOpenAartis) { Text("Open devotional player") }
        }
      }
    }
  }
}
