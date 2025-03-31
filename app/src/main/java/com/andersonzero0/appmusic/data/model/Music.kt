package com.andersonzero0.appmusic.data.model

import android.graphics.Bitmap
import android.net.Uri

data class Music(
    val id: Long,
    val title: String,
    val artist: String,
    val duration: Int,
    val path: String,
    val albumArtUri: Uri,
)