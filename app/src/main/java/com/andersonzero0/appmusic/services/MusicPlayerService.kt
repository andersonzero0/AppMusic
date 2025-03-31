package com.andersonzero0.appmusic.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.andersonzero0.appmusic.data.model.Music
import java.util.Locale

class MusicPlayerService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var currentMusic: Music? = null
    private var isPrepared = false

    companion object {
        const val ACTION_PLAY = "action_play"
        const val ACTION_PAUSE = "action_pause"
        const val ACTION_NEXT = "action_next"
        const val ACTION_PREV = "action_prev"
        const val ACTION_STOP = "action_stop"
        const val CHANNEL_ID = "music_channel"
        const val NOTIFICATION_ID = 101
    }

    override fun onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer();
        mediaPlayer?.isLooping = true;
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    fun playMusic(music: Music) {
        currentMusic = music

        mediaPlayer?.apply {
            reset()
            setDataSource(music.path)
            prepareAsync()
            setOnPreparedListener {
                isPrepared = true
                start()
            }
            setOnCompletionListener {
                skipToNext()
            }
        }
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    fun play() {
        if (!isPrepared && currentMusic != null) {
            playMusic(currentMusic!!)
        } else {
            mediaPlayer?.start()
        }
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun skipToNext() {
        // Implement your logic to get next music
        // val nextMusic = getNextMusic()
        // nextMusic?.let { playMusic(it) }
    }

    fun skipToPrevious() {
        // Implement your logic to get previous music
        // val prevMusic = getPreviousMusic()
        // prevMusic?.let { playMusic(it) }
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun getCurrentPosition() = mediaPlayer?.currentPosition ?: 0
    fun getDuration() = mediaPlayer?.duration ?: 0

    fun getCurrentPositionFormatted(): String {
        val currentPosition = getCurrentPosition()
        val minutes = (currentPosition / 1000) / 60
        val seconds = (currentPosition / 1000) % 60
        return String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds)
    }

    fun getCurrentMusic(): Music? {
        return currentMusic
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleIntent(intent)
        return START_STICKY
    }

    private fun handleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_PLAY -> play()
            ACTION_PAUSE -> pause()
            ACTION_NEXT -> skipToNext()
            ACTION_PREV -> skipToPrevious()
            ACTION_STOP -> stopSelf()
        }
    }

}