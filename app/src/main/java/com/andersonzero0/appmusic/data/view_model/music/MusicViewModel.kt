package com.andersonzero0.appmusic.data.view_model.music

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andersonzero0.appmusic.services.AudioService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MusicViewModel(
) : ViewModel() {
    private val _uiState = MutableStateFlow(MusicUiState())
    val uiState: StateFlow<MusicUiState> = _uiState.asStateFlow()

    private val audioService: AudioService = AudioService()

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
}