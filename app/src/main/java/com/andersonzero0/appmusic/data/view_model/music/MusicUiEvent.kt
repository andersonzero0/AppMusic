package com.andersonzero0.appmusic.data.view_model.music

import android.content.Context
import com.andersonzero0.appmusic.data.model.Music

sealed class MusicUiEvent {
    data class OnFetchMusics(val context: Context) : MusicUiEvent()
    data class OnSelectMusic(val music: Music) : MusicUiEvent()
    data class OnSearch(val query: String) : MusicUiEvent()

    data class OnPlayMusic(val music: Music, val playlist: List<Music>) : MusicUiEvent()
    data object OnPlayPause : MusicUiEvent()
    data class OnSeekTo(val position: Int) : MusicUiEvent()
    data object OnSkipToNext : MusicUiEvent()
    data object OnSkipToPrevious : MusicUiEvent()
}