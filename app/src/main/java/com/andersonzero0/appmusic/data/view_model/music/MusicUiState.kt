package com.andersonzero0.appmusic.data.view_model.music

import com.andersonzero0.appmusic.data.model.Music

data class MusicUiState(
    val musics: List<Music> = emptyList(),
    val tempMusics: List<Music> = emptyList(),
    val hasNextMusic: Boolean = false,
    val hasPreviousMusic: Boolean = false,
)