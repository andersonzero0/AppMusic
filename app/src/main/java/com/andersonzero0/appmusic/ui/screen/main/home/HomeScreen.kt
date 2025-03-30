package com.andersonzero0.appmusic.ui.screen.main.home


import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.andersonzero0.appmusic.core.permissions.AudioPermission
import com.andersonzero0.appmusic.ui.components.list_musics.ListMusics
import com.andersonzero0.appmusic.ui.components.screen.Screen

@Composable
fun HomeScreen(
    onNavigateToPlayMusic: (String) -> Unit,
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
) {
    val context = LocalContext.current

    AudioPermission(
        callback = {
            onEvent(HomeUiEvent.OnFetchMusics(context))
        }
    )

    LaunchedEffect(key1 = true) {
        onEvent(HomeUiEvent.OnFetchMusics(context))
    }

    Screen(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ListMusics(
                onNavigateToPlayMusic = onNavigateToPlayMusic,
                musics = uiState.musics,
            )
        }

    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onNavigateToPlayMusic = {},
        uiState = HomeUiState(),
        onEvent = {}
    )
}