package com.edu6tron.spiritualcompanion.nativepreview.media

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.edu6tron.spiritualcompanion.nativepreview.R
import com.edu6tron.spiritualcompanion.nativepreview.diagnostics.NativeDiagnostics
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
  val positionMs: Long = 0L,
  val durationMs: Long = 0L,
)

@Singleton
class NativeDevotionalPlayer @Inject constructor(
  @ApplicationContext private val appContext: Context,
) {
  private companion object {
    const val PROGRESS_UPDATE_INTERVAL_MS = 500L
  }

  private val player = ExoPlayer.Builder(appContext).build()
  private val _playback = MutableStateFlow(DevotionalPlaybackState())
  val playback: StateFlow<DevotionalPlaybackState> = _playback.asStateFlow()
  private val progressHandler = Handler(Looper.getMainLooper())
  private val progressTick = object : Runnable {
    override fun run() {
      publishProgress()
      if (player.isPlaying) progressHandler.postDelayed(this, PROGRESS_UPDATE_INTERVAL_MS)
    }
  }

  init {
    player.addListener(object : Player.Listener {
      override fun onIsPlayingChanged(isPlaying: Boolean) {
        _playback.value = _playback.value.copy(isPlaying = isPlaying, message = null)
        if (isPlaying) {
          progressHandler.removeCallbacks(progressTick)
          progressHandler.post(progressTick)
        } else {
          progressHandler.removeCallbacks(progressTick)
          publishProgress()
        }
      }

      override fun onPlaybackStateChanged(playbackState: Int) {
        if (playbackState == Player.STATE_ENDED) {
          progressHandler.removeCallbacks(progressTick)
          _playback.value = _playback.value.copy(
            isPlaying = false,
            message = "Playback finished",
            positionMs = _playback.value.durationMs,
          )
        } else if (playbackState == Player.STATE_READY) {
          publishProgress()
        }
      }

      override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
        progressHandler.removeCallbacks(progressTick)
        _playback.value = _playback.value.copy(isPlaying = false, message = "This audio file could not be played. Choose another local audio file.")
      }
    })
  }

  fun play(uri: String, label: String = "Selected devotional audio") {
    _playback.value = DevotionalPlaybackState(sourceLabel = label, message = "Preparing audio…")
    runCatching {
      player.setMediaItem(MediaItem.fromUri(uri))
      player.prepare()
      player.play()
    }.onFailure { error ->
      NativeDiagnostics.recordFailure("local-playback", error)
      _playback.value = DevotionalPlaybackState(
        sourceLabel = label,
        message = "This audio file could not be opened. Choose another local audio file.",
      )
    }
  }

  fun playBundledFallback() {
    play(
      "android.resource://${appContext.packageName}/${R.raw.devotional_alarm_fallback}",
      "Bundled offline devotional tone",
    )
  }

  fun playOfflineSoundscape(soundscape: OfflineSoundscape) {
    play(
      "android.resource://${appContext.packageName}/${soundscape.resourceId}",
      soundscape.playbackLabel,
    )
  }

  fun stop() {
    progressHandler.removeCallbacks(progressTick)
    runCatching { player.stop() }
      .onFailure { NativeDiagnostics.recordFailure("stop-playback", it) }
    _playback.value = _playback.value.copy(isPlaying = false, message = "Playback stopped", positionMs = 0L)
  }

  fun release() {
    progressHandler.removeCallbacks(progressTick)
    runCatching { player.release() }
      .onFailure { NativeDiagnostics.recordFailure("release-playback", it) }
    _playback.value = DevotionalPlaybackState(message = "Playback closed")
  }

  private fun publishProgress() {
    val duration = player.duration.takeIf { it > 0L && it != androidx.media3.common.C.TIME_UNSET } ?: 0L
    _playback.value = _playback.value.copy(positionMs = player.currentPosition.coerceAtLeast(0L), durationMs = duration)
  }
}
