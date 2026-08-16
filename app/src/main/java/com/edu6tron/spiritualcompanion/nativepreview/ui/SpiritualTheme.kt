package com.edu6tron.spiritualcompanion.nativepreview.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.edu6tron.spiritualcompanion.nativepreview.data.ReadingComfort

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
fun SpiritualCompanionNativeTheme(
  readingComfort: ReadingComfort = ReadingComfort.STANDARD,
  content: @Composable () -> Unit,
) {
  val baseTypography = remember { Typography() }
  val typography = remember(readingComfort, baseTypography) {
    val scale = readingComfort.textScale
    baseTypography.copy(
      headlineLarge = baseTypography.headlineLarge.copy(fontSize = baseTypography.headlineLarge.fontSize * scale),
      headlineMedium = baseTypography.headlineMedium.copy(fontSize = baseTypography.headlineMedium.fontSize * scale),
      headlineSmall = baseTypography.headlineSmall.copy(fontSize = baseTypography.headlineSmall.fontSize * scale),
      titleLarge = baseTypography.titleLarge.copy(fontSize = baseTypography.titleLarge.fontSize * scale),
      titleMedium = baseTypography.titleMedium.copy(fontSize = baseTypography.titleMedium.fontSize * scale),
      bodyLarge = baseTypography.bodyLarge.copy(fontSize = baseTypography.bodyLarge.fontSize * scale),
      bodyMedium = baseTypography.bodyMedium.copy(fontSize = baseTypography.bodyMedium.fontSize * scale),
      bodySmall = baseTypography.bodySmall.copy(fontSize = baseTypography.bodySmall.fontSize * scale),
      labelLarge = baseTypography.labelLarge.copy(fontSize = baseTypography.labelLarge.fontSize * scale),
      labelMedium = baseTypography.labelMedium.copy(fontSize = baseTypography.labelMedium.fontSize * scale),
      labelSmall = baseTypography.labelSmall.copy(fontSize = baseTypography.labelSmall.fontSize * scale),
    )
  }
  MaterialTheme(
    colorScheme = LightColors,
    typography = typography,
    content = content,
  )
}
