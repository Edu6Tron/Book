package com.edu6tron.spiritualcompanion.nativepreview.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.edu6tron.spiritualcompanion.nativepreview.data.FestivalItem
import com.edu6tron.spiritualcompanion.nativepreview.data.NativeCatalogue
import com.edu6tron.spiritualcompanion.nativepreview.data.TempleItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FestivalCalendarScreen(contentPadding: PaddingValues) {
  var selectedSection by rememberSaveable { mutableIntStateOf(0) }
  var month by rememberSaveable { mutableStateOf("All") }
  var templeQuery by rememberSaveable { mutableStateOf("") }
  var templeCity by rememberSaveable { mutableStateOf("All") }
  var selectedFestival by remember { mutableStateOf<FestivalItem?>(null) }
  var selectedTemple by remember { mutableStateOf<TempleItem?>(null) }
  val festivals = remember(month) { NativeCatalogue.festivals.filter { month == "All" || it.hinduMonth == month } }
  val templeCities = remember { listOf("All") + NativeCatalogue.temples.map { it.city }.distinct().sorted() }
  val temples = remember(templeCity, templeQuery) {
    NativeCatalogue.temples.filter { temple ->
      (templeCity == "All" || temple.city == templeCity) &&
        (templeQuery.isBlank() || listOf(temple.name, temple.city, temple.state, temple.address, temple.authority).any {
          it.contains(templeQuery, ignoreCase = true)
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
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    item {
      Text(if (selectedSection == 0) "Festival calendar" else "Temple directory", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    }
    item {
      SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        listOf("Festivals", "Temples").forEachIndexed { index, label ->
          SegmentedButton(
            selected = selectedSection == index,
            onClick = { selectedSection = index },
            shape = SegmentedButtonDefaults.itemShape(index = index, count = 2),
            label = { Text(label) },
          )
        }
      }
    }
    if (selectedSection == 0) {
      item {
        Text("Indicative observances — verify regional dates with your local Panchang.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
      item {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(NativeCatalogue.hinduMonths, key = { it }) { chip ->
            FilterChip(selected = month == chip, onClick = { month = chip }, label = { Text(chip) })
          }
        }
      }
      items(festivals, key = { it.id }, contentType = { "festival" }) { festival ->
        FestivalCard(festival, onClick = { selectedFestival = festival })
      }
    } else {
      item {
        Text("Offline government and trust-source directory. No GPS or map tracking is used.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
      item {
        OutlinedTextField(
          value = templeQuery,
          onValueChange = { templeQuery = it },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true,
          label = { Text("Search temple, city, state, or authority") },
        )
      }
      item {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(templeCities, key = { it }) { city ->
            FilterChip(selected = templeCity == city, onClick = { templeCity = city }, label = { Text(city) })
          }
        }
      }
      if (temples.isEmpty()) {
        item {
          Card(modifier = Modifier.fillMaxWidth()) {
            Text("No authorised directory record matches this search.", modifier = Modifier.padding(20.dp))
          }
        }
      }
      items(temples, key = { it.id }, contentType = { "temple" }) { temple ->
        TempleCard(temple, onClick = { selectedTemple = temple })
      }
    }
  }
  selectedFestival?.let { festival -> FestivalDetailDialog(festival, onDismiss = { selectedFestival = null }) }
  selectedTemple?.let { temple -> TempleDetailDialog(temple, onDismiss = { selectedTemple = null }) }
}

@Composable
private fun FestivalCard(festival: FestivalItem, onClick: () -> Unit) {
  Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
      Text(festival.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
      Text("${festival.hinduMonth} · ${festival.dateNote}", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
      Text(festival.significance, maxLines = 2, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}

@Composable
private fun TempleCard(temple: TempleItem, onClick: () -> Unit) {
  Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
      Icon(Icons.Outlined.LocationCity, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
      Spacer(Modifier.size(12.dp))
      Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(temple.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text("${temple.city}, ${temple.state}", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
        Text(temple.registryStatus, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
  }
}

@Composable
private fun FestivalDetailDialog(festival: FestivalItem, onDismiss: () -> Unit) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(festival.name) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(festival.dateNote, color = MaterialTheme.colorScheme.primary)
        Text(festival.significance)
        Text("Simple observance", fontWeight = FontWeight.SemiBold)
        Text(festival.observance)
        Text("Source: ${festival.source}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    },
    confirmButton = { TextButton(onClick = onDismiss) { Text("Close") } },
  )
}

@Composable
private fun TempleDetailDialog(temple: TempleItem, onDismiss: () -> Unit) {
  val context = androidx.compose.ui.platform.LocalContext.current
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(temple.name) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(temple.address)
        Text(temple.registryStatus, color = MaterialTheme.colorScheme.primary)
        Text("Authority: ${temple.authority}")
        Text("Source: ${temple.sourceUrl}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    },
    confirmButton = {
      TextButton(onClick = {
        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(temple.sourceUrl))
        if (intent.resolveActivity(context.packageManager) != null) context.startActivity(intent)
      }) { Text("Open source") }
    },
    dismissButton = { TextButton(onClick = onDismiss) { Text("Close") } },
  )
}
