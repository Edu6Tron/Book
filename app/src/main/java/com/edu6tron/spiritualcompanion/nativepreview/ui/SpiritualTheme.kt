package com.edu6tron.spiritualcompanion.nativepreview.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.edu6tron.spiritualcompanion.nativepreview.data.ReadingComfort
import com.edu6tron.spiritualcompanion.nativepreview.data.ThemeMode

private val LightColors = lightColorScheme(
  primary = Color(0xFF9B4D0B),
  onPrimary = Color(0xFFFFFFFF),
  primaryContainer = Color(0xFFFFE3C5),
  onPrimaryContainer = Color(0xFF321200),
  secondary = Color(0xFF715A2B),
  onSecondary = Color(0xFFFFFFFF),
  secondaryContainer = Color(0xFFFFE9B8),
  onSecondaryContainer = Color(0xFF251A00),
  tertiary = Color(0xFF4F6750),
  onTertiary = Color(0xFFFFFFFF),
  tertiaryContainer = Color(0xFFD3EBD0),
  onTertiaryContainer = Color(0xFF0C2A12),
  background = Color(0xFFFFF9F1),
  onBackground = Color(0xFF231B13),
  surface = Color(0xFFFFFCF8),
  onSurface = Color(0xFF231B13),
  surfaceVariant = Color(0xFFF0E3D5),
  onSurfaceVariant = Color(0xFF51443A),
  outline = Color(0xFF857466),
)

private val DarkColors = darkColorScheme(
  primary = Color(0xFFFFB77B),
  onPrimary = Color(0xFF522300),
  primaryContainer = Color(0xFF723900),
  onPrimaryContainer = Color(0xFFFFDCC0),
  secondary = Color(0xFFE7C78C),
  onSecondary = Color(0xFF3B2F04),
  secondaryContainer = Color(0xFF554500),
  onSecondaryContainer = Color(0xFFFFEAB9),
  tertiary = Color(0xFFB8D1B3),
  onTertiary = Color(0xFF223A27),
  tertiaryContainer = Color(0xFF38513B),
  onTertiaryContainer = Color(0xFFD4ECCF),
  background = Color(0xFF1D1813),
  onBackground = Color(0xFFEFE3D8),
  surface = Color(0xFF272019),
  onSurface = Color(0xFFEFE3D8),
  surfaceVariant = Color(0xFF51443A),
  onSurfaceVariant = Color(0xFFD7C5B6),
  outline = Color(0xFFA08D7D),
)

private val DevotionalTypography = Typography(
  displayLarge = TextStyle(
    fontFamily = FontFamily.Serif,
    fontWeight = FontWeight.Bold,
    fontSize = 52.sp,
    lineHeight = 60.sp,
    letterSpacing = (-1.2).sp,
  ),
  displaySmall = TextStyle(
    fontFamily = FontFamily.Serif,
    fontWeight = FontWeight.Bold,
    fontSize = 36.sp,
    lineHeight = 42.sp,
    letterSpacing = (-0.6).sp,
  ),
  headlineSmall = TextStyle(
    fontFamily = FontFamily.Serif,
    fontWeight = FontWeight.Bold,
    fontSize = 26.sp,
    lineHeight = 32.sp,
  ),
  titleLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 28.sp),
  titleMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 24.sp),
  titleSmall = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 15.sp, lineHeight = 21.sp),
  bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp),
  bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 21.sp),
  bodySmall = TextStyle(fontSize = 12.sp, lineHeight = 18.sp),
  labelLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp),
  labelMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.2.sp),
  labelSmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 15.sp, letterSpacing = 0.2.sp),
)

@Composable
fun SpiritualCompanionNativeTheme(
  readingComfort: ReadingComfort = ReadingComfort.STANDARD,
  themeMode: ThemeMode = ThemeMode.LIGHT,
  content: @Composable () -> Unit,
) {
  val useDarkTheme = when (themeMode) {
    ThemeMode.LIGHT -> false
    ThemeMode.DARK -> true
    ThemeMode.SYSTEM -> isSystemInDarkTheme()
  }
  val typography = remember(readingComfort) {
    val scale = readingComfort.textScale
    DevotionalTypography.copy(
      displayLarge = DevotionalTypography.displayLarge.copy(fontSize = DevotionalTypography.displayLarge.fontSize * scale),
      displayMedium = DevotionalTypography.displayMedium.copy(fontSize = DevotionalTypography.displayMedium.fontSize * scale),
      displaySmall = DevotionalTypography.displaySmall.copy(fontSize = DevotionalTypography.displaySmall.fontSize * scale),
      headlineLarge = DevotionalTypography.headlineLarge.copy(fontSize = DevotionalTypography.headlineLarge.fontSize * scale),
      headlineMedium = DevotionalTypography.headlineMedium.copy(fontSize = DevotionalTypography.headlineMedium.fontSize * scale),
      headlineSmall = DevotionalTypography.headlineSmall.copy(fontSize = DevotionalTypography.headlineSmall.fontSize * scale),
      titleLarge = DevotionalTypography.titleLarge.copy(fontSize = DevotionalTypography.titleLarge.fontSize * scale),
      titleMedium = DevotionalTypography.titleMedium.copy(fontSize = DevotionalTypography.titleMedium.fontSize * scale),
      titleSmall = DevotionalTypography.titleSmall.copy(fontSize = DevotionalTypography.titleSmall.fontSize * scale),
      bodyLarge = DevotionalTypography.bodyLarge.copy(fontSize = DevotionalTypography.bodyLarge.fontSize * scale),
      bodyMedium = DevotionalTypography.bodyMedium.copy(fontSize = DevotionalTypography.bodyMedium.fontSize * scale),
      bodySmall = DevotionalTypography.bodySmall.copy(fontSize = DevotionalTypography.bodySmall.fontSize * scale),
      labelLarge = DevotionalTypography.labelLarge.copy(fontSize = DevotionalTypography.labelLarge.fontSize * scale),
      labelMedium = DevotionalTypography.labelMedium.copy(fontSize = DevotionalTypography.labelMedium.fontSize * scale),
      labelSmall = DevotionalTypography.labelSmall.copy(fontSize = DevotionalTypography.labelSmall.fontSize * scale),
    )
  }
  MaterialTheme(
    colorScheme = if (useDarkTheme) DarkColors else LightColors,
    typography = typography,
    content = content,
  )
}
