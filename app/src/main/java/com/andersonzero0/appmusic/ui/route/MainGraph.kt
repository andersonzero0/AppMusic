package com.andersonzero0.appmusic.ui.route

import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.andersonzero0.appmusic.R
import com.andersonzero0.appmusic.data.view_model.music.MusicUiEvent
import com.andersonzero0.appmusic.data.view_model.music.MusicViewModel
import com.andersonzero0.appmusic.ui.screen.main.explore.ExploreScreen
import com.andersonzero0.appmusic.ui.screen.main.home.HomeScreen
import com.andersonzero0.appmusic.ui.screen.main.play_music.PlayMusicScreen
import com.andersonzero0.appmusic.ui.screen.main.profile.ProfileScreen


fun NavGraphBuilder.mainGraph(navController: NavController, musicViewModel: MusicViewModel) {
    navigation(startDestination = Route.Home.name, route = "main") {
        composable(Route.Home.name) {
            HomeScreen(
                onNavigateToPlayMusic = { music, mode ->
                    musicViewModel.onEvent(MusicUiEvent.OnSelectMusic(music, mode))

                    navController.navigate(Route.PlayMusic.name)
                },
                musicViewModel = musicViewModel,
            )
        }
        composable(Route.Explore.name) {
            ExploreScreen()
        }
//        composable(Route.Profile.name) {
//            ProfileScreen()
//        }
        composable(Route.PlayMusic.name) {
            PlayMusicScreen(musicViewModel = musicViewModel)
        }
    }
}