package com.example.spotify.activity.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotify.R
import com.example.spotify.databinding.YourTopMixesEachItemBinding
import com.example.spotify.models.AudioModel

class YourTopMixesAdapter(
    private val context: Context,
    private val songs: List<AudioModel.YourTopMixesModel>,
    private val onSongClick: (AudioModel.YourTopMixesModel) -> Unit
) : RecyclerView.Adapter<YourTopMixesAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding =
            YourTopMixesEachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding, onSongClick, context)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song)
    }

    override fun getItemCount(): Int = songs.size

    class SongViewHolder(
        private val binding: YourTopMixesEachItemBinding,
        private val onSongClick: (AudioModel.YourTopMixesModel) -> Unit,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(yourTopMixes: AudioModel.YourTopMixesModel) {
            binding.playlistCreaters.text = "Artist Name" // Set artist name if available
            binding.playlistTitle.text = yourTopMixes.title // Display song title

            // Load album art
            Glide.with(context)
                .load(yourTopMixes.albumArt)
                .placeholder(R.drawable.your_top_mixes_1) // Placeholder image
                .into(binding.playlistImage)

            binding.root.setOnClickListener {
                onSongClick(yourTopMixes) // Trigger callback to play the song
            }
        }
    }
}

