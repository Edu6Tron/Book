package com.edu6tron.spiritualcompanion.nativepreview.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.edu6tron.spiritualcompanion.nativepreview.data.AartiItem
import com.edu6tron.spiritualcompanion.nativepreview.data.NativeCatalogue
import com.edu6tron.spiritualcompanion.nativepreview.media.DevotionalPlaybackState
import com.edu6tron.spiritualcompanion.nativepreview.media.LyricTiming
import com.edu6tron.spiritualcompanion.nativepreview.panchang.PanchangCalculator

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
  initialAartiId: String? = null,
  onInitialAartiConsumed: () -> Unit = {},
) {
  var query by rememberSaveable { mutableStateOf("") }
  var category by rememberSaveable { mutableStateOf("All") }
  var locationDraft by rememberSaveable(savedLocation) { mutableStateOf(savedLocation.orEmpty()) }
  var selectedAarti by remember { mutableStateOf<AartiItem?>(null) }
  LaunchedEffect(initialAartiId) {
    initialAartiId?.let { aartiId ->
      selectedAarti = NativeCatalogue.aartis.firstOrNull { it.id == aartiId }
      onInitialAartiConsumed()
    }
  }
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
  val supportedPlaces = remember { PanchangCalculator.supportedPlaceLabels() }
  val locationSuggestions = remember(savedLocation) {
    savedLocation
      ?.takeIf { it.isNotBlank() }
      ?.let { location -> NativeCatalogue.suggestionsFor(location).map { it.id }.toSet() }
      .orEmpty()
  }
  val filtered = remember(category, query, locationSuggestions) {
    NativeCatalogue.aartis.filter { item ->
      (category == "All" || item.category == category || item.deity == category) &&
        (locationSuggestions.isEmpty() || item.id in locationSuggestions) &&
        (query.isBlank() || listOf(item.title, item.deity, item.category, item.languages.joinToString()).any {
          it.contains(query, ignoreCase = true)
        })
    }
  }

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
      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("OFFLINE LYRICS & AUDIO", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Text("Aarti library", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Curated lyrics and your chosen local audio, ready whenever you need them", color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shape = RoundedCornerShape(28.dp),
      ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("YOUR LOCAL PLAYER", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
          Text("Devotional player", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
          Text(
            selectedMediaLabel ?: "Choose an audio file already stored on your device. Its access is retained for offline playback.",
            color = MaterialTheme.colorScheme.onSecondaryContainer,
          )
          Button(onClick = { mediaPicker.launch(arrayOf("audio/*")) }) {
            Text(if (selectedMediaUri == null) "Choose local audio" else "Change audio")
          }
          if (selectedMediaUri != null) {
            val isSelectedAudioPlaying = playback.isPlaying &&
              playback.sourceLabel == (selectedMediaLabel ?: "Selected devotional audio")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              TextButton(onClick = { onPlaySelectedMedia(selectedMediaUri, selectedMediaLabel ?: "Selected devotional audio") }) {
                Text(if (isSelectedAudioPlaying) "Restart" else "Play")
              }
              TextButton(onClick = onClearSelectedMedia) { Text("Clear") }
            }
          }
          if (playback.isPlaying) {
            TextButton(onClick = onStopPlayback) { Text("Stop playing audio") }
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
        shape = RoundedCornerShape(28.dp),
      ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("PERSONALISE THE LIBRARY", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
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
          Text("Quick choices", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(supportedPlaces, key = { it }) { place ->
              AssistChip(onClick = { locationDraft = place }, label = { Text(place.substringBefore(',')) })
            }
          }
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
        label = { Text("Search Aartis, deity, or language") },
      )
    }
    item {
      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(NativeCatalogue.aartiCategories, key = { it }) { chip ->
          AssistChip(
            onClick = { category = chip },
            label = { Text(chip) },
            leadingIcon = if (chip == category) ({
              Icon(Icons.AutoMirrored.Outlined.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
            }) else null,
          )
        }
      }
    }
    if (filtered.isEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
          shape = RoundedCornerShape(24.dp),
        ) {
          Text("No Aartis match this search. Try a deity, language, or category.", modifier = Modifier.padding(20.dp))
        }
      }
    }
    items(filtered, key = { it.id }, contentType = { "aarti" }) { aarti ->
      AartiListCard(
        aarti = aarti,
        favourite = aarti.id in favourites,
        onOpen = { selectedAarti = aarti },
        onToggleFavourite = { onToggleFavourite(aarti.id) },
      )
    }
  }

  selectedAarti?.let { aarti ->
    AartiLyricsDialog(
      aarti = aarti,
      selectedMediaUri = selectedMediaUri,
      selectedMediaLabel = selectedMediaLabel,
      playback = playback,
      onPlaySelectedMedia = onPlaySelectedMedia,
      onStopPlayback = onStopPlayback,
      onDismiss = { selectedAarti = null },
    )
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
    shape = RoundedCornerShape(24.dp),
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 18.dp, vertical = 17.dp),
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
private fun AartiLyricsDialog(
  aarti: AartiItem,
  selectedMediaUri: String?,
  selectedMediaLabel: String?,
  playback: DevotionalPlaybackState,
  onPlaySelectedMedia: (String, String) -> Unit,
  onStopPlayback: () -> Unit,
  onDismiss: () -> Unit,
) {
  val activeVerseIndex = if (playback.isPlaying) {
    LyricTiming.activeVerseIndex(playback.positionMs, playback.durationMs, aarti.verses.size)
  } else -1
  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
      Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        Text(aarti.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(
          "Use your local recording while reading. When duration is available, the current verse is highlighted proportionally; recordings can have different exact timings.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (selectedMediaUri != null) {
          val isSelectedAudioPlaying = playback.isPlaying &&
            playback.sourceLabel == (selectedMediaLabel ?: "Selected devotional audio")
          Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
            Row(
              modifier = Modifier.fillMaxWidth().padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(selectedMediaLabel ?: "Selected local audio", style = MaterialTheme.typography.titleSmall)
                Text(
                  if (playback.isPlaying) "Playing in this app" else playback.message ?: "Ready for offline playback",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
              }
              TextButton(onClick = {
                onPlaySelectedMedia(selectedMediaUri, selectedMediaLabel ?: "Selected devotional audio")
              }) { Text(if (isSelectedAudioPlaying) "Restart" else "Play") }
              if (playback.isPlaying) {
                TextButton(onClick = onStopPlayback) { Text("Stop") }
              }
            }
          }
        } else {
          Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)) {
            Text(
              "Choose local audio from the Aarti library to listen offline while reading these lyrics.",
              modifier = Modifier.padding(12.dp),
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
        LazyColumn(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
          item { Text(aarti.summary, color = MaterialTheme.colorScheme.onSurfaceVariant) }
          items(count = aarti.verses.size, key = { it }, contentType = { "verse" }) { index ->
            val isActive = index == activeVerseIndex
            Card(
              colors = CardDefaults.cardColors(
                containerColor = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
              ),
            ) {
              Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (isActive) {
                  Text("Reading now", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                }
                Text(aarti.verses[index], style = MaterialTheme.typography.bodyLarge)
              }
            }
          }
          item {
            Text("Source: ${aarti.source}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
        TextButton(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) { Text("Close") }
      }
    }
  }
}
