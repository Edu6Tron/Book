package com.edu6tron.spiritualcompanion.nativepreview.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.edu6tron.spiritualcompanion.nativepreview.BuildConfig
import com.edu6tron.spiritualcompanion.nativepreview.data.ReadingComfort
import com.edu6tron.spiritualcompanion.nativepreview.data.ThemeMode

@Composable
fun SettingsScreen(
  contentPadding: PaddingValues,
  themeMode: ThemeMode,
  readingComfort: ReadingComfort,
  onSaveThemeMode: (ThemeMode) -> Unit,
  onSaveReadingComfort: (ReadingComfort) -> Unit,
  onOpenNotificationSettings: () -> Unit,
  onNavigateBack: () -> Unit,
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(
      start = 20.dp,
      top = contentPadding.calculateTopPadding() + 12.dp,
      end = 20.dp,
      bottom = contentPadding.calculateBottomPadding() + 20.dp,
    ),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    item(contentType = "header") {
      Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onNavigateBack) {
          Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back to Today")
        }
        Column(modifier = Modifier.weight(1f)) {
          Text("Settings", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
          Text("Personalise this device", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }
    }
    item(contentType = "appearance") {
      SettingsCard(icon = { Icon(Icons.Outlined.Palette, null) }, title = "Appearance", detail = "Choose the colour treatment used across the app.") {
        ThemeMode.entries.forEach { mode ->
          ChoiceRow(mode.label, selected = themeMode == mode, onClick = { onSaveThemeMode(mode) })
        }
      }
    }
    item(contentType = "reading") {
      SettingsCard(icon = { Icon(Icons.Outlined.TextFields, null) }, title = "Reading comfort", detail = "Use a size that keeps Aartis, festival guidance, and practice content comfortable to read.") {
        ReadingComfort.entries.forEach { comfort ->
          ChoiceRow(comfort.label, selected = readingComfort == comfort, onClick = { onSaveReadingComfort(comfort) })
        }
      }
    }
    item(contentType = "notifications") {
      SettingsCard(icon = { Icon(Icons.Outlined.Notifications, null) }, title = "Notifications and alarms", detail = "Android controls this app’s notification permission and ritual-alarm notification channel. Changes open the device settings; no reminder information is sent to a server.") {
        Button(onClick = onOpenNotificationSettings, modifier = Modifier.fillMaxWidth()) {
          Text("Manage in Android settings")
        }
      }
    }
    item(contentType = "privacy") {
      SettingsCard(icon = { Icon(Icons.Outlined.PrivacyTip, null) }, title = "Privacy", detail = "This app keeps practice, alarm, reading, and appearance preferences on this device. It does not request GPS for the temple directory, and online discovery starts only when you choose it.") {}
    }
    item(contentType = "about") {
      SettingsCard(icon = { Icon(Icons.Outlined.Info, null) }, title = "About", detail = "Spiritual Companion Native · version ${BuildConfig.VERSION_NAME}\nOffline devotional content, source-labelled temples, local media, and Android-managed ritual alarms.") {}
    }
  }
}

@Composable
private fun SettingsCard(
  icon: @Composable () -> Unit,
  title: String,
  detail: String,
  content: @Composable () -> Unit,
) {
  Card(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
        icon()
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
      }
      Text(detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      content()
    }
  }
}

@Composable
private fun ChoiceRow(label: String, selected: Boolean, onClick: () -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(role = Role.RadioButton, onClick = onClick)
      .padding(vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    RadioButton(selected = selected, onClick = null)
    Text(label, modifier = Modifier.padding(start = 10.dp), style = MaterialTheme.typography.bodyLarge)
  }
}
