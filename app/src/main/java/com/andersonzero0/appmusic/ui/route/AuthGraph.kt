package com.andersonzero0.appmusic.ui.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.andersonzero0.appmusic.ui.screen.auth.LoginScreen

fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation(startDestination = Route.Login.name, route = "auth") {
        composable(Route.Login.name) {
            LoginScreen()
        }
    }
}