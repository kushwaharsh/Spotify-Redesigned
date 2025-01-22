package com.example.spotify.activity.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotify.R
import com.example.spotify.databinding.RecentlyPlayedEachItemBinding
import com.example.spotify.databinding.YourTopMixesEachItemBinding
import com.example.spotify.models.AudioModel
import com.example.spotify.models.GetAlbumsResponseModel

class RecentlyPlayedSongsAdapter(
    private val context: Context,
    private val songs: List<AudioModel.YourTopMixesModel>,
    private val onSongClick: (AudioModel.YourTopMixesModel) -> Unit
) : RecyclerView.Adapter<RecentlyPlayedSongsAdapter.GridViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        // Using View Binding instead of inflating the layout manually
        val binding = RecentlyPlayedEachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridViewHolder(binding , onSongClick , context)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song)  // Bind data
    }

    override fun getItemCount(): Int = songs.size

    class GridViewHolder(
        private val binding: RecentlyPlayedEachItemBinding,
        private val onSongClick: (AudioModel.YourTopMixesModel) -> Unit,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(yourTopMixes: AudioModel.YourTopMixesModel) {
            binding.recentlyPlayedSongName.text = yourTopMixes.title // Display song title

            // Load album art
            Glide.with(context)
                .load(yourTopMixes.albumArt)
                .placeholder(R.drawable.your_top_mixes_1) // Placeholder image
                .into(binding.recentlyplayedSongImage)

            binding.root.setOnClickListener {
                onSongClick(yourTopMixes) // Trigger callback to play the song
            }
        }
    }
}
