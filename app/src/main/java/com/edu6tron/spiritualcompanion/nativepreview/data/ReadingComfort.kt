package com.edu6tron.spiritualcompanion.nativepreview.data

enum class ReadingComfort(
  val storedValue: String,
  val label: String,
  val textScale: Float,
) {
  COMPACT("compact", "Compact", 0.92f),
  STANDARD("standard", "Standard", 1.0f),
  LARGE("large", "Large", 1.14f),
  ;

  companion object {
    fun fromStored(value: String?): ReadingComfort = entries.firstOrNull { it.storedValue == value } ?: STANDARD
  }
}
