package com.andersonzero0.appmusic.ui.components.list_musics

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material.icons.sharp.PlayCircleFilled
import androidx.compose.material.icons.sharp.Shuffle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andersonzero0.appmusic.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListMusics(modifier: Modifier = Modifier, onNavigateToPlayMusic: (String) -> Unit = {}) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        item {
            Text(
                text = "Minhas Músicas",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SearchBar(
                    modifier = Modifier
                        .weight(1f),
                    query = "",
                    onQueryChange = {},
                    onSearch = {},
                    active = false,
                    onActiveChange = {},
                    windowInsets = WindowInsets.ime,
                    placeholder = {
                        Text(
                            text = "Buscar",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "AppMusic",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .width(24.dp)
                                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                                .clip(MaterialTheme.shapes.small)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Shuffle,
                            contentDescription = "AppMusic",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .width(24.dp)
                                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                                .clip(MaterialTheme.shapes.small)
                        )
                    },
                    colors = SearchBarDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),

                    ),
                ) {

                }
            }
        }

        items(15) { index ->
            MusicItem(
                id = index.toString(),
                title = "Música $index",
                artist = "Artista $index",
                cover = R.drawable.img1,
                duration = "3:30",
                onClick = { onNavigateToPlayMusic("Música $index") },
            )
        }
    }
}

@Preview
@Composable
fun ListMusicsPreview() {
    ListMusics()
}