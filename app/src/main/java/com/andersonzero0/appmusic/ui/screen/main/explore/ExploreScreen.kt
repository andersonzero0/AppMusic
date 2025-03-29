package com.andersonzero0.appmusic.ui.screen.main.explore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andersonzero0.appmusic.ui.components.screen.Screen
import com.andersonzero0.appmusic.ui.components.section_musics.SectionMusics
import com.andersonzero0.appmusic.ui.screen.main.home.HomeScreen

@Composable
fun ExploreScreen() {
    Screen(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            SectionMusics(title = "Listen again")

            Spacer(modifier = Modifier.height(32.dp))

            SectionMusics(title = "Popular")

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview
@Composable
fun ExploreScreenPreview() {
    HomeScreen(
        onNavigateToPlayMusic = {}
    )
}