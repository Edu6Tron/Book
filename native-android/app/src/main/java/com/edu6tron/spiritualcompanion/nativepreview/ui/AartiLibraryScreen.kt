package com.edu6tron.spiritualcompanion.nativepreview.ui

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.edu6tron.spiritualcompanion.nativepreview.data.AartiItem
import com.edu6tron.spiritualcompanion.nativepreview.data.NativeCatalogue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AartiLibraryScreen(
  contentPadding: PaddingValues,
  favourites: Set<String>,
  onToggleFavourite: (String) -> Unit,
) {
  var query by rememberSaveable { mutableStateOf("") }
  var category by rememberSaveable { mutableStateOf("All") }
  var selectedAarti by remember { mutableStateOf<AartiItem?>(null) }
  val filtered = NativeCatalogue.aartis.filter { item ->
    (category == "All" || item.category == category || item.deity == category) &&
      (query.isBlank() || listOf(item.title, item.deity, item.category, item.languages.joinToString()).any { it.contains(query, ignoreCase = true) })
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
        Text("Curated devotional lyrics available offline", color = MaterialTheme.colorScheme.onSurfaceVariant)
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
            leadingIcon = if (chip == category) ({ Icon(Icons.Outlined.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp)) }) else null,
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
