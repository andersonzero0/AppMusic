package com.andersonzero0.appmusic.ui.components.player

import DraggableProgressIndicator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.PauseCircle
import androidx.compose.material.icons.sharp.PlayCircleFilled
import androidx.compose.material.icons.sharp.Repeat
import androidx.compose.material.icons.sharp.Shuffle
import androidx.compose.material.icons.sharp.SkipNext
import androidx.compose.material.icons.sharp.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andersonzero0.appmusic.data.view_model.music.MusicUiEvent
import com.andersonzero0.appmusic.data.view_model.music.MusicViewModel
import com.andersonzero0.appmusic.services.toTimeFormat

@Composable
fun ControllerPlayer(duration: Int, onProgressChange: (Float) -> Unit, musicViewModel: MusicViewModel) {

    val isPlaying by musicViewModel.isPlayingState.collectAsStateWithLifecycle()
    val currentPosition by musicViewModel.currentPositionState.collectAsStateWithLifecycle()
    val hasNextMusic by musicViewModel.hasNextMusicState.collectAsStateWithLifecycle()
    val hasPreviousMusic by musicViewModel.hasPreviousMusicState.collectAsStateWithLifecycle()

    DraggableProgressIndicator(
        progress = currentPosition.toFloat() / duration,
        onProgressChange = onProgressChange,
        activeBall = true,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currentPosition.toTimeFormat(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Light,
        )

        Text(
            text = duration.toTimeFormat(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Light,
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(modifier = Modifier.size(32.dp), onClick = { /*TODO*/ }) {
            Icon(
                Icons.Sharp.Shuffle,
                contentDescription = "AppMusic",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxSize()
            )
        }
        IconButton(modifier = Modifier.size(40.dp), onClick = {
            musicViewModel.onEvent(MusicUiEvent.OnSkipToPrevious)
        }, enabled = hasPreviousMusic, colors = IconButtonColors(
            disabledContentColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            disabledContainerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = Color.Transparent
        )) {
            Icon(
                Icons.Sharp.SkipPrevious,
                contentDescription = "AppMusic",
                modifier = Modifier.fillMaxSize()
            )
        }
        IconButton(modifier = Modifier.size(64.dp), onClick = {
            musicViewModel.onEvent(MusicUiEvent.OnPlayPause)
        }) {
            Icon(
                if (isPlaying) Icons.Sharp.PauseCircle else Icons.Sharp.PlayCircleFilled,
                contentDescription = "AppMusic",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxSize()
            )
        }
        IconButton(modifier = Modifier.size(40.dp), onClick = {
            musicViewModel.onEvent(MusicUiEvent.OnSkipToNext)
        }, enabled = hasNextMusic, colors = IconButtonColors(
            disabledContentColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            disabledContainerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = Color.Transparent
        )) {
            Icon(
                Icons.Sharp.SkipNext,
                contentDescription = "AppMusic",
                modifier = Modifier.fillMaxSize(),
            )
        }

        IconButton(modifier = Modifier.size(32.dp), onClick = { /*TODO*/ }) {
            Icon(
                Icons.Sharp.Repeat,
                contentDescription = "AppMusic",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxSize()
            )
        }

    }

}