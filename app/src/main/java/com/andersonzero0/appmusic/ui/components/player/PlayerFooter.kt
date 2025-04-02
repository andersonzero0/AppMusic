package com.andersonzero0.appmusic.ui.components.player

import DraggableProgressIndicator
import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Pause
import androidx.compose.material.icons.sharp.PlayArrow
import androidx.compose.material.icons.sharp.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.andersonzero0.appmusic.R
import com.andersonzero0.appmusic.data.view_model.music.MusicUiEvent
import com.andersonzero0.appmusic.data.view_model.music.MusicViewModel
import com.andersonzero0.appmusic.ui.theme.colorMusic

@Composable
fun PlayerFooter(
    navigationBar: Boolean = true,
    musicViewModel: MusicViewModel,
    onNavigateToPlayMusic: () -> Unit = {}
) {

    val music = musicViewModel.currentMusicState.collectAsStateWithLifecycle().value
        ?: return

    val isPlaying by musicViewModel.isPlayingState.collectAsStateWithLifecycle()
    val currentPosition by musicViewModel.currentPositionState.collectAsStateWithLifecycle()
    val hasNextMusic by musicViewModel.hasNextMusicState.collectAsStateWithLifecycle()

    Row(
        modifier = if (navigationBar) Modifier
            .fillMaxWidth()
            .background(
                if (colorMusic != Color.Unspecified) {
                    colorMusic.copy(alpha = 0.2f)
                } else {
                    MaterialTheme.colorScheme.onSecondary
                },
            )
            .navigationBarsPadding()
            .height(86.dp)
            .clickable {
                onNavigateToPlayMusic()
            }
            .padding(16.dp)
        else Modifier
            .fillMaxWidth()
            .background(
                if (colorMusic != Color.Unspecified) {
                    colorMusic.copy(alpha = 0.2f)
                } else {
                    MaterialTheme.colorScheme.onSecondary
                },
            )
            .height(86.dp)
            .clickable {
                onNavigateToPlayMusic()
            }
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = music.albumArtUri, contentDescription = "cover",
            placeholder = painterResource(id = R.drawable.music_placeholder),
            error = painterResource(id = R.drawable.music_placeholder),
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .clip(MaterialTheme.shapes.small),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = music.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = music.artist,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            DraggableProgressIndicator(
                activeBall = false, progress = currentPosition.toFloat() / music.duration,
                onProgressChange = {
                    musicViewModel.seekTo((it * music.duration).toInt())
                },
            )
        }

        Row {
            IconButton(modifier = Modifier.size(40.dp), onClick = {
                musicViewModel.onEvent(MusicUiEvent.OnPlayPause)
            }) {
                Icon(
                    if (isPlaying) Icons.Sharp.Pause else Icons.Sharp.PlayArrow,
                    contentDescription = "AppMusic",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxSize()
                )
            }

            IconButton(modifier = Modifier.size(40.dp), onClick = {
                musicViewModel.onEvent(MusicUiEvent.OnSkipToNext)
            }, enabled = hasNextMusic, colors = IconButtonColors(
                disabledContentColor = MaterialTheme.colorScheme.outline,
                disabledContainerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                containerColor = Color.Transparent
            )) {
                Icon(
                    Icons.Sharp.SkipNext,
                    contentDescription = "AppMusic",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview
@Composable
fun PlayerFooterPreview() {
    PlayerFooter(
        musicViewModel = MusicViewModel(application = Application()),
    )
}