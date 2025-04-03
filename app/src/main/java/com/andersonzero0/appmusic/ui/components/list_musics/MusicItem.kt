package com.andersonzero0.appmusic.ui.components.list_musics

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.sharp.PlayCircleFilled
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.andersonzero0.appmusic.R
import com.andersonzero0.appmusic.data.model.Music
import com.andersonzero0.appmusic.services.toTimeFormat
import com.andersonzero0.appmusic.ui.theme.colorMusic

@Composable
fun MusicItem(
    music: Music,
    playing: Boolean = false,
    onClick: (Music) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable { onClick(music) }
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = music.albumArtUri, contentDescription = "cover",
                placeholder = painterResource(id = R.drawable.music_placeholder),
                error = painterResource(id = R.drawable.music_placeholder),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(52.dp)
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
                    .clip(MaterialTheme.shapes.small)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = music.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            Text(
                text = music.artist,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = music.duration.toTimeFormat(), style = MaterialTheme.typography.bodySmall)
            Icon(
                if (playing) Icons.Default.Equalizer else Icons.Outlined.PlayCircle,
                contentDescription = "AppMusic",
                tint = if (playing) Color(0xFF1DB954) else MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .width(28.dp)
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
                    .clip(MaterialTheme.shapes.small)
            )
        }
    }
}

@Preview
@Composable
fun MusicItemPreview() {
    MusicItem(
        music = Music(
            id = 1,
            title = "Title",
            artist = "Artist",
            albumArtUri = Uri.parse("https://example.com/cover.jpg"),
            duration = 180000,
            path = "path/to/music.mp3"
        ),
    )
}