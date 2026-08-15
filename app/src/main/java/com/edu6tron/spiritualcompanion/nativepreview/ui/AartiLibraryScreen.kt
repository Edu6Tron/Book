package com.edu6tron.spiritualcompanion.nativepreview.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.edu6tron.spiritualcompanion.nativepreview.data.AartiItem
import com.edu6tron.spiritualcompanion.nativepreview.data.NativeCatalogue
import com.edu6tron.spiritualcompanion.nativepreview.media.DevotionalPlaybackState

@Composable
fun AartiLibraryScreen(
  contentPadding: PaddingValues,
  favourites: Set<String>,
  onToggleFavourite: (String) -> Unit,
  selectedMediaUri: String?,
  selectedMediaLabel: String?,
  onSaveSelectedMedia: (String, String) -> Unit,
  onClearSelectedMedia: () -> Unit,
  onPlaySelectedMedia: (String, String) -> Unit,
  onStopPlayback: () -> Unit,
  playback: DevotionalPlaybackState,
  savedLocation: String?,
  onSaveLocation: (String) -> Unit,
  onClearLocation: () -> Unit,
) {
  var query by rememberSaveable { mutableStateOf("") }
  var category by rememberSaveable { mutableStateOf("All") }
  var locationDraft by rememberSaveable(savedLocation) { mutableStateOf(savedLocation.orEmpty()) }
  var selectedAarti by remember { mutableStateOf<AartiItem?>(null) }
  val context = LocalContext.current
  val mediaPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
    uri ?: return@rememberLauncherForActivityResult
    runCatching {
      context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    val label = uri.lastPathSegment?.substringAfterLast('/')?.takeIf { it.isNotBlank() }
      ?: "Selected devotional audio"
    onSaveSelectedMedia(uri.toString(), label)
  }
  val locationSuggestions = savedLocation
    ?.takeIf { it.isNotBlank() }
    ?.let { location -> NativeCatalogue.suggestionsFor(location).map { it.id }.toSet() }
    .orEmpty()
  val filtered = NativeCatalogue.aartis.filter { item ->
    (category == "All" || item.category == category || item.deity == category) &&
      (locationSuggestions.isEmpty() || item.id in locationSuggestions) &&
      (query.isBlank() || listOf(item.title, item.deity, item.category, item.languages.joinToString()).any {
        it.contains(query, ignoreCase = true)
      })
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
        Text("Aarti library", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Curated devotional lyrics and your downloaded audio, available offline", color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("Devotional player", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
          Text(
            selectedMediaLabel ?: "Choose an audio file already stored on your device. Its access is retained for offline playback.",
            color = MaterialTheme.colorScheme.onSecondaryContainer,
          )
          Button(onClick = { mediaPicker.launch(arrayOf("audio/*")) }) {
            Text(if (selectedMediaUri == null) "Choose local audio" else "Change audio")
          }
          if (selectedMediaUri != null) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              TextButton(onClick = { onPlaySelectedMedia(selectedMediaUri, selectedMediaLabel ?: "Selected devotional audio") }) { Text("Play") }
              TextButton(onClick = onStopPlayback) { Text("Stop") }
              TextButton(onClick = onClearSelectedMedia) { Text("Clear") }
            }
          }
          playback.sourceLabel?.let { source ->
            Text(
              if (playback.isPlaying) "Playing: $source" else "${playback.message ?: "Ready"}: $source",
              style = MaterialTheme.typography.bodySmall,
              color = if (playback.isPlaying) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondaryContainer,
            )
          }
        }
      }
    }
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("Aartis for your place", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
          Text(
            "Optionally save a city or state to guide the offline library. Your location is entered by you; this app does not use GPS.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          OutlinedTextField(
            value = locationDraft,
            onValueChange = { locationDraft = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("City or state, for example Pune, Maharashtra") },
          )
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onSaveLocation(locationDraft) }, enabled = locationDraft.isNotBlank()) {
              Text(if (savedLocation == null) "Save location" else "Refresh suggestions")
            }
            if (savedLocation != null) {
              TextButton(onClick = {
                locationDraft = ""
                onClearLocation()
              }) { Text("Clear") }
            }
          }
          savedLocation?.let { location ->
            Text(
              "Showing ${locationSuggestions.size} location-guided Aartis for $location.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.primary,
            )
          }
        }
      }
    }
    item {
      OutlinedTextField(
        value = query,
        onValueChange = { query = it },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        label = { Text("Search Aartis or deity") },
      )
    }
    item {
      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(NativeCatalogue.aartiCategories) { chip ->
          AssistChip(
            onClick = { category = chip },
            label = { Text(chip) },
            leadingIcon = if (chip == category) ({
              Icon(Icons.Outlined.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
            }) else null,
          )
        }
      }
    }
    if (filtered.isEmpty()) {
      item {
        Card(modifier = Modifier.fillMaxWidth()) {
          Text("No Aartis match this search. Try a deity, language, or category.", modifier = Modifier.padding(20.dp))
        }
      }
    }
    items(filtered, key = { it.id }) { aarti ->
      AartiListCard(
        aarti = aarti,
        favourite = aarti.id in favourites,
        onOpen = { selectedAarti = aarti },
        onToggleFavourite = { onToggleFavourite(aarti.id) },
      )
    }
  }

  selectedAarti?.let { aarti ->
    AartiLyricsDialog(aarti = aarti, onDismiss = { selectedAarti = null })
  }
}

@Composable
private fun AartiListCard(
  aarti: AartiItem,
  favourite: Boolean,
  onOpen: () -> Unit,
  onToggleFavourite: () -> Unit,
) {
  Card(
    modifier = Modifier.fillMaxWidth().clickable(onClick = onOpen),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(aarti.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text("${aarti.deity} · ${aarti.languages.joinToString(" / ")} · ${aarti.duration}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Text(aarti.opening, maxLines = 2, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
      IconButton(onClick = onToggleFavourite) {
        Icon(
          if (favourite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
          contentDescription = if (favourite) "Remove from favourites" else "Add to favourites",
          tint = if (favourite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
  }
}

@Composable
private fun AartiLyricsDialog(aarti: AartiItem, onDismiss: () -> Unit) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(aarti.title) },
    text = {
      LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text(aarti.summary, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        items(aarti.verses) { verse -> Text(verse, style = MaterialTheme.typography.bodyLarge) }
        item {
          Spacer(Modifier.height(2.dp))
          Text("Source: ${aarti.source}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }
    },
    confirmButton = { TextButton(onClick = onDismiss) { Text("Close") } },
  )
}
