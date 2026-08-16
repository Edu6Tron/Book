package com.edu6tron.spiritualcompanion.nativepreview.media

import androidx.annotation.RawRes
import com.edu6tron.spiritualcompanion.nativepreview.R

enum class OfflineSoundscape(
  val title: String,
  val subtitle: String,
  @RawRes val resourceId: Int,
) {
  SACRED_DAWN(
    title = "Sacred dawn",
    subtitle = "A gentle sunrise soundscape for reflection",
    resourceId = R.raw.sacred_dawn_ambience,
  ),
  FOCUSED_JAPA(
    title = "Focused japa",
    subtitle = "A steady, sparse bed for counting practice",
    resourceId = R.raw.focused_japa_ambience,
  ),
  EVENING_LAMP(
    title = "Evening lamp",
    subtitle = "A calm closing soundscape for evening prayer",
    resourceId = R.raw.evening_lamp_ambience,
  ),
  ;

  val playbackLabel: String get() = "$title ambience · offline"
}
