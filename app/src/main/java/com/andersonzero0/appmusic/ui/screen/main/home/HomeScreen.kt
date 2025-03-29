package com.andersonzero0.appmusic.ui.screen.main.home


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andersonzero0.appmusic.ui.components.list_musics.ListMusics
import com.andersonzero0.appmusic.ui.components.screen.Screen

@Composable
fun HomeScreen(
    onNavigateToPlayMusic: (String) -> Unit,
) {
    Screen(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ListMusics(
                onNavigateToPlayMusic = onNavigateToPlayMusic,
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