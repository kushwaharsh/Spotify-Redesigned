package com.example.spotify.activity.home

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.BuildConfig
import com.example.spotify.activity.home.adapters.YourTopMixesAdapter
import com.example.spotify.databinding.FragmentHomeBinding
import com.example.spotify.models.YourTopMixesModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var yourTopMixesAdapter: YourTopMixesAdapter

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkAndRequestPermissions()
    }

    private fun yourTopMixes() {
        val songs = getAllAudioFiles()
        if (songs.isNotEmpty()) {
            yourTopMixesAdapter = YourTopMixesAdapter(requireContext(), songs) { song ->
                // Start PlayingMusicActivity with song data
                val intent = Intent(requireContext(), PlayingMusicActivity::class.java).apply {
                    putExtra("SONG_TITLE", song.title)
                    putExtra("SONG_PATH", song.path)
                    putExtra("SONG_ALBUM_ART", song.albumArt)
                    putExtra("SONG_DURATION", song.duration)
                }
                startActivity(intent)
            }
            binding.yourTopMixesRV.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.yourTopMixesRV.adapter = yourTopMixesAdapter
        } else {
            Toast.makeText(requireContext(), "No songs found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getAllAudioFiles(): List<YourTopMixesModel> {
        val audioList = mutableListOf<YourTopMixesModel>()
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

        val cursor = requireContext().contentResolver.query(
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

                // Fetch album art
                val albumArtUri = ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"), albumId
                )
                val albumArt = albumArtUri.toString() // Convert URI to String

                if (BuildConfig.DEBUG) {
                    Log.d("HomeFragment", "Found audio file: $title, Path: $path, Duration: $duration, Album Art: $albumArt")
                }

                audioList.add(YourTopMixesModel(title, path, albumArt, duration))
            }
        } ?: Log.d("HomeFragment", "Cursor is null")

        Log.d("HomeFragment", "Total audio files found: ${audioList.size}")
        return audioList
    }

    private fun checkAndRequestPermissions() {
        val permissionsNeeded = mutableListOf<String>()

        when {
            Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2 -> {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_MEDIA_AUDIO
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionsNeeded.add(Manifest.permission.READ_MEDIA_AUDIO)
                }
            }
        }

        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionsNeeded.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        } else {
            // Permissions are already granted
            yourTopMixes()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                yourTopMixes()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        if (::yourTopMixesAdapter.isInitialized) {
            yourTopMixesAdapter.notifyDataSetChanged() // Clear the adapter if needed
        }
    }
}
