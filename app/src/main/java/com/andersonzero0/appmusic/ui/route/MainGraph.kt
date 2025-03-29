package com.andersonzero0.appmusic.ui.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.andersonzero0.appmusic.R
import com.andersonzero0.appmusic.ui.screen.main.explore.ExploreScreen
import com.andersonzero0.appmusic.ui.screen.main.home.HomeScreen
import com.andersonzero0.appmusic.ui.screen.main.play_music.PlayMusicScreen
import com.andersonzero0.appmusic.ui.screen.main.profile.ProfileScreen

fun NavGraphBuilder.mainGraph(navController: NavController) {
    navigation(startDestination = Route.Home.name, route = "main") {
        composable(Route.Home.name) {
            HomeScreen(
                onNavigateToPlayMusic = { musicId ->
                    navController.navigate(Route.PlayMusic.name)
                }
            )
        }
        composable(Route.Explore.name) {
            ExploreScreen()
        }
//        composable(Route.Profile.name) {
//            ProfileScreen()
//        }
        composable(Route.PlayMusic.name) {
            PlayMusicScreen(cover = R.drawable.img5)
        }
    }
}