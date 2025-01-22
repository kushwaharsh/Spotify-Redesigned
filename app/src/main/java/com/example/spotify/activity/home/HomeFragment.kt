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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.models.SlideModel
import com.example.spotify.R
import com.example.spotify.activity.home.adapters.EditorsPicksAdapter
import com.example.spotify.activity.home.adapters.EventsRvAdapter
import com.example.spotify.activity.home.adapters.ImageAndTitleAdapter
import com.example.spotify.activity.home.adapters.RecentlyPlayedSongsAdapter
import com.example.spotify.activity.home.adapters.SuggestedArtistAdapter
import com.example.spotify.activity.home.adapters.YourTopMixesAdapter
import com.example.spotify.databinding.FragmentHomeBinding
import com.example.spotify.models.Artist
import com.example.spotify.models.AudioModel
import com.example.spotify.models.EventsResponseModel
import com.example.spotify.models.SuggestedArtist
import com.example.spotify.util.Common.getAllAudioFiles
import com.example.spotify.util.ProgressBarUtils
import com.example.spotify.util.Resource
import com.example.spotify.viewModels.HomeViewModel


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel : HomeViewModel by viewModels()
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var yourTopMixesAdapter: YourTopMixesAdapter
    private lateinit var recentlyPlayedSongsAdapter: RecentlyPlayedSongsAdapter
    private lateinit var imageAndTitleAdapter: ImageAndTitleAdapter
    private lateinit var editorsPicksAdapter: EditorsPicksAdapter
    private lateinit var eventsRvAdapter: EventsRvAdapter

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
        observer()
        setUpSlider()

        return binding.root
    }

    private fun observer() {
        viewModel.getAllAlbums.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    ProgressBarUtils.showProgressDialog(requireContext())
                }
                is Resource.Success -> {
                    ProgressBarUtils.hideProgressDialog()
                    // Check if data is not null or empty
                    val artist = resource.value?.artists
                    if (artist != null && artist.isNotEmpty()) {
                        // Update the UI with the data
                        setupSuggestedArtistRV(artist)
                    } else {
                        Toast.makeText(requireContext(), "No albums found", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Failure -> {
                    ProgressBarUtils.hideProgressDialog()
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                null -> {
                    ProgressBarUtils.hideProgressDialog()
                    Toast.makeText(requireContext(), "Unexpected error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun listener(){}

    private fun initView() {
        yourTopMixes(requireContext())
        recentlyPlayed(requireContext())
        editorsPick(requireContext())
        setUpImageTitleRV(requireContext())
        setUpEventsRv()

        viewModel.getAllAlbums("3IBcauSj5M2A6lTeffJzdv" , "c21c114a4emsh25b7f7df05e8c39p11cca5jsn73a682810ab8" , "spotify23.p.rapidapi.com")

    }

    private fun setupSuggestedArtistRV(artist: List<SuggestedArtist?>?) {
        binding.suggestArtistRV.adapter = SuggestedArtistAdapter(artist)
        binding.suggestArtistRV.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setUpSlider(){
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1))
        imageList.add(SlideModel(R.drawable.dummy_1))
        imageList.add(SlideModel(R.drawable.banner1))
        imageList.add(SlideModel(R.drawable.banner1))
        imageList.add(SlideModel(R.drawable.banner1))



        binding.imageSlider.setImageList(imageList)
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

                binding.bestEpisodesRV.layoutManager =
                    LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL, false)
                binding.bestEpisodesRV.adapter = yourTopMixesAdapter

            } else {
                Toast.makeText(requireContext(), "No songs found", Toast.LENGTH_SHORT).show()
            }
        }

    private fun recentlyPlayed(context: Context) {
        val topMixSongs = getAllAudioFiles<AudioModel.YourTopMixesModel>(requireContext())
        val audioFileModel = getAllAudioFiles<AudioModel.AudioFileModel>(requireContext())

        if (topMixSongs.isNotEmpty()) {
            recentlyPlayedSongsAdapter = RecentlyPlayedSongsAdapter(requireContext(), topMixSongs) { song ->
                // Start PlayingMusicActivity with song data
                val intent = Intent(requireContext(), PlayingMusicActivity::class.java).apply {
                    putExtra("SONG_TITLE", song.title)
                    putExtra("SONG_PATH", song.path)
                    putExtra("SONG_ALBUM_ART", song.albumArt)
                    putExtra("SONG_DURATION", song.duration)
                }
                startActivity(intent)
            }
            binding.recentlyPlayedSongRV.layoutManager =
                GridLayoutManager(requireContext(),2 , LinearLayoutManager.VERTICAL, false)
            binding.recentlyPlayedSongRV.adapter = recentlyPlayedSongsAdapter
        } else {
            Toast.makeText(requireContext(), "No songs found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editorsPick(context: Context) {
        val topMixSongs = getAllAudioFiles<AudioModel.YourTopMixesModel>(requireContext())
        val audioFileModel = getAllAudioFiles<AudioModel.AudioFileModel>(requireContext())

        if (topMixSongs.isNotEmpty()) {
            editorsPicksAdapter = EditorsPicksAdapter(requireContext(), topMixSongs) { song ->
                // Start PlayingMusicActivity with song data
                val intent = Intent(requireContext(), PlayingMusicActivity::class.java).apply {
                    putExtra("SONG_TITLE", song.title)
                    putExtra("SONG_PATH", song.path)
                    putExtra("SONG_ALBUM_ART", song.albumArt)
                    putExtra("SONG_DURATION", song.duration)
                }
                startActivity(intent)
            }
            binding.editorsPicksRV.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.editorsPicksRV.adapter = editorsPicksAdapter

        } else {
            Toast.makeText(requireContext(), "No songs found", Toast.LENGTH_SHORT).show()
        }
    }
    private fun setUpImageTitleRV(context: Context) {
        val topMixSongs = getAllAudioFiles<AudioModel.YourTopMixesModel>(requireContext())
        val audioFileModel = getAllAudioFiles<AudioModel.AudioFileModel>(requireContext())

        if (topMixSongs.isNotEmpty()) {
            imageAndTitleAdapter = ImageAndTitleAdapter(requireContext(), topMixSongs) { song ->
                // Start PlayingMusicActivity with song data
                val intent = Intent(requireContext(), PlayingMusicActivity::class.java).apply {
                    putExtra("SONG_TITLE", song.title)
                    putExtra("SONG_PATH", song.path)
                    putExtra("SONG_ALBUM_ART", song.albumArt)
                    putExtra("SONG_DURATION", song.duration)
                }
                startActivity(intent)
            }
            binding.freshFindsRV.layoutManager =
                LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL, false)
            binding.freshFindsRV.adapter = imageAndTitleAdapter

            binding.yearInReviewRV.layoutManager =
                LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL, false)
            binding.yearInReviewRV.adapter = imageAndTitleAdapter


        } else {
            Toast.makeText(requireContext(), "No songs found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpEventsRv(){
        val itemList = listOf(
            EventsResponseModel("Entrepreneur Mixers", "The Beer Cafe, Garden Galleria", R.drawable.dummy_img_7,"22 nov"),
            EventsResponseModel("Entrepreneur Mixers", "The Beer Cafe, Garden Galleria", R.drawable.dummy_img_4,"22 nov"),
            EventsResponseModel("Entrepreneur Mixers", "The Beer Cafe, Garden Galleria", R.drawable.dummy_img_5,"22 nov"),
            EventsResponseModel("Entrepreneur Mixers", "The Beer Cafe, Garden Galleria", R.drawable.dummy_img_6,"22 nov"),

        )


        // Initialize the adapter with static data and item click listener
        eventsRvAdapter = EventsRvAdapter(itemList) { position ->
            startActivity(Intent(requireContext() , EventDetailsActivity::class.java))
        }

        // Set up the RecyclerView with LayoutManager and Adapter
        binding.homeCardRV.layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.VERTICAL , false)
        binding.homeCardRV.adapter = eventsRvAdapter
        binding.homeCardRV.setHasFixedSize(true)
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


    @Deprecated("Deprecated in Java")
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
