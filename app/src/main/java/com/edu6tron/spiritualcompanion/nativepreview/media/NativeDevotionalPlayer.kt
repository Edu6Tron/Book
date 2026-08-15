package com.edu6tron.spiritualcompanion.nativepreview.media

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.edu6tron.spiritualcompanion.nativepreview.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DevotionalPlaybackState(
  val isPlaying: Boolean = false,
  val sourceLabel: String? = null,
  val message: String? = null,
)

@Singleton
class NativeDevotionalPlayer @Inject constructor(
  @ApplicationContext private val appContext: Context,
) {
  private val player = ExoPlayer.Builder(appContext).build()
  private val _playback = MutableStateFlow(DevotionalPlaybackState())
  val playback: StateFlow<DevotionalPlaybackState> = _playback.asStateFlow()

  init {
    player.addListener(object : Player.Listener {
      override fun onIsPlayingChanged(isPlaying: Boolean) {
        _playback.value = _playback.value.copy(isPlaying = isPlaying, message = null)
      }

      override fun onPlaybackStateChanged(playbackState: Int) {
        if (playbackState == Player.STATE_ENDED) {
          _playback.value = _playback.value.copy(isPlaying = false, message = "Playback finished")
        }
      }

      override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
        _playback.value = _playback.value.copy(isPlaying = false, message = "This audio file could not be played. Choose another local audio file.")
      }
    })
  }

  fun play(uri: String, label: String = "Selected devotional audio") {
    _playback.value = DevotionalPlaybackState(sourceLabel = label, message = "Preparing audio…")
    player.setMediaItem(MediaItem.fromUri(uri))
    player.prepare()
    player.play()
  }

  fun playBundledFallback() {
    play(
      "android.resource://${appContext.packageName}/${R.raw.devotional_alarm_fallback}",
      "Bundled offline devotional tone",
    )
  }

  fun stop() {
    player.stop()
    _playback.value = _playback.value.copy(isPlaying = false, message = "Playback stopped")
  }

  fun release() {
    player.release()
    _playback.value = DevotionalPlaybackState(message = "Playback closed")
  }
}
