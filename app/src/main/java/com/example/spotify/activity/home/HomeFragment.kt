package com.example.spotify.activity.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spotify.activity.home.adapters.YourTopMixesAdapter
import com.example.spotify.databinding.FragmentHomeBinding
import com.example.spotify.models.AudioModel
import com.example.spotify.util.Common.getAllAudioFiles


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

        checkAndRequestPermissions()
        initView()

        return binding.root
    }

    private fun initView() {
        yourTopMixes(requireContext())
    }


    private fun yourTopMixes(context: Context) {
            val topMixSongs = getAllAudioFiles<AudioModel.YourTopMixesModel>(requireContext())
            val audioFileModel = getAllAudioFiles<AudioModel.AudioFileModel>(requireContext())

            if (topMixSongs.isNotEmpty()) {
                yourTopMixesAdapter = YourTopMixesAdapter(requireContext(), topMixSongs) { song ->
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
            initView()
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
                initView()
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
