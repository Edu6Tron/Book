package com.edu6tron.spiritualcompanion.nativepreview.media

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import com.edu6tron.spiritualcompanion.nativepreview.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NativeDevotionalPlayer @Inject constructor(
  @ApplicationContext context: Context,
) {
  private val player = ExoPlayer.Builder(context).build()

  fun play(uri: String) {
    player.setMediaItem(MediaItem.fromUri(uri))
    player.prepare()
    player.play()
  }

  fun playBundledFallback() {
    play(RawResourceDataSource.buildRawResourceUri(R.raw.devotional_alarm_fallback).toString())
  }

  fun stop() {
    player.stop()
  }

  fun release() {
    player.release()
  }
}
