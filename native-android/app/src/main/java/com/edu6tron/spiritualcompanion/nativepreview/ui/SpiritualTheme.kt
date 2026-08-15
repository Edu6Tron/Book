package com.edu6tron.spiritualcompanion.nativepreview.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
  primary = Color(0xFFAA540D),
  onPrimary = Color.White,
  secondary = Color(0xFF6D5C3C),
  background = Color(0xFFFCF8F2),
  surface = Color(0xFFFFFDF9),
  onSurface = Color(0xFF221B14),
)

private val DarkColors = darkColorScheme(
  primary = Color(0xFFFFB77B),
  secondary = Color(0xFFE2C28B),
  background = Color(0xFF211A14),
  surface = Color(0xFF2C241D),
)

@Composable
fun SpiritualCompanionNativeTheme(content: @Composable () -> Unit) {
  MaterialTheme(
    colorScheme = LightColors,
    content = content,
  )
}
