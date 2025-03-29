package com.andersonzero0.appmusic.ui.components.section_musics

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowOverflow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andersonzero0.appmusic.R

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun SectionMusics(
    title: String = "Listen again"
) {
    data class Music(
        var musicName: String = "",
        var musicImage: Int = 0
    )

    val musics = listOf(
        Music("Princess Diana (with Nicki Minaj)", R.drawable.img),
        Music("PORTALS (Deluxe)", R.drawable.img2),
        Music("Freak Mode", R.drawable.img3),
        Music("Church Outfit", R.drawable.img4),
        Music("Dumb Dummy", R.drawable.img5),
        Music("Pink Venom", R.drawable.img6),
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        BoxWithConstraints {
            val maxWidth = this.maxWidth
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                maxItemsInEachRow = 3
            ) {
                musics.forEach { music ->
                    BoxWithConstraints(
                        modifier = Modifier
                            .width((maxWidth - 33.dp) / 3) // 32.dp = 2 spaces of 16.dp
                    ) {
                        SectionMusicsItem(
                            modifier = Modifier.fillMaxWidth(),
                            musicName = music.musicName,
                            musicImage = music.musicImage
                        )
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun SectionMusicsPreview() {
    SectionMusics()
}