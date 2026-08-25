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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DevotionalPlaybackState(
  val isPlaying: Boolean = false,
  val sourceUri: String? = null,
  val sourceLabel: String? = null,
  val message: String? = null,
  val positionMs: Long = 0L,
  val durationMs: Long = 0L,
  val isReleased: Boolean = false,
)

class NativeDevotionalPlayer @Inject constructor(
  @ApplicationContext private val appContext: Context,
) {
  private companion object {
    const val PROGRESS_UPDATE_INTERVAL_MS = 250L
    const val SEEK_STEP_MS = 15_000L
  }

  private val player = ExoPlayer.Builder(appContext).build()
  private val _playback = MutableStateFlow(DevotionalPlaybackState())
  val playback: StateFlow<DevotionalPlaybackState> = _playback.asStateFlow()
  private val progressHandler = Handler(Looper.getMainLooper())
  private var released = false
  private val progressTick = object : Runnable {
    override fun run() {
      if (released) return
      publishProgress()
      if (player.isPlaying) progressHandler.postDelayed(this, PROGRESS_UPDATE_INTERVAL_MS)
    }
  }

  init {
    player.addListener(object : Player.Listener {
      override fun onIsPlayingChanged(isPlaying: Boolean) {
        if (released) return
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
        if (released) return
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
        if (released) return
        progressHandler.removeCallbacks(progressTick)
        _playback.value = _playback.value.copy(isPlaying = false, message = "This audio file could not be played. Choose another local audio file.")
      }
    })
  }

  fun play(uri: String, label: String = "Selected devotional audio") {
    if (released) {
      _playback.value = DevotionalPlaybackState(
        sourceLabel = label,
        message = "Playback is unavailable because this screen is closing.",
        isReleased = true,
      )
      return
    }
    _playback.value = DevotionalPlaybackState(sourceUri = uri, sourceLabel = label, message = "Preparing audio…")
    runCatching {
      player.setMediaItem(MediaItem.fromUri(uri))
      player.prepare()
      player.play()
    }.onFailure { error ->
      NativeDiagnostics.recordFailure("local-playback", error)
      _playback.value = DevotionalPlaybackState(
        sourceUri = uri,
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
    if (released) return
    progressHandler.removeCallbacks(progressTick)
    runCatching { player.stop() }
      .onFailure { NativeDiagnostics.recordFailure("stop-playback", it) }
    _playback.value = _playback.value.copy(isPlaying = false, message = "Playback stopped", positionMs = 0L)
  }

  fun togglePlayPause() {
    if (released || player.mediaItemCount == 0) return
    runCatching {
      if (player.isPlaying) {
        player.pause()
      } else {
        if (player.playbackState == Player.STATE_ENDED) {
          player.seekTo(0L)
        }
        player.play()
      }
    }.onFailure { error ->
      NativeDiagnostics.recordFailure("toggle-local-playback", error)
      _playback.value = _playback.value.copy(
        isPlaying = false,
        message = "Playback could not continue. Choose another local audio file.",
      )
    }
  }

  fun seekTo(positionMs: Long) {
    if (released || player.mediaItemCount == 0) return
    runCatching {
      val duration = player.duration.takeIf { it > 0L && it != androidx.media3.common.C.TIME_UNSET }
      val boundedPosition = duration?.let { positionMs.coerceIn(0L, it) } ?: positionMs.coerceAtLeast(0L)
      player.seekTo(boundedPosition)
      publishProgress()
    }.onFailure { error ->
      NativeDiagnostics.recordFailure("seek-local-playback", error)
      _playback.value = _playback.value.copy(message = "This audio position is unavailable.")
    }
  }

  fun seekBy(deltaMs: Long) {
    if (released || player.mediaItemCount == 0) return
    seekTo(player.currentPosition + deltaMs.coerceIn(-SEEK_STEP_MS, SEEK_STEP_MS))
  }

  fun release() {
    if (released) return
    released = true
    progressHandler.removeCallbacks(progressTick)
    runCatching { player.release() }
      .onFailure { NativeDiagnostics.recordFailure("release-playback", it) }
    _playback.value = DevotionalPlaybackState(message = "Playback closed", isReleased = true)
  }

  private fun publishProgress() {
    if (released) return
    val duration = player.duration.takeIf { it > 0L && it != androidx.media3.common.C.TIME_UNSET } ?: 0L
    _playback.value = _playback.value.copy(positionMs = player.currentPosition.coerceAtLeast(0L), durationMs = duration)
  }
}
