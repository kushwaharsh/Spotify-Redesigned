package com.example.spotify.util

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.denzcoskun.imageslider.BuildConfig
import com.example.spotify.activity.home.HomeFragment
import com.example.spotify.models.AudioModel

object Common {

    inline fun <reified T: AudioModel>getAllAudioFiles(context: Context): List<T> {
        val audioList = mutableListOf<AudioModel>()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION
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
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (it.moveToNext()) {
                val title = it.getString(titleColumn)
                val path = it.getString(dataColumn)
                val albumId = it.getLong(albumIdColumn)
                val duration = it.getLong(durationColumn)
                val artistName = it.getString(titleColumn)


                // Fetch album art
                val albumArtUri = ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"), albumId
                )
                val albumArt = albumArtUri.toString() // Convert URI to String

                if (BuildConfig.DEBUG) {
                    Log.d("HomeFragment", "Found audio file: $title, Path: $path, Duration: $duration, Album Art: $albumArt")
                }

                when(T::class.java){
                    AudioModel.YourTopMixesModel::class.java -> {
                        audioList.add(
                            AudioModel.YourTopMixesModel(title, path, albumArt, duration,
                                artistName)
                        )
                    }
                    AudioModel.AudioFileModel::class.java -> {
                        audioList.add(
                            AudioModel.AudioFileModel(title, path, albumArt, duration )
                        )
                    }
                }
            }
        } ?: Log.d("HomeFragment", "Cursor is null")

        Log.d("HomeFragment", "Total audio files found: ${audioList.size}")
        return audioList as MutableList<T>
    }

}