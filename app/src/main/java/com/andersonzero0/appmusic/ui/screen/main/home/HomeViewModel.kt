package com.andersonzero0.appmusic.ui.screen.main.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andersonzero0.appmusic.core.storage.AudioService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val audioService: AudioService = AudioService()

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnFetchMusics -> fetchMusics(event.context)
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
}