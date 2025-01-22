package com.example.spotify.activity.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotify.R
import com.example.spotify.databinding.RoundedImageTitleEachItemBinding
import com.example.spotify.databinding.YourTopMixesEachItemBinding
import com.example.spotify.models.AudioModel
import com.example.spotify.models.GetAlbumsResponseModel

class ImageAndTitleAdapter(
    private val context: Context,
    private val songs: List<AudioModel.YourTopMixesModel>,
    private val onSongClick: (AudioModel.YourTopMixesModel) -> Unit
) : RecyclerView.Adapter<ImageAndTitleAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = RoundedImageTitleEachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding , onSongClick , context)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song)
    }

    override fun getItemCount(): Int = songs.size

    class SongViewHolder(
        private val binding: RoundedImageTitleEachItemBinding,
        private val onSongClick: (AudioModel.YourTopMixesModel) -> Unit,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(yourTopMixes: AudioModel.YourTopMixesModel) {
           // binding.Pla.text = "Artist Name" // Set artist name if available
            binding.playlistName.text = yourTopMixes.title

            // Load album art
            Glide.with(context)
                .load(yourTopMixes.albumArt)
                .placeholder(R.drawable.your_top_mixes_1)
                .into(binding.playlistImage)

            binding.root.setOnClickListener {
                onSongClick(yourTopMixes)
            }
        }
    }
}

