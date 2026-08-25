package com.edu6tron.spiritualcompanion.nativepreview.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.edu6tron.spiritualcompanion.nativepreview.data.FestivalItem
import com.edu6tron.spiritualcompanion.nativepreview.data.MaharashtraCalendar
import com.edu6tron.spiritualcompanion.nativepreview.data.MaharashtraCalendarSourceTier
import com.edu6tron.spiritualcompanion.nativepreview.data.MaharashtraRichCalendarEvent
import com.edu6tron.spiritualcompanion.nativepreview.data.NativeCatalogue
import com.edu6tron.spiritualcompanion.nativepreview.data.TempleItem
import com.edu6tron.spiritualcompanion.nativepreview.panchang.PanchangCalculator
import com.edu6tron.spiritualcompanion.nativepreview.panchang.PanchangSnapshot
import com.edu6tron.spiritualcompanion.nativepreview.panchang.OnlineAstronomyCache
import com.edu6tron.spiritualcompanion.nativepreview.panchang.withOnlineAstronomyTiming
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

private val monthFormatter = DateTimeFormatter.ofPattern("MMMM uuuu", Locale.ENGLISH)
private val fullDateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM uuuu", Locale.ENGLISH)
private val weekdayLabels = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FestivalCalendarScreen(
  contentPadding: PaddingValues,
  savedLocation: String?,
  onlineAstronomyCache: OnlineAstronomyCache?,
) {
  var selectedSection by rememberSaveable { mutableIntStateOf(0) }
  var month by rememberSaveable { mutableStateOf("All") }
  var templeQuery by rememberSaveable { mutableStateOf("") }
  var templeCity by rememberSaveable { mutableStateOf("All") }
  var displayedMonth by rememberSaveable { mutableStateOf(YearMonth.now()) }
  var selectedDate by rememberSaveable { mutableStateOf(LocalDate.now()) }
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
      Text(
        when (selectedSection) {
          0 -> "Maharashtra calendar"
          1 -> "Festival guides"
          else -> "Temple directory"
        },
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
      )
    }
    item {
      SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        listOf("Calendar", "Guides", "Temples").forEachIndexed { index, label ->
          SegmentedButton(
            selected = selectedSection == index,
            onClick = { selectedSection = index },
            shape = SegmentedButtonDefaults.itemShape(index = index, count = 3),
            label = { Text(label) },
          )
        }
      }
    }
    when (selectedSection) {
      0 -> item {
        MaharashtraMonthCalendar(
          displayedMonth = displayedMonth,
          selectedDate = selectedDate,
          savedLocation = savedLocation,
          onlineAstronomyCache = onlineAstronomyCache,
          onPreviousMonth = {
            CalendarMonthNavigator.previous(displayedMonth).also { selection ->
              displayedMonth = selection.month
              selectedDate = selection.selectedDate
            }
          },
          onNextMonth = {
            CalendarMonthNavigator.next(displayedMonth).also { selection ->
              displayedMonth = selection.month
              selectedDate = selection.selectedDate
            }
          },
          onSelectDate = { date ->
            CalendarMonthNavigator.select(date).also { selection ->
              selectedDate = selection.selectedDate
              displayedMonth = selection.month
            }
          },
        )
      }
      1 -> {
        item {
          Text(
            "Thirty-five offline devotional guides grouped by lunar month. Regional dates and temple timings should be verified with a published local Panchang.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
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
      }
      else -> {
        item {
          Text(
            "Offline government and trust-source directory. No GPS or map tracking is used.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
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
  }
  selectedFestival?.let { festival -> FestivalDetailDialog(festival, onDismiss = { selectedFestival = null }) }
  selectedTemple?.let { temple -> TempleDetailDialog(temple, onDismiss = { selectedTemple = null }) }
}

@Composable
private fun MaharashtraMonthCalendar(
  displayedMonth: YearMonth,
  selectedDate: LocalDate,
  savedLocation: String?,
  onlineAstronomyCache: OnlineAstronomyCache?,
  onPreviousMonth: () -> Unit,
  onNextMonth: () -> Unit,
  onSelectDate: (LocalDate) -> Unit,
) {
  val cells = remember(displayedMonth) { MaharashtraCalendar.monthGrid(displayedMonth) }
  val officialDates = remember(displayedMonth) { MaharashtraCalendar.observanceDatesIn(displayedMonth) }
  val snapshot = remember(selectedDate, savedLocation, onlineAstronomyCache) {
    val offlineSnapshot = PanchangCalculator.calculate(selectedDate, savedLocation)
    offlineSnapshot.withOnlineAstronomyTiming(
      onlineAstronomyCache?.timingFor(
        date = selectedDate,
        expectedLocationCacheKey = PanchangCalculator.onlineTimingLocation(savedLocation)?.cacheKey,
      ),
    )
  }
  val richEvents = remember(selectedDate, snapshot) { MaharashtraCalendar.richEventsOn(selectedDate, snapshot) }
  val today = LocalDate.now()

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
      Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("An original offline calendar for Maharashtra", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text(
          "Public-holiday markers are bundled from the 2026 official list. Panchang values are local estimates for your selected place, not ritual authority.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
      }
    }
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
      IconButton(onClick = onPreviousMonth, modifier = Modifier.semantics { contentDescription = "Show previous month" }) {
        Icon(Icons.Outlined.ChevronLeft, contentDescription = null)
      }
      Text(
        displayedMonth.format(monthFormatter),
        modifier = Modifier.weight(1f),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
      )
      IconButton(onClick = onNextMonth, modifier = Modifier.semantics { contentDescription = "Show next month" }) {
        Icon(Icons.Outlined.ChevronRight, contentDescription = null)
      }
    }
    Row(modifier = Modifier.fillMaxWidth()) {
      weekdayLabels.forEach { label ->
        Text(
          label,
          modifier = Modifier.weight(1f),
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
    cells.chunked(7).forEach { week ->
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        week.forEach { date ->
          if (date == null) {
            Spacer(modifier = Modifier.weight(1f).height(56.dp))
          } else {
            CalendarDayCell(
              date = date,
              isSelected = date == selectedDate,
              isToday = date == today,
              publicHolidayCount = if (date in officialDates) MaharashtraCalendar.observancesOn(date).size else 0,
              modifier = Modifier.weight(1f),
              onClick = { onSelectDate(date) },
            )
          }
        }
      }
    }
    SelectedDayCard(
      date = selectedDate,
      snapshot = snapshot,
      richEvents = richEvents,
    )
  }
}

@Composable
private fun CalendarDayCell(
  date: LocalDate,
  isSelected: Boolean,
  isToday: Boolean,
  publicHolidayCount: Int,
  modifier: Modifier,
  onClick: () -> Unit,
) {
  val background = when {
    isSelected -> MaterialTheme.colorScheme.primary
    isToday -> MaterialTheme.colorScheme.primaryContainer
    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
  }
  val foreground = when {
    isSelected -> MaterialTheme.colorScheme.onPrimary
    isToday -> MaterialTheme.colorScheme.onPrimaryContainer
    else -> MaterialTheme.colorScheme.onSurface
  }
  val dayDescription = buildString {
    append(date.format(fullDateFormatter))
    if (publicHolidayCount > 0) append(", $publicHolidayCount official public-holiday marker")
  }
  Box(
    modifier = modifier
      .height(56.dp)
      .clip(RoundedCornerShape(14.dp))
      .background(background)
      .clickable(onClick = onClick)
      .semantics {
        contentDescription = dayDescription
        stateDescription = if (isSelected) "Selected day" else "Select day"
      },
    contentAlignment = Alignment.Center,
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(3.dp)) {
      Text(date.dayOfMonth.toString(), color = foreground, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
      if (publicHolidayCount > 0) {
        Box(
          modifier = Modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary),
        )
      } else {
        Spacer(modifier = Modifier.height(6.dp))
      }
    }
  }
}

@Composable
private fun SelectedDayCard(
  date: LocalDate,
  snapshot: PanchangSnapshot,
  richEvents: List<MaharashtraRichCalendarEvent>,
) {
  Card(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Text(date.format(fullDateFormatter), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
      Text("${snapshot.placeLabel} · ${snapshot.sakaDate}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
      Text("Selected-day Panchang estimate", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
      PanchangValue("Brahma Muhurta", "${snapshot.brahmaMuhurtaStart.displayTime()} – ${snapshot.brahmaMuhurtaEnd.displayTime()}")
      PanchangValue("Sunrise / sunset", "${snapshot.sunrise.displayTime()} / ${snapshot.sunset.displayTime()}")
      PanchangValue("Moonrise / moonset", "${snapshot.moonrise.displayTime()} / ${snapshot.moonset.displayTime()}")
      PanchangValue("Tithi", "${snapshot.tithi} · ${snapshot.paksha}")
      PanchangValue("Nakshatra", snapshot.nakshatra)
      PanchangValue("Lunar month", "${snapshot.lunarMonthEstimate} (estimate)")
      Text("Source-labelled day events", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
      if (richEvents.isEmpty()) {
        Text(
          "No bundled government marker or calculated personal-practice cue for this date.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      } else {
        richEvents.forEach { event -> RichCalendarEventCard(event) }
      }
      Text(
        "For temple-specific or ritual-critical observance, confirm a locally published Panchang. This app provides personal guidance, not ritual authority.",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}

@Composable
private fun PanchangValue(label: String, value: String) {
  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
    Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Spacer(modifier = Modifier.width(12.dp))
    Text(value, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.End, modifier = Modifier.weight(1f))
  }
}

@Composable
private fun RichCalendarEventCard(event: MaharashtraRichCalendarEvent) {
  val containerColor = when (event.sourceTier) {
    MaharashtraCalendarSourceTier.GOVERNMENT_PUBLISHED -> MaterialTheme.colorScheme.secondaryContainer
    MaharashtraCalendarSourceTier.CURATED_DEVOTIONAL_GUIDE -> MaterialTheme.colorScheme.primaryContainer
    MaharashtraCalendarSourceTier.LOCAL_PANCHANG_ESTIMATE -> MaterialTheme.colorScheme.tertiaryContainer
    MaharashtraCalendarSourceTier.PERSONAL_PLAN -> MaterialTheme.colorScheme.surfaceVariant
  }
  val contentColor = when (event.sourceTier) {
    MaharashtraCalendarSourceTier.GOVERNMENT_PUBLISHED -> MaterialTheme.colorScheme.onSecondaryContainer
    MaharashtraCalendarSourceTier.CURATED_DEVOTIONAL_GUIDE -> MaterialTheme.colorScheme.onPrimaryContainer
    MaharashtraCalendarSourceTier.LOCAL_PANCHANG_ESTIMATE -> MaterialTheme.colorScheme.onTertiaryContainer
    MaharashtraCalendarSourceTier.PERSONAL_PLAN -> MaterialTheme.colorScheme.onSurfaceVariant
  }
  Card(colors = CardDefaults.cardColors(containerColor = containerColor, contentColor = contentColor)) {
    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
      Text(event.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
      Text(event.category, style = MaterialTheme.typography.labelSmall)
      Text(event.detail, style = MaterialTheme.typography.bodySmall)
      Text(event.sourceTier.label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
      Text(event.sourceTier.disclosure, style = MaterialTheme.typography.labelSmall)
      Text("Source: ${event.sourceLabel}", style = MaterialTheme.typography.labelSmall)
      if (event.isEstimate) {
        Text("Timing and observance are estimates; confirm locally when needed.", style = MaterialTheme.typography.labelSmall)
      }
    }
  }
}

private fun LocalTime?.displayTime(): String = this?.format(DateTimeFormatter.ofPattern("h:mm a")) ?: "Unavailable"

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
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(temple.sourceUrl))
        if (intent.resolveActivity(context.packageManager) != null) context.startActivity(intent)
      }) { Text("Open source") }
    },
    dismissButton = { TextButton(onClick = onDismiss) { Text("Close") } },
  )
}
