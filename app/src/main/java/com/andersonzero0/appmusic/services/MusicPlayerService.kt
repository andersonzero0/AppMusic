package com.andersonzero0.appmusic.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationCompat
import com.andersonzero0.appmusic.MainActivity
import com.andersonzero0.appmusic.R
import com.andersonzero0.appmusic.data.enums.MusicModeEnum
import com.andersonzero0.appmusic.data.model.Music
import com.andersonzero0.appmusic.ui.theme.colorMusic
import com.andersonzero0.appmusic.ui.theme.primaryDark
import java.util.Locale

class MusicPlayerService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var currentMusic: Music? = null
    private var queueMusic: List<Music>? = null
    private var tempQueueMusic: List<Music>? = null
    private var isPrepared = false
    private lateinit var mediaSession: MediaSessionCompat
    private var mode: MusicModeEnum = MusicModeEnum.NORMAL

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var updateRunnable: Runnable

    init {
        updateRunnable = object : Runnable {
            override fun run() {
                if (isPlaying()) {
                    val playbackState = PlaybackStateCompat.Builder()
                        .setActions(
                            PlaybackStateCompat.ACTION_PLAY_PAUSE or
                                    PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                                    PlaybackStateCompat.ACTION_SEEK_TO
                        )
                        .setState(
                            PlaybackStateCompat.STATE_PLAYING,
                            mediaPlayer?.currentPosition?.toLong() ?: 0,
                            1.0f,
                            SystemClock.elapsedRealtime()
                        )
                        .build()
                    mediaSession.setPlaybackState(playbackState)
                    updateNotification()
                    handler.postDelayed(this, UPDATE_INTERVAL)
                }
            }
        }
    }

    var onMusicChangeListener: ((Music?) -> Unit)? = null

    companion object {
        const val ACTION_PLAY = "action_play"
        const val ACTION_PAUSE = "action_pause"
        const val ACTION_NEXT = "action_next"
        const val ACTION_PREV = "action_prev"
        const val ACTION_STOP = "action_stop"
        const val CHANNEL_ID = "music_channel"
        private const val UPDATE_INTERVAL = 1000L
        const val NOTIFICATION_ID = 101
    }

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        mediaSession = MediaSessionCompat(this, "MusicPlayerService")
        mediaSession.isActive = true
        mediaPlayer = MediaPlayer().apply {
            isLooping = true
        }
        mediaSession.setCallback(object : MediaSessionCompat.Callback() {
            override fun onPlay() = play()
            override fun onPause() = pause()
            override fun onSkipToNext() = skipToNext()
            override fun onSkipToPrevious() = skipToPrevious()
            override fun onStop() = stopSelf()
            override fun onSeekTo(pos: Long) {
                seekTo(pos.toInt())
            }
        })

        updateRunnable = object : Runnable {
            override fun run() {
                if (isPlaying()) {
                    updateNotification()
                    handler.postDelayed(this, UPDATE_INTERVAL)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaSession.release()
        handler.removeCallbacks(updateRunnable)
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun onBind(intent: Intent?): IBinder = binder

    fun playMusic(music: Music, queueMusic: List<Music>? = null, modeEnum: MusicModeEnum = mode) {
        modeEnum.let { mode = it }

        currentMusic = music

        queueMusic.let {
            this.queueMusic = it
            tempQueueMusic = it
        }

        when (modeEnum) {
            MusicModeEnum.SHUFFLE -> {
                queueMusic?.let { list ->
                    this.tempQueueMusic = list.filter { it.id != music.id }
                        .shuffled()
                        .let { shuffledList -> listOf(music) + shuffledList }
                }
            }
            MusicModeEnum.REPEAT_ONE -> {
                queueMusic?.let {
                    this.tempQueueMusic = listOf(music)
                }
            }
            else -> {
                queueMusic?.let {
                    this.tempQueueMusic = it
                }
            }
        }

        onMusicChangeListener?.invoke(currentMusic)

        updateNotification()

        mediaPlayer?.apply {
            reset()
            setDataSource(music.path)
            prepareAsync()
            setOnPreparedListener {
                isPrepared = true
                start()
                val metadata = MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, music.title)
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, music.artist)
                    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration.toLong())
                    .build()
                mediaSession.setMetadata(metadata)
                updateNotification()
            }
            setOnCompletionListener {
                skipToNext()
//                handler.post { updateNotification() }
            }
        }
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    fun getQueueMusic(): List<Music>? {
        return tempQueueMusic
    }

    fun changeMode(modeEnum: MusicModeEnum) {
        mode = modeEnum

        tempQueueMusic = when (modeEnum) {
            MusicModeEnum.NORMAL -> {
                queueMusic
            }

            MusicModeEnum.SHUFFLE -> {
                queueMusic?.let { list ->
                    list.filter { it.id != currentMusic?.id }
                        .shuffled()
                        .let { shuffledList -> listOf(currentMusic!!) + shuffledList }
                }
            }

            MusicModeEnum.REPEAT_ONE -> {
                listOf(currentMusic!!)
            }
        }
    }

    fun play() {
        if (!isPrepared && currentMusic != null) {
            playMusic(currentMusic!!, modeEnum = mode)
        } else {
            mediaPlayer?.start()
            updateNotification()
            handler.post(updateRunnable)
        }
    }

    fun pause() {
        mediaPlayer?.pause()
        updateNotification()
        handler.removeCallbacks(updateRunnable)
    }

    private fun getNextMusic(): Music? {
        val currentIndex = tempQueueMusic?.indexOf(currentMusic)
        return if (currentIndex != null && currentIndex < tempQueueMusic!!.size - 1) {
            tempQueueMusic!![currentIndex + 1]
        } else {
            tempQueueMusic?.firstOrNull()
        }
    }

    private fun getPreviousMusic(): Music? {
        val currentIndex = tempQueueMusic?.indexOf(currentMusic)
        return if (currentIndex != null && currentIndex > 0) {
            tempQueueMusic!![currentIndex - 1]
        } else {
            tempQueueMusic?.lastOrNull()
        }
    }

    fun hasNextMusic(): Boolean {
        return getNextMusic() != null
    }

    fun hasPreviousMusic(): Boolean {
        return getPreviousMusic() != null
    }

    fun skipToNext() {
        val nextMusic = getNextMusic()
        nextMusic?.let { playMusic(it, queueMusic = queueMusic, modeEnum = mode) }
    }

    fun skipToPrevious() {
        val prevMusic = getPreviousMusic()
        prevMusic?.let { playMusic(it, queueMusic = queueMusic, modeEnum = mode) }
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
        updateNotification()
    }

    fun getCurrentPosition() = mediaPlayer?.currentPosition ?: 0
    fun getDuration() = mediaPlayer?.duration ?: 0

    fun getCurrentMusic(): Music? {
        return currentMusic
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Reprodução de Música",
                NotificationManager.IMPORTANCE_DEFAULT // Alterado para DEFAULT
            ).apply {
                description = "Canal para controle de reprodução musical"
                setSound(null, null)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                setShowBadge(false)
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        val isPlaying = isPlaying()
        val duration = getDuration().toLong()
        val currentPosition = getCurrentPosition().toLong()

        val playbackState = PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_PLAY_PAUSE or
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                        PlaybackStateCompat.ACTION_SEEK_TO
            )
            .setState(
                if (isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED,
                currentPosition,
                1.0f,
                SystemClock.elapsedRealtime()
            )
            .build()

        mediaSession.setPlaybackState(playbackState)

        val metadata = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, currentMusic?.title)
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, currentMusic?.artist)
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
            .build()
        mediaSession.setMetadata(metadata)

        val playPauseAction = NotificationCompat.Action(
            if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play,
            if (isPlaying) "Pausar" else "Tocar",
            getPendingIntent(if (isPlaying) ACTION_PAUSE else ACTION_PLAY)
        )

        val previousAction = NotificationCompat.Action(
            android.R.drawable.ic_media_previous,
            "Anterior",
            getPendingIntent(ACTION_PREV)
        )

        val nextAction = NotificationCompat.Action(
            android.R.drawable.ic_media_next,
            "Próxima",
            getPendingIntent(ACTION_NEXT)
        )

        val notificationColor = if (colorMusic == Color.Unspecified) primaryDark else colorMusic

        val coverUri: Uri? = currentMusic?.albumArtUri
        val largeIcon = coverUri?.let { loadBitmapFromUri(it) }
            ?: BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_foreground)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle(currentMusic?.title ?: "Sem música")
            .setContentText(currentMusic?.artist ?: "Artista desconhecido")
            .setLargeIcon(largeIcon)
            .setColorized(true)
            .setColor(notificationColor.toArgb())
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
                    .setMediaSession(mediaSession.sessionToken)
            )
            .addAction(previousAction)
            .addAction(playPauseAction)
            .addAction(nextAction)
            .setContentIntent(getContentIntent())
            .setDeleteIntent(getStopPendingIntent())
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    @SuppressLint("ForegroundServiceType")
    private fun updateNotification() {
        val notification = buildNotification()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification, FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
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

    private fun getPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, MusicPlayerService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(
            this,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getStopPendingIntent(): PendingIntent {
        val stopIntent = Intent(this, MusicPlayerService::class.java).apply {
            action = ACTION_STOP
        }
        return PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getContentIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        return PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun loadBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

