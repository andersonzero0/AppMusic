package com.andersonzero0.appmusic.data.view_model.music

import android.content.Context

sealed class MusicUiEvent {
    data class OnFetchMusics(val context: Context) : MusicUiEvent()
    data class OnSelectMusic(val musicId: Long) : MusicUiEvent()
    data class OnSearch(val query: String) : MusicUiEvent()
}