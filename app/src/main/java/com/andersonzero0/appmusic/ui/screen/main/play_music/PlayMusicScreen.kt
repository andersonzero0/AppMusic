package com.andersonzero0.appmusic.ui.screen.main.play_music

import DraggableProgressIndicator
import android.app.Application
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.material.icons.sharp.IosShare
import androidx.compose.material.icons.sharp.PauseCircle
import androidx.compose.material.icons.sharp.PlayCircleFilled
import androidx.compose.material.icons.sharp.Repeat
import androidx.compose.material.icons.sharp.Shuffle
import androidx.compose.material.icons.sharp.SkipNext
import androidx.compose.material.icons.sharp.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import coil3.compose.AsyncImage
import com.andersonzero0.appmusic.R
import com.andersonzero0.appmusic.core.permissions.NotificationPermission
import com.andersonzero0.appmusic.data.model.Music
import com.andersonzero0.appmusic.data.view_model.music.MusicUiEvent
import com.andersonzero0.appmusic.data.view_model.music.MusicViewModel
import com.andersonzero0.appmusic.services.toTimeFormat
import com.andersonzero0.appmusic.ui.components.player.ControllerPlayer
import com.andersonzero0.appmusic.ui.components.screen.Screen
import com.andersonzero0.appmusic.ui.theme.colorMusic

@Composable
fun PlayMusicScreen(
    musicViewModel: MusicViewModel
) {
    Screen {
        val context = LocalContext.current
        val currentMusic by musicViewModel.currentMusicState.collectAsStateWithLifecycle()

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
                    )
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