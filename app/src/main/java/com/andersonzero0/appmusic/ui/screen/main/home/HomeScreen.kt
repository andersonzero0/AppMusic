package com.andersonzero0.appmusic.ui.screen.main.home


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andersonzero0.appmusic.core.permissions.AudioPermission
import com.andersonzero0.appmusic.core.permissions.NotificationPermission
import com.andersonzero0.appmusic.data.model.Music
import com.andersonzero0.appmusic.data.view_model.music.MusicUiEvent
import com.andersonzero0.appmusic.data.view_model.music.MusicViewModel
import com.andersonzero0.appmusic.ui.components.list_musics.ListMusics
import com.andersonzero0.appmusic.ui.components.screen.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToPlayMusic: (Music) -> Unit,
    musicViewModel: MusicViewModel = viewModel(),
) {
    val context = LocalContext.current
    val uiState by musicViewModel.uiState.collectAsStateWithLifecycle()
    val currentMusic by musicViewModel.currentMusicState.collectAsStateWithLifecycle()

    AudioPermission(
        callback = {
            musicViewModel.onEvent(MusicUiEvent.OnFetchMusics(context))
        }
    )

    NotificationPermission {}

    Screen(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            ListMusics(
                onNavigateToPlayMusic = onNavigateToPlayMusic,
                musics = uiState.musics,
                currentMusic = currentMusic,
                tempMusics = uiState.tempMusics,
                onSearch = {
                    musicViewModel.onEvent(MusicUiEvent.OnSearch(it))
                },
            )
        }

    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onNavigateToPlayMusic = {}
    )
}