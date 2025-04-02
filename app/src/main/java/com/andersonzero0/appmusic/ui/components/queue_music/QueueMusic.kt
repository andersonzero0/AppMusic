package com.andersonzero0.appmusic.ui.components.queue_music

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andersonzero0.appmusic.data.model.Music
import com.andersonzero0.appmusic.ui.components.list_musics.MusicItem

@Composable
fun QueueMusic(
    queueMusic: List<Music>,
    currentMusic: Music? = null,
    onClickMusic: (Music) -> Unit = {},
) {
    val configuration = LocalConfiguration.current
    val listState = rememberLazyListState()

    val playingIndex = queueMusic.indexOfFirst { it.id == currentMusic?.id }

    LaunchedEffect(playingIndex) {
        if (playingIndex != -1) {
            val centerOffset = if (playingIndex >= 2) playingIndex - 2 else 0
            listState.animateScrollToItem(centerOffset)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = (configuration.screenHeightDp / 2).dp)
            .padding(horizontal = 16.dp)
    ) {

        Text(
            text = "Fila",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

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

@Preview
@Composable
fun QueueMusicPreview() {
    QueueMusic(queueMusic = emptyList())
}