package com.andersonzero0.appmusic.ui.screen.main.play_music

import android.app.Application
import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.material.icons.sharp.IosShare
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import coil3.compose.AsyncImage
import com.andersonzero0.appmusic.R
import com.andersonzero0.appmusic.data.view_model.music.MusicUiEvent
import com.andersonzero0.appmusic.data.view_model.music.MusicViewModel
import com.andersonzero0.appmusic.ui.components.queue_music.QueueMusic
import com.andersonzero0.appmusic.ui.components.screen.Screen
import com.andersonzero0.appmusic.ui.theme.colorMusic

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayMusicScreen(
    musicViewModel: MusicViewModel
) {
    val context = LocalContext.current
    val currentMusic by musicViewModel.currentMusicState.collectAsStateWithLifecycle()

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true);

    Screen {
        LaunchedEffect(key1 = currentMusic) {

            currentMusic?.albumArtUri.let { uri ->/**/
                runCatching {
                    uri?.let {
                        context.contentResolver.openInputStream(it)?.use { inputStream ->
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            val palette = Palette.from(bitmap).generate()

                            val colorVibrant =
                                Color(palette.getDarkVibrantColor(Color.White.toArgb()))

                            colorVibrant
                        }
                    }
                }.onSuccess { result ->
                    colorMusic = result ?: Color.Unspecified
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(top = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = currentMusic?.albumArtUri,
                contentDescription = "cover",
                placeholder = painterResource(id = R.drawable.music_placeholder),
                error = painterResource(id = R.drawable.music_placeholder),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
                    .clip(MaterialTheme.shapes.large),
            )

            Spacer(modifier = Modifier.height(16.dp))


            currentMusic?.let {
                Text(
                    text = it.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            currentMusic?.let {
                Text(
                    text = it.artist,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Sharp.Favorite,
                            contentDescription = "AppMusic",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Sharp.IosShare,
                            contentDescription = "AppMusic",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                currentMusic?.let {
                    ControllerPlayer(
                        musicViewModel = musicViewModel, duration = it.duration,
                        onProgressChange = {
                            musicViewModel.onEvent(MusicUiEvent.OnSeekTo((it * currentMusic!!.duration).toInt()))
                        },
                        onQueueMusic = {
                            showBottomSheet = true
                        }
                    )
                }
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                    contentWindowInsets = { WindowInsets.navigationBars },
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    properties = ModalBottomSheetProperties(
                        shouldDismissOnBackPress = true
                    ),
                ) {
                    QueueMusic(
                        queueMusic = musicViewModel.getQueueMusic(),
                        currentMusic,
                        onClickMusic = {
                            musicViewModel.onEvent(MusicUiEvent.OnSelectMusic(it))
                        })
                }
            }
        }
    }
}

@Preview
@Composable
fun PlayMusicScreenPreview() {
    PlayMusicScreen(
        musicViewModel = MusicViewModel(Application()) // Use a mock or test instance of MusicViewModel
    )
}