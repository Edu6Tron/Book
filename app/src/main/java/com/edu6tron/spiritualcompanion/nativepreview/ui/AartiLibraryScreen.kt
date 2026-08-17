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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Forward10
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Replay10
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
  onTogglePlayback: () -> Unit,
  onSeekTo: (Long) -> Unit,
  onSeekBy: (Long) -> Unit,
  playback: DevotionalPlaybackState,
  personalLyricTimingByAarti: Map<String, List<Long>>,
  onSavePersonalLyricTiming: (String, List<Long>) -> Unit,
  onClearPersonalLyricTiming: (String) -> Unit,
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
        Text("Curated reading excerpts, verified whole texts, and your chosen local audio — ready whenever you need them", color = MaterialTheme.colorScheme.onSurfaceVariant)
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
        onTogglePlayback = onTogglePlayback,
        onSeekTo = onSeekTo,
        onSeekBy = onSeekBy,
        savedPersonalOffsetsMs = personalLyricTimingByAarti[aarti.id].orEmpty(),
        onSavePersonalOffsets = { offsets -> onSavePersonalLyricTiming(aarti.id, offsets) },
        onClearPersonalOffsets = { onClearPersonalLyricTiming(aarti.id) },
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
  onTogglePlayback: () -> Unit,
  onSeekTo: (Long) -> Unit,
  onSeekBy: (Long) -> Unit,
  savedPersonalOffsetsMs: List<Long>,
  onSavePersonalOffsets: (List<Long>) -> Unit,
  onClearPersonalOffsets: () -> Unit,
  onDismiss: () -> Unit,
) {
  val context = LocalContext.current
  val listState = rememberLazyListState()
  var readingMode by rememberSaveable(aarti.id) { mutableStateOf(false) }
  var readingLineIndex by rememberSaveable(aarti.id) { mutableStateOf(0) }
  var personalOffsetsMs by remember(aarti.id, savedPersonalOffsetsMs) { mutableStateOf(savedPersonalOffsetsMs) }
  val hasPersonalTiming = LyricTiming.isValidProfile(personalOffsetsMs, aarti.verses.size)
  val selectedAudioLabel = selectedMediaLabel ?: "Selected devotional audio"
  val isSelectedAartiAudio = selectedMediaUri != null && playback.sourceLabel == selectedAudioLabel
  val activeVerseIndex = if (isSelectedAartiAudio && playback.durationMs > 0L && !readingMode) {
    LyricTiming.activeLineIndex(
      positionMs = playback.positionMs,
      durationMs = playback.durationMs,
      lineCount = aarti.verses.size,
      personalOffsetsMs = personalOffsetsMs,
    )
  } else -1
  val currentLineIndex = if (readingMode) readingLineIndex else activeVerseIndex
  val durationMs = playback.durationMs.coerceAtLeast(0L)
  var sliderPosition by remember(playback.positionMs, durationMs) {
    mutableFloatStateOf(playback.positionMs.coerceIn(0L, durationMs).toFloat())
  }
  LaunchedEffect(activeVerseIndex, readingMode) {
    if (!readingMode && activeVerseIndex >= 0) {
      listState.animateScrollToItem(activeVerseIndex + 1)
    }
  }
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
        Row(verticalAlignment = Alignment.CenterVertically) {
          Column(modifier = Modifier.weight(1f)) {
            Text(aarti.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(
              aarti.deity + " · " + aarti.languages.joinToString(" / "),
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
          TextButton(onClick = onDismiss) { Text("Done") }
        }
        aarti.textAttribution?.let { attribution ->
          Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
              Text(
                if (attribution.includesWholeText) "VERIFIED WHOLE TEXT" else "SOURCE-ATTRIBUTED TEXT",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.tertiary,
              )
              Text(attribution.licence, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
              Text(attribution.attribution, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onTertiaryContainer)
              Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = {
                  context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(attribution.sourceUrl)))
                }) { Text("Open source") }
                TextButton(onClick = {
                  context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(attribution.licenceUrl)))
                }) { Text("Open licence") }
              }
            }
          }
        }
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)) {
          Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("On-device lyrics player", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Text(
              when {
                hasPersonalTiming -> "Personal sync markers are active for this local recording."
                playback.durationMs > 0L -> "Guided pace is proportional to this local recording; it is not a third-party transcript sync."
                else -> "Reading mode is ready. Add local audio to enable guided pacing."
              },
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              FilterChip(
                selected = !readingMode,
                onClick = { readingMode = false },
                label = { Text(if (hasPersonalTiming) "Personal sync" else "Guided pace") },
              )
              FilterChip(
                selected = readingMode,
                onClick = { readingMode = true },
                label = { Text("Reading mode") },
              )
            }
          }
        }
        if (selectedMediaUri != null) {
          val isSelectedAudioPlaying = playback.isPlaying && isSelectedAartiAudio
          Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
            Column(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                Text(selectedMediaLabel ?: "Selected local audio", style = MaterialTheme.typography.titleSmall)
                Text(
                  if (isSelectedAudioPlaying) "Playing privately in this app" else playback.message ?: "Ready for offline playback",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
              }
                TextButton(onClick = {
                  onPlaySelectedMedia(selectedMediaUri, selectedMediaLabel ?: "Selected devotional audio")
                }) { Text(if (isSelectedAudioPlaying) "Restart" else "Play") }
              }
              if (durationMs > 0L) {
                Slider(
                  value = sliderPosition,
                  onValueChange = { sliderPosition = it },
                  onValueChangeFinished = { onSeekTo(sliderPosition.toLong()) },
                  valueRange = 0f..durationMs.toFloat(),
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                  Text(formatPlaybackTime(playback.positionMs), style = MaterialTheme.typography.labelSmall)
                  Text(formatPlaybackTime(durationMs), style = MaterialTheme.typography.labelSmall)
                }
              }
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                IconButton(onClick = { onSeekBy(-15_000L) }, enabled = durationMs > 0L) {
                  Icon(Icons.Outlined.Replay10, contentDescription = "Rewind 15 seconds")
                }
                IconButton(onClick = onTogglePlayback, enabled = isSelectedAudioPlaying || durationMs > 0L) {
                  Icon(
                    if (isSelectedAudioPlaying) Icons.Outlined.Pause else Icons.Outlined.PlayArrow,
                    contentDescription = if (isSelectedAudioPlaying) "Pause local audio" else "Resume local audio",
                  )
                }
                IconButton(onClick = { onSeekBy(15_000L) }, enabled = durationMs > 0L) {
                  Icon(Icons.Outlined.Forward10, contentDescription = "Forward 15 seconds")
                }
                IconButton(onClick = onStopPlayback, enabled = isSelectedAudioPlaying || playback.positionMs > 0L) {
                  Icon(Icons.Outlined.Stop, contentDescription = "Stop local audio")
                }
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
          state = listState,
          verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
          item { Text(aarti.summary, color = MaterialTheme.colorScheme.onSurfaceVariant) }
          items(count = aarti.verses.size, key = { it }, contentType = { "verse" }) { index ->
            val isActive = index == currentLineIndex
            Card(
              modifier = Modifier.clickable {
                readingMode = true
                readingLineIndex = index
                LyricTiming.offsetForLine(personalOffsetsMs, index, aarti.verses.size)?.let(onSeekTo)
              },
              colors = CardDefaults.cardColors(
                containerColor = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
              ),
            ) {
              Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                if (isActive) {
                  Text(if (readingMode) "Reading now" else "Current line", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                }
                Text(aarti.verses[index], style = MaterialTheme.typography.bodyLarge)
                if (isSelectedAartiAudio && durationMs > 0L) {
                  TextButton(onClick = {
                    val updated = LyricTiming.withOffset(
                      currentOffsetsMs = personalOffsetsMs,
                      lineIndex = index,
                      positionMs = playback.positionMs,
                      lineCount = aarti.verses.size,
                    )
                    if (updated != null) {
                      personalOffsetsMs = updated
                      if (LyricTiming.isValidProfile(updated, aarti.verses.size)) onSavePersonalOffsets(updated)
                    }
                  }) {
                    Icon(Icons.Outlined.Tune, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.size(4.dp))
                    Text("Set this line at ${formatPlaybackTime(playback.positionMs)}")
                  }
                }
              }
            }
          }
          item {
            Text(
              if (aarti.textAttribution == null) {
                "Guide source: ${aarti.source}. This screen provides a short reading excerpt; full-text rights are not represented as verified."
              } else {
                "Text source: ${aarti.source}"
              },
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
        if (hasPersonalTiming) {
          TextButton(onClick = {
            personalOffsetsMs = emptyList()
            onClearPersonalOffsets()
          }, modifier = Modifier.align(Alignment.End)) { Text("Clear personal sync") }
        }
      }
    }
  }
}

private fun formatPlaybackTime(milliseconds: Long): String {
  val totalSeconds = (milliseconds.coerceAtLeast(0L) / 1_000L).toInt()
  return "${totalSeconds / 60}:${(totalSeconds % 60).toString().padStart(2, '0')}"
}
