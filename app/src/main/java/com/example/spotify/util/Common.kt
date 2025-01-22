package com.example.spotify.util

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.example.spotify.models.AudioModel

object Common {

    inline fun <reified T : AudioModel> getAllAudioFiles(context: Context): List<T> {
        val audioList = mutableListOf<AudioModel>()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.GENRE,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND ${MediaStore.Audio.Media.DURATION} >= 10000" // Filtering for songs with duration >= 10 seconds
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        } else {
            MediaStore.Audio.Media.INTERNAL_CONTENT_URI
        }

        val cursor = context.contentResolver.query(
            uri,
            projection,
            selection,
            null,
            sortOrder
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val genreColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.GENRE)
            val trackColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val title = it.getString(titleColumn)
                val artist = it.getString(artistColumn) ?: "Unknown Artist"
                val album = it.getString(albumColumn) ?: "Unknown Album"
                val genre = it.getString(genreColumn) ?: "Unknown Genre"
                val trackNumber = it.getString(trackColumn)?.toIntOrNull() ?: 0
                val path = it.getString(dataColumn)
                val albumId = it.getLong(albumIdColumn)
                val duration = it.getLong(durationColumn)
                val size = it.getLong(sizeColumn)

                // Fetch album art
                val albumArtUri = ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"), albumId
                )
                val albumArt = albumArtUri.toString() // Convert URI to String

                Log.d(
                    "AudioFile",
                    "ID: $id, Title: $title, Artist: $artist, Album: $album, Genre: $genre, " +
                            "Track: $trackNumber, Duration: $duration, Path: $path, Size: $size, Album Art: $albumArt"
                )

                // Create instances of AudioModel based on the type
                when (T::class.java) {
                    AudioModel.YourTopMixesModel::class.java -> {
                        audioList.add(
                            AudioModel.YourTopMixesModel(
                                title = title,
                                path = path,
                                albumArt = albumArt,
                                duration = duration,
                                artistName = artist,
                                albumName = album,
                                genre = genre,
                                trackNumber = trackNumber,
                                size = size
                            )
                        )
                    }
                    AudioModel.AudioFileModel::class.java -> {
                        audioList.add(
                            AudioModel.AudioFileModel(
                                title = title,
                                path = path,
                                albumArt = albumArt,
                                duration = duration,
                                artist = artist,
                                album = album,
                                genre = genre,
                                trackNumber = trackNumber,
                                size = size
                            )
                        )
                    }
                }
            }
        } ?: Log.d("AudioFile", "Cursor is null")

        Log.d("AudioFile", "Total audio files found: ${audioList.size}")
        return audioList as MutableList<T>
    }
}
