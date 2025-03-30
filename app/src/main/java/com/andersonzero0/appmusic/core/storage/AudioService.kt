package com.andersonzero0.appmusic.core.storage

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.andersonzero0.appmusic.data.model.Music
import java.util.Locale
import java.util.concurrent.TimeUnit


class AudioService {
    fun getAllAudios(context: Context): Result<List<Music>> = try {
        val listMusics = mutableListOf<Music>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID,
        )

        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val idIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleIndex = it.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artistIndex = it.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val durationIndex = it.getColumnIndex(MediaStore.Audio.Media.DURATION)
            val dataIndex = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
            val albumIdIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (it.moveToNext()) {
                val id = it.getLong(idIndex)
                val title = it.getString(titleIndex)
                val artist = if (artistIndex != -1) it.getString(artistIndex) else "Desconhecido"
                val duration = if (durationIndex != -1) it.getString(durationIndex).toLong().toTimeFormat() else "00:00"
                val path = it.getString(dataIndex)
                val albumId = if (albumIdIndex != -1) it.getLong(albumIdIndex) else 0L

                val albumArtUri = getAlbumCoverUri(context, albumId)

                listMusics.add(Music(id, title, artist, duration, path, albumArtUri))
            }
        }
        Result.success(listMusics)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

fun Long.toTimeFormat(): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) % 60
    return String.format(Locale.ENGLISH ,"%02d:%02d", minutes, seconds)
}

fun getAlbumCoverUri(context: Context, albumId: Long): Uri {
    return ContentUris.withAppendedId(
        Uri.parse("content://media/external/audio/albumart"),
        albumId
    )
}