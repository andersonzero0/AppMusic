package com.andersonzero0.appmusic.ui.components.list_musics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andersonzero0.appmusic.data.enums.MusicModeEnum
import com.andersonzero0.appmusic.data.model.Music
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListMusics(
    modifier: Modifier = Modifier,
    musics: List<Music>,
    currentMusic: Music? = null,
    mode: MusicModeEnum,
    tempMusics: List<Music> = emptyList(),
    onNavigateToPlayMusic: (music: Music, mode: MusicModeEnum) -> Unit = {_, _ -> },
    onSearch: (String) -> Unit = {},
) {

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(searchQuery) {
        delay(500)
        onSearch(searchQuery)
    }

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

            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchQuery,
                        onQueryChange = {
                            searchQuery = it
                        },
                        onSearch = {
                        },
                        expanded = false,
                        onExpandedChange = {},
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = null
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                val randomMusic: Music = musics.random()

                                onNavigateToPlayMusic(randomMusic, MusicModeEnum.SHUFFLE)
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Shuffle,
                                    contentDescription = null
                                )
                            }
                        },
                    )
                },
                expanded = false,
                onExpandedChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp).clip(MaterialTheme.shapes.large),
                windowInsets = WindowInsets.ime,
                colors = SearchBarDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                ),
            ) {}
        }

        items(items = if (searchQuery.isNotEmpty()) tempMusics else musics) { music ->
            MusicItem(
                music = music,
                playing = currentMusic?.id == music.id,
                onClick = { onNavigateToPlayMusic(music, mode) },
            )
        }
    }
}

@Preview
@Composable
fun ListMusicsPreview() {
    ListMusics(musics = emptyList(), mode = MusicModeEnum.NORMAL) {
        // Do nothing
    }
}