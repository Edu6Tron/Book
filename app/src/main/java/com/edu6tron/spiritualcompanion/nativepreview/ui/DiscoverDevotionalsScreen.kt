package com.edu6tron.spiritualcompanion.nativepreview.ui

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import android.net.Uri

@Composable
fun DiscoverDevotionalsScreen(
  contentPadding: PaddingValues,
  onOpenAartis: () -> Unit,
) {
  var query by rememberSaveable { mutableStateOf("") }
  var submittedQuery by rememberSaveable { mutableStateOf<String?>(null) }
  val suggestedQueries = listOf("Ganesh Aarti lyrics", "Shiva Aarti", "Krishna bhajan", "Devi stotram", "Morning mantra")

  if (submittedQuery != null) {
    InAppProviderSearch(
      query = submittedQuery.orEmpty(),
      onBack = { submittedQuery = null },
    )
    return
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
        Text("Discover devotionals", style = MaterialTheme.typography.headlineSmall)
        Text("Search begins only when you tap Search. Nothing is fetched or refreshed in the background.", color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
    item {
      Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text("Search online", style = MaterialTheme.typography.titleMedium)
          Text("Results open inside this app. Online provider media cannot be used as an alarm tone; select local audio for an alarm instead.", color = MaterialTheme.colorScheme.onSurfaceVariant)
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
          Button(onClick = { submittedQuery = query.trim().ifBlank { "devotional Aarti lyrics" } }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Outlined.Search, contentDescription = null)
            Text(" Search online")
          }
        }
      }
    }
    item {
      Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Keep a devotional available offline", style = MaterialTheme.typography.titleMedium)
          Text("For uninterrupted offline playback and an alarm tone, choose an audio file you have permission to use. It stays on your device.", color = MaterialTheme.colorScheme.onSurfaceVariant)
          Button(onClick = onOpenAartis) { Text("Open devotional player") }
        }
      }
    }
  }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun InAppProviderSearch(query: String, onBack: () -> Unit) {
  Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Text("Online devotional search", style = MaterialTheme.typography.headlineSmall)
    Text("Provider content is streamed by the provider and remains separate from local alarm tones.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    Button(onClick = onBack) { Text("Back to search") }
    AndroidView(
      modifier = Modifier.fillMaxWidth().weight(1f),
      factory = { context ->
        WebView(context).apply {
          settings.javaScriptEnabled = true
          settings.domStorageEnabled = true
          settings.allowFileAccess = false
          settings.allowContentAccess = false
          settings.cacheMode = WebSettings.LOAD_DEFAULT
          webViewClient = WebViewClient()
          loadUrl("https://www.youtube.com/results?search_query=${Uri.encode(query)}")
        }
      },
    )
  }
}
