package com.andersonzero0.appmusic.ui.route

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import com.andersonzero0.appmusic.R

sealed class Route(val name: String, val label: String = name) {
    data object Home : Route("home", "Home")
    data object Explore : Route("explore", "Explorar")
//    data object Profile : Route("profile", "Perfil")
    data object PlayMusic : Route("play_music", "Tocando")

    data object Login : Route("login", "Entrar")
}

data class NavBarRoute(
    val name: String,
    val route: Route,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

val navBarRoutes = listOf(
    NavBarRoute("Home", Route.Home, Icons.Filled.LibraryMusic, Icons.Outlined.LibraryMusic),
    NavBarRoute("Explore", Route.Explore, Icons.Filled.Explore, Icons.Outlined.Explore),
//    NavBarRoute("Profile", Route.Profile, Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle),
)

val routesWithGoBack = listOf(
    Route.PlayMusic,
)