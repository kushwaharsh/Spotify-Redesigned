package com.example.spotify.activity.home

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.text.format.DateUtils
import android.view.View
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.spotify.R
import com.example.spotify.databinding.ActivityPlayingMusicBinding

class PlayingMusicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayingMusicBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var handler : Handler
    private var isUserSeeking = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayingMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listener()
        setUpData()
    }

    private fun setUpData() {
        //Retriving Song data via intent
        val songTitle = intent.getStringExtra("SONG_TITLE")
        val songPath = intent.getStringExtra("SONG_PATH")
        val songAlbumArt = intent.getStringExtra("SONG_ALBUM_ART")
        val songDuration = intent.getLongExtra("SONG_DURATION", 0L)

        binding.songTitleTV.text = songTitle
        binding.songLengthTV.text = formatDuration(songDuration)

        // Load the album art if available
        if (songAlbumArt != null) {
            Glide.with(this)
                .load(songAlbumArt)
                .placeholder(R.drawable.your_top_mixes_1) // Placeholder image
                .into(binding.songImage)
        }

        // Set up MediaPlayer to play the song
        mediaPlayer = MediaPlayer().apply {
            setDataSource(songPath)
            prepare()
            start()
        }

        //setUpSeekBar
        binding.seekbar.max = mediaPlayer.duration
        handler = Handler()
        updateSeekBar()

        binding.seekbar.setOnSeekBarChangeListener(object  : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    mediaPlayer.seekTo(progress)
                    binding.songCurrentTimeTV.text = formatDuration(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isUserSeeking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isUserSeeking = false
                mediaPlayer.seekTo(binding.seekbar.progress)
            }
        })
    }

    private fun listener() {
        binding.pauseBtn.visibility = View.VISIBLE
        binding.playBtn.visibility = View.GONE


        binding.pauseBtn.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                binding.pauseBtn.visibility = View.GONE
                binding.playBtn.visibility = View.VISIBLE

            } else {
                mediaPlayer.start()
                binding.pauseBtn.visibility = View.VISIBLE
                binding.playBtn.visibility = View.GONE
                updateSeekBar()

            }
        }
        binding.playBtn.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                binding.pauseBtn.visibility = View.VISIBLE
                binding.playBtn.visibility = View.GONE
                updateSeekBar()

            }
        }
    }

    private fun updateSeekBar() {
        if (!isUserSeeking){
            binding.seekbar.progress = mediaPlayer.currentPosition
            binding.songCurrentTimeTV.text = formatDuration(mediaPlayer.currentPosition.toLong())
        }
        if (mediaPlayer.isPlaying){
            handler.postDelayed({ updateSeekBar()} , 1000)
        }
    }

    private fun formatDuration(durationMillis: Long): String {
        return if (durationMillis == 0L) "00:00" else DateUtils.formatElapsedTime(durationMillis / 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        handler.removeCallbacksAndMessages(null)
    }
}