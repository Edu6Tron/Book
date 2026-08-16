package com.edu6tron.spiritualcompanion.nativepreview.data

enum class ThemeMode(
  val storedValue: String,
  val label: String,
) {
  LIGHT("light", "Light"),
  DARK("dark", "Dark"),
  SYSTEM("system", "Use device setting"),
  ;

  companion object {
    fun fromStored(value: String?): ThemeMode = entries.firstOrNull { it.storedValue == value } ?: LIGHT
  }
}
