package com.andersonzero0.appmusic.ui.components.player

import DraggableProgressIndicator
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.PlayArrow
import androidx.compose.material.icons.sharp.Repeat
import androidx.compose.material.icons.sharp.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.andersonzero0.appmusic.R
import com.andersonzero0.appmusic.data.model.Music
import com.andersonzero0.appmusic.shouldShowBottomBar
import com.andersonzero0.appmusic.ui.theme.colorMusic

@Composable
fun PlayerFooter(navigationBar: Boolean = true, music: Music) {
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
            .padding(16.dp) else Modifier
            .fillMaxWidth()
            .background(
                if (colorMusic != Color.Unspecified) {
                    colorMusic.copy(alpha = 0.2f)
                } else {
                    MaterialTheme.colorScheme.onSecondary
                },
            )
            .height(86.dp)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = music.albumArtUri, contentDescription = "cover",
            placeholder = painterResource(id = R.drawable.img5),
            error = painterResource(id = R.drawable.img5),
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
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = music.artist,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            DraggableProgressIndicator(activeBall = false)
        }

        Row {
            IconButton(modifier = Modifier.size(40.dp), onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Sharp.PlayArrow,
                    contentDescription = "AppMusic",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxSize()
                )
            }

            IconButton(modifier = Modifier.size(40.dp), onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Sharp.SkipNext,
                    contentDescription = "AppMusic",
                    tint = MaterialTheme.colorScheme.primary,
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
        music = Music(
            id = 1,
            title = "Amanhacer",
            artist = "BK'",
            duration = "03:45",
            path = "",
            albumArtUri = "".toUri(),
        )
    )
}