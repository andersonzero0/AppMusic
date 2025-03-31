package com.andersonzero0.appmusic.data.view_model.music

import android.annotation.SuppressLint
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.andersonzero0.appmusic.data.model.Music
import com.andersonzero0.appmusic.services.AudioService
import com.andersonzero0.appmusic.services.MusicPlayerService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MusicViewModel(
    application: Application
) : AndroidViewModel(application) {
    private var serviceBound = false

    private val audioService: AudioService = AudioService()
    @SuppressLint("StaticFieldLeak")
    private var musicService: MusicPlayerService? = null

    private val _uiState = MutableStateFlow(MusicUiState())
    val uiState: StateFlow<MusicUiState> = _uiState.asStateFlow()

    private val _isPlayingState = MutableStateFlow(false)
    val isPlayingState: StateFlow<Boolean> = _isPlayingState.asStateFlow()

    private val _currentPositionState = MutableStateFlow(0)
    val currentPositionState: StateFlow<Int> = _currentPositionState.asStateFlow()

    private var positionUpdaterJob: Job? = null

    private fun startPositionUpdater() {
        positionUpdaterJob?.cancel()
        positionUpdaterJob = viewModelScope.launch {
            while (true) {
                _currentPositionState.value = musicService?.getCurrentPosition() ?: 0
                delay(500)
            }
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MusicPlayerService.LocalBinder
            musicService = binder.getService()
            serviceBound = true
            startPositionUpdater()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            serviceBound = false
            musicService = null
            positionUpdaterJob?.cancel()
        }
    }

    fun bindService(context: Context) {
        Intent(context, MusicPlayerService::class.java).also { intent ->
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
            context.startService(intent)
        }
    }

    fun onEvent(event: MusicUiEvent) {
        when (event) {
            is MusicUiEvent.OnFetchMusics -> fetchMusics(event.context)
            is MusicUiEvent.OnSelectMusic -> onMusicSelected(event.musicId)
            is MusicUiEvent.OnSearch -> searchMusics(event.query)
        }
    }

    private fun fetchMusics(context: Context) {

        viewModelScope.launch {
            _uiState.update { currentUiState ->
                audioService.getAllAudios(context).fold(
                    onSuccess = { musics ->
                        currentUiState.copy(
                            musics = musics
                        )
                    },
                    onFailure = { _ ->
                        currentUiState.copy(
                            musics = emptyList()
                        )
                    }
                )
            }
        }
    }

    private fun searchMusics(query: String) {
        viewModelScope.launch {
            _uiState.update { currentUiState ->
                val filteredMusics = currentUiState.musics.filter { music ->
                    music.title.contains(query, ignoreCase = true)
                }
                currentUiState.copy(
                    tempMusics = filteredMusics
                )
            }
        }
    }

    private fun onMusicSelected(musicId: Long) {
        viewModelScope.launch {
            _uiState.update { currentUiState ->
                val selectedMusic = currentUiState.musics.find { it.id == musicId }
                currentUiState.copy(
                    selectedMusic = selectedMusic
                )
            }
        }
    }

    fun isPlaying(): Boolean {
        return musicService?.isPlaying() ?: false
    }

    fun playPause() {
        if (musicService?.isPlaying() == true) {
            musicService?.pause()
            _isPlayingState.value = false
        } else {
            musicService?.play()
            _isPlayingState.value = true
        }
    }

    fun playMusic(music: Music) {
        musicService?.playMusic(music)
        _isPlayingState.value = true
    }

    fun getDuration(): Int {
        return musicService?.getDuration() ?: 0
    }

    fun getCurrentMusic(): Music? {
        return musicService?.getCurrentMusic()
    }

    fun seekTo(position: Int) {
        musicService?.seekTo(position)
    }

    override fun onCleared() {
        super.onCleared()
        if (serviceBound) {
            getApplication<Application>().unbindService(connection)
            serviceBound = false
        }
    }
}