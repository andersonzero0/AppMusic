package com.andersonzero0.appmusic.ui.screen.main.home

import android.content.Context

sealed class HomeUiEvent {
    data class OnFetchMusics(val context: Context) : HomeUiEvent()
}