package com.andersonzero0.appmusic.ui.components.navbar

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.andersonzero0.appmusic.ui.route.NavBarRoute
import com.andersonzero0.appmusic.ui.theme.colorMusic

@Composable
fun Navbar(navController: NavController, navBarRoutes: List<NavBarRoute>) {
    NavigationBar(
        containerColor = if (colorMusic != Color.Unspecified) {
            colorMusic.copy(alpha = 0.2f)
        } else {
            MaterialTheme.colorScheme.surfaceContainer
        },
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        navBarRoutes.forEach { item ->

            val isCurrentRoute = currentDestination?.route == item.route.name

            NavigationBarItem(
                icon = {
                    Icon(
                        if (isCurrentRoute) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.name
                    )
                },
                selected = isCurrentRoute,
//                label = {
//                    Text(text = item.route.label)
//                },
                onClick = {
                    navController.navigate(item.route.name) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}