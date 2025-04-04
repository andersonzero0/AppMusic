package com.andersonzero0.appmusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBackIosNew
import androidx.compose.material.icons.sharp.Download
import androidx.compose.material.icons.sharp.PlayCircleFilled
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.andersonzero0.appmusic.ui.theme.AppMusicTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.andersonzero0.appmusic.data.view_model.music.MusicViewModel
import com.andersonzero0.appmusic.ui.components.player.PlayerFooter
import com.andersonzero0.appmusic.ui.route.Route
import com.andersonzero0.appmusic.ui.route.mainGraph
import com.andersonzero0.appmusic.ui.route.authGraph
import com.andersonzero0.appmusic.ui.route.navBarRoutes
import com.andersonzero0.appmusic.ui.route.routesWithGoBack
import com.andersonzero0.appmusic.ui.theme.colorMusic

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppMusicTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                val musicViewModel: MusicViewModel by viewModels()

                val currentRoute = navBackStackEntry?.destination?.route

                val context = LocalContext.current

                LaunchedEffect(Unit) {
                    musicViewModel.bindService(context)
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        if (colorMusic != Color.Unspecified) {
                                            colorMusic.copy(alpha = 0.33f)
                                        } else {
                                            Color.Transparent
                                        },
                                        Color.Transparent
                                    ),
                                    start = Offset(100f, 100f),
                                    end = Offset(1000f, 1000f)
                                )
                            )
                    ) {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    colors = TopAppBarDefaults.topAppBarColors(
                                        titleContentColor = MaterialTheme.colorScheme.primary,
                                        containerColor = Color.Transparent
                                    ),
                                    navigationIcon = {
                                        if (shouldShowGoBack(currentRoute)) {
                                            IconButton(
                                                onClick = { navController.popBackStack() }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Sharp.ArrowBackIosNew,
                                                    contentDescription = "AppMusic",
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    },
                                    actions = {
                                        IconButton(onClick = { /*TODO*/ }) {
                                            Icon(
                                                imageVector = Icons.Sharp.Download,
                                                contentDescription = "AppMusic",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    },
                                    title = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        ) {
                                            Icon(
                                                Icons.Sharp.PlayCircleFilled,
                                                contentDescription = "AppMusic",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                            Text(
                                                "App Music",
                                                style = MaterialTheme.typography.headlineSmall,
                                                fontWeight = FontWeight.Black
                                            )
                                        }

                                    },
                                )
                            },
                            bottomBar = {
                                Column {
                                    if (currentRoute != Route.PlayMusic.name) {
                                        PlayerFooter(
                                            navigationBar = !shouldShowBottomBar(currentRoute),
                                            musicViewModel = musicViewModel,
                                            onNavigateToPlayMusic = {
                                                navController.navigate(Route.PlayMusic.name) {
                                                    popUpTo(Route.Home.name) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        )
                                    }
//                                    if (shouldShowBottomBar(currentRoute)) {
//                                        Navbar(
//                                            navController = navController,
//                                            navBarRoutes = navBarRoutes
//                                        )
//                                    }
                                }
                            },
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ) { innerPadding ->
                            NavHost(
                                navController = navController,
                                startDestination = "main",
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                authGraph()
                                mainGraph(navController, musicViewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun shouldShowBottomBar(route: String?): Boolean {
    return route in navBarRoutes.map { it.route.name }
}

fun shouldShowGoBack(route: String?): Boolean {
    return route in routesWithGoBack.map { it.name }
}