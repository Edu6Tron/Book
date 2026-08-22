package com.edu6tron.spiritualcompanion.nativepreview.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.edu6tron.spiritualcompanion.nativepreview.R
import com.edu6tron.spiritualcompanion.nativepreview.data.RitualAlarmEntity
import com.edu6tron.spiritualcompanion.nativepreview.diagnostics.NativeDiagnostics

class RitualAlarmService : Service() {
  private var player: MediaPlayer? = null
  private var activeAlarm: RitualAlarmEntity? = null

  override fun onBind(intent: Intent?): IBinder? = null

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    val alarm = intent?.let(RitualAlarmScheduler::alarmFrom) ?: return START_NOT_STICKY
    return when (intent.action) {
      RitualAlarmScheduler.ACTION_FIRE -> {
        if (!startAlarm(alarm)) return START_NOT_STICKY
        // Keep an active alarm recoverable if the process is reclaimed while the display is off.
        // Explicit Stop and Snooze actions call stopSelf(), so they are never resurrected.
        START_REDELIVER_INTENT
      }
      RitualAlarmScheduler.ACTION_SNOOZE -> {
        stopAlarm()
        RitualAlarmScheduler.snooze(this, alarm, intent.getIntExtra("ritual_alarm_snooze_minutes", 5))
        START_NOT_STICKY
      }
      RitualAlarmScheduler.ACTION_STOP -> {
        stopAlarm()
        START_NOT_STICKY
      }
      else -> START_NOT_STICKY
    }
  }

  private fun startAlarm(alarm: RitualAlarmEntity): Boolean {
    activeAlarm = alarm
    createChannel()
    try {
      startForeground(NOTIFICATION_ID, buildNotification(alarm))
      AlarmDeliveryDiagnostics.record(this, AlarmDeliveryStage.FOREGROUND_SERVICE_STARTED)
    } catch (error: Exception) {
      AlarmDeliveryDiagnostics.record(this, AlarmDeliveryStage.FOREGROUND_SERVICE_FAILED)
      NativeDiagnostics.recordFailure("alarm_foreground_service", error)
      activeAlarm = null
      stopSelf()
      return false
    }
    player?.release()
    val preparedPlayer = try {
      createAlarmPlayer(alarm.toneUri)
    } catch (error: Exception) {
      AlarmDeliveryDiagnostics.record(this, AlarmDeliveryStage.PLAYBACK_FAILED)
      NativeDiagnostics.recordFailure("alarm_player_prepare", error)
      activeAlarm = null
      stopForeground(STOP_FOREGROUND_REMOVE)
      stopSelf()
      return false
    }
    player = preparedPlayer.player.also { alarmPlayer ->
      alarmPlayer.setOnErrorListener { _, _, _ ->
        startBundledFallback(alarmPlayer)
        true
      }
      startPreparedTone(alarmPlayer, preparedPlayer.source)
    }
    return true
  }

  private fun createAlarmPlayer(toneUri: String?): PreparedAlarmPlayer {
    val player = MediaPlayer().apply {
      setAudioAttributes(
        AudioAttributes.Builder()
          .setUsage(AudioAttributes.USAGE_ALARM)
          .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
          .build(),
      )
      isLooping = true
      setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
    }
    if (!toneUri.isNullOrBlank()) {
      try {
        player.setDataSource(this, Uri.parse(toneUri))
        player.prepare()
        return PreparedAlarmPlayer(player, ToneSource.LOCAL)
      } catch (_: Exception) {
        player.reset()
      }
    }
    prepareBundledFallback(player)
    return PreparedAlarmPlayer(player, ToneSource.FALLBACK)
  }

  private fun startPreparedTone(player: MediaPlayer, source: ToneSource) {
    try {
      player.start()
      AlarmDeliveryDiagnostics.record(this, source.startedStage)
    } catch (error: Exception) {
      NativeDiagnostics.recordFailure("alarm_tone_start", error)
      startBundledFallback(player)
    }
  }

  private fun startBundledFallback(player: MediaPlayer) {
    try {
      player.reset()
      prepareBundledFallback(player)
      player.start()
      AlarmDeliveryDiagnostics.record(this, AlarmDeliveryStage.FALLBACK_TONE_STARTED)
    } catch (error: Exception) {
      AlarmDeliveryDiagnostics.record(this, AlarmDeliveryStage.PLAYBACK_FAILED)
      NativeDiagnostics.recordFailure("alarm_fallback_start", error)
      stopAlarm()
    }
  }

  private fun prepareBundledFallback(target: MediaPlayer) {
    val descriptor = resources.openRawResourceFd(R.raw.devotional_alarm_fallback)
      ?: error("Bundled devotional fallback is missing")
    target.setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
    descriptor.close()
    target.prepare()
  }

  private fun createChannel() {
    val channel = NotificationChannel(CHANNEL_ID, "Ritual alarms", NotificationManager.IMPORTANCE_HIGH).apply {
      setSound(null, null)
      lockscreenVisibility = Notification.VISIBILITY_PUBLIC
    }
    getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
  }

  private fun buildNotification(alarm: RitualAlarmEntity): Notification {
    val fullScreen = PendingIntent.getActivity(
      this,
      alarm.id.hashCode(),
      Intent(this, RitualAlarmActivity::class.java).apply { with(RitualAlarmScheduler) { putAlarmExtras(alarm) } }
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP),
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
    val snooze = PendingIntent.getBroadcast(
      this,
      alarm.id.hashCode() + 1,
      Intent(this, RitualAlarmReceiver::class.java).setAction(RitualAlarmScheduler.ACTION_SNOOZE).apply { with(RitualAlarmScheduler) { putAlarmExtras(alarm) } },
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
    val stop = PendingIntent.getBroadcast(
      this,
      alarm.id.hashCode() + 2,
      Intent(this, RitualAlarmReceiver::class.java).setAction(RitualAlarmScheduler.ACTION_STOP).apply { with(RitualAlarmScheduler) { putAlarmExtras(alarm) } },
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
    return NotificationCompat.Builder(this, CHANNEL_ID)
      .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
      .setContentTitle("Ritual alarm")
      .setContentText(alarm.label)
      .setCategory(NotificationCompat.CATEGORY_ALARM)
      .setPriority(NotificationCompat.PRIORITY_MAX)
      .setOngoing(true)
      .setAutoCancel(false)
      .setContentIntent(fullScreen)
      .setFullScreenIntent(fullScreen, true)
      .addAction(0, "Snooze 5 min", snooze)
      .addAction(0, "Stop", stop)
      .build()
  }

  private fun stopAlarm() {
    player?.run {
      if (isPlaying) stop()
      release()
    }
    player = null
    activeAlarm = null
    stopForeground(STOP_FOREGROUND_REMOVE)
    stopSelf()
  }

  override fun onDestroy() {
    player?.release()
    player = null
    super.onDestroy()
  }

  override fun onTaskRemoved(rootIntent: Intent?) {
    // The alarm belongs to its foreground service, not to the visible activity task. Reasserting
    // the foreground notification keeps an already-ringing alarm active if the main app task is
    // dismissed while the screen is off.
    activeAlarm?.takeIf { player?.isPlaying == true }?.let { alarm ->
      startForeground(NOTIFICATION_ID, buildNotification(alarm))
    }
    super.onTaskRemoved(rootIntent)
  }

  companion object {
    private const val CHANNEL_ID = "ritual-alarms-native-v1"
    private const val NOTIFICATION_ID = 9034
  }

  private data class PreparedAlarmPlayer(
    val player: MediaPlayer,
    val source: ToneSource,
  )

  private enum class ToneSource(val startedStage: AlarmDeliveryStage) {
    LOCAL(AlarmDeliveryStage.LOCAL_TONE_STARTED),
    FALLBACK(AlarmDeliveryStage.FALLBACK_TONE_STARTED),
  }
}
