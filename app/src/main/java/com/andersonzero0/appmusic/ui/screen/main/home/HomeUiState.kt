package com.andersonzero0.appmusic.ui.screen.main.home

import com.andersonzero0.appmusic.data.model.Music

data class HomeUiState(
    val musics: List<Music> = emptyList(),
)