package com.andersonzero0.appmusic.ui.screen.main.play_music

import DraggableProgressIndicator
import android.widget.ProgressBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import com.andersonzero0.appmusic.R
import com.andersonzero0.appmusic.ui.components.screen.Screen
import com.andersonzero0.appmusic.ui.theme.colorMusic
import kotlin.math.roundToInt

@Composable
fun PlayMusicScreen(
    cover: Int
) {
    Screen {

        val bitmap = ImageBitmap.imageResource(cover)

        LaunchedEffect(Unit) {
            val palette = Palette.from(bitmap.asAndroidBitmap()).generate()
            colorMusic = Color(palette.getVibrantColor(Color.Black.toArgb()))
//            secondaryColor = Color(palette.getVibrantColor(Color.Gray.toArgb()))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(cover),
                contentDescription = "cover",
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
                    .clip(MaterialTheme.shapes.large),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Feeling Lonely", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "BK'",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Light,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(0.85f),
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


                DraggableProgressIndicator()

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "0:00",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Light,
                    )

                    Text(text = "3:45",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Light,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(modifier = Modifier.size(32.dp),onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Sharp.Shuffle,
                            contentDescription = "AppMusic",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    IconButton(modifier = Modifier.size(40.dp), onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Sharp.SkipPrevious,
                            contentDescription = "AppMusic",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    IconButton(modifier = Modifier.size(64.dp), onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Sharp.PauseCircle,
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
        }
    }
}

@Preview
@Composable
fun PlayMusicScreenPreview() {
    PlayMusicScreen(
        cover = R.drawable.img1
    )
}