package com.edu6tron.spiritualcompanion.nativepreview.data

/**
 * A local-only colour collection. Appearance mode chooses light, dark, or system contrast;
 * this preference chooses the devotional palette used within that appearance.
 */
enum class DevotionalTheme(
  val storedValue: String,
  val label: String,
  val description: String,
  val previewPrimaryArgb: Long,
  val previewAccentArgb: Long,
) {
  SACRED_SAFFRON(
    "sacred_saffron",
    "Sacred saffron",
    "Temple lamps and morning prayer",
    0xFF9B4D0BL,
    0xFFFFE3C5L,
  ),
  TEMPLE_LOTUS(
    "temple_lotus",
    "Temple lotus",
    "Rose petals and quiet offering",
    0xFF9B3158L,
    0xFFFFD9E4L,
  ),
  KRISHNA_TWILIGHT(
    "krishna_twilight",
    "Krishna twilight",
    "Indigo sky and flute-song calm",
    0xFF384D8DL,
    0xFFDEE6FFL,
  ),
  GANGA_DAWN(
    "ganga_dawn",
    "Ganga dawn",
    "Clear water and first light",
    0xFF006876L,
    0xFFA5F2FFL,
  ),
  TULSI_GROVE(
    "tulsi_grove",
    "Tulsi grove",
    "Leafy stillness and gentle growth",
    0xFF41693DL,
    0xFFC1F0B8L,
  ),
  HIMALAYAN_MIST(
    "himalayan_mist",
    "Himalayan mist",
    "Stone, sky, and clear air",
    0xFF50616FL,
    0xFFD8E7F2L,
  ),
  VITHOBA_INDIGO(
    "vithoba_indigo",
    "Vithoba indigo",
    "Pilgrimage blue and evening song",
    0xFF51458CL,
    0xFFE4DFFFL,
  ),
  DEEPA_EMBER(
    "deepa_ember",
    "Deepa ember",
    "A steady diya after sunset",
    0xFFA44300L,
    0xFFFFDCC5L,
  ),
  MONSOON_PRAYER(
    "monsoon_prayer",
    "Monsoon prayer",
    "Rain-washed teal and reflection",
    0xFF006A64L,
    0xFFA0F3E9L,
  ),
  ROSE_SANDAL(
    "rose_sandal",
    "Rose sandal",
    "Sandalwood warmth and rose devotion",
    0xFF874B3CL,
    0xFFFFDBD2L,
  ),
  MOONLIT_SILVER(
    "moonlit_silver",
    "Moonlit silver",
    "Cool moonlight and quiet mantra",
    0xFF536062L,
    0xFFDCE5E7L,
  ),
  ;

  companion object {
    fun fromStored(value: String?): DevotionalTheme =
      entries.firstOrNull { it.storedValue == value } ?: SACRED_SAFFRON
  }
}
