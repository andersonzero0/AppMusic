package com.andersonzero0.appmusic.services

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
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
            MediaStore.Audio.Media.IS_MUSIC,
            MediaStore.Audio.Media.IS_PODCAST,
            MediaStore.Audio.Media.IS_DRM,
            MediaStore.Audio.Media.IS_DOWNLOAD
        )


        val sortOrder = "${MediaStore.Audio.Media.DATE_MODIFIED} DESC"

        val cursor = context.contentResolver.query(uri, projection, null, null, sortOrder)

        cursor?.use {
            val idIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleIndex = it.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artistIndex = it.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val durationIndex = it.getColumnIndex(MediaStore.Audio.Media.DURATION)
            val dataIndex = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
            val albumIdIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val isMusicIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC)
            val isPodcastIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_PODCAST)
            val isDrmIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_DRM)
            val isDownloadIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_DOWNLOAD)

            while (it.moveToNext()) {
                val isMusic = it.getInt(isMusicIndex) == 1
                val isPodcast = it.getInt(isPodcastIndex) == 1
                val isDrm = it.getInt(isDrmIndex) == 1
                val isDownload = it.getInt(isDownloadIndex) == 1

                if (!isMusic && !isPodcast && !isDrm && !isDownload) {
                    continue
                }

                val id = it.getLong(idIndex)
                val title = it.getString(titleIndex) ?: "Sem título"
                val artist = if (artistIndex != -1) it.getString(artistIndex) ?: "Desconhecido" else "Desconhecido"

                val duration = if (durationIndex == -1) 0 else it.getInt(durationIndex)

                if (!isDurationValid(duration)) {
                    continue
                }

                val durationFormat = duration.toTimeFormat()

                val path = it.getString(dataIndex) ?: ""
                val albumId = if (albumIdIndex != -1) it.getLong(albumIdIndex) else 0L

                val albumArtUri = getAlbumCoverUri(context, albumId)

                listMusics.add(Music(id, title, artist, duration, path, albumArtUri))
            }
        }

        Result.success(listMusics)
    } catch (e: Exception) {
        Log.e("AudioService", "Error fetching audios: ${e.message}")
        Result.failure(e)
    }
}

fun Int.toTimeFormat(): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this.toLong())
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this.toLong()) % 60
    return String.format(Locale.ENGLISH ,"%02d:%02d", minutes, seconds)
}


fun isDurationValid(duration: Int): Boolean {
    return duration > 30_000
}

fun getAlbumCoverUri(context: Context, albumId: Long): Uri {
    return ContentUris.withAppendedId(
        Uri.parse("content://media/external/audio/albumart"),
        albumId
    )
}