package com.andersonzero0.appmusic.ui.components.queue_music

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.FilterTiltShift
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.sharp.IosShare
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.andersonzero0.appmusic.data.model.Music
import com.andersonzero0.appmusic.ui.components.list_musics.MusicItem
import com.andersonzero0.appmusic.ui.theme.colorMusic
import kotlinx.coroutines.launch

@Composable
fun QueueMusic(
    queueMusic: List<Music>,
    currentMusic: Music? = null,
    onClickMusic: (Music) -> Unit = {},
) {
    val configuration = LocalConfiguration.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val playingIndex = queueMusic.indexOfFirst { it.id == currentMusic?.id }

    LaunchedEffect(playingIndex) {
        scrollToCurrentMusic(playingIndex, listState)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        if (colorMusic != Color.Unspecified) {
                            colorMusic.copy(alpha = 0.2f)
                        } else {
                            Color.Transparent
                        }
                    ),
                    tileMode = TileMode.Mirror,
//                    start = Offset(0f, 1200f),
//                    end = Offset(100f, 100f)
                )
            )
            .heightIn(max = (configuration.screenHeightDp / 2).dp)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Fila",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            IconButton(onClick = {
                coroutineScope.launch {
                    scrollToCurrentMusic(playingIndex, listState)
                }
            }, modifier = Modifier.scale(0.8f)) {
                Icon(
                    Icons.Filled.FilterTiltShift,
                    contentDescription = "AppMusic",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxSize(0.9f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            state = listState,
        ) {
            items(queueMusic, key = { it.id }) { music ->
                MusicItem(
                    music = music,
                    playing = currentMusic?.id == music.id,
                    onClick = {
                        onClickMusic(music)
                    },
                )
            }
        }
    }
}

suspend fun scrollToCurrentMusic(playingIndex: Int, listState: LazyListState) {
    if (playingIndex != -1) {
        val centerOffset = if (playingIndex >= 2) playingIndex - 2 else 0
        listState.animateScrollToItem(centerOffset)
    }
}

@Preview
@Composable
fun QueueMusicPreview() {
    QueueMusic(queueMusic = List(10) {
        Music(
            id = it.toLong(),
            title = "Title $it",
            artist = "Artist $it",
            duration = 1000,
            path = "path/to/music/$it.mp3",
            albumArtUri = "a".toUri(),
        )
    })

}