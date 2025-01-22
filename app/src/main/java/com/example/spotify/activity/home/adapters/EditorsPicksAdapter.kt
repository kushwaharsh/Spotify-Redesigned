package com.example.spotify.activity.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotify.R
import com.example.spotify.databinding.EditorsPicksEachItemBinding
import com.example.spotify.databinding.YourTopMixesEachItemBinding
import com.example.spotify.models.AudioModel
import com.example.spotify.models.GetAlbumsResponseModel

class EditorsPicksAdapter(
    private val context: Context,
    private val items: List<AudioModel.YourTopMixesModel>,
    private val onSongClick: (AudioModel.YourTopMixesModel) -> Unit
) : RecyclerView.Adapter<EditorsPicksAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            EditorsPicksEachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, onSongClick, context)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    class ItemViewHolder(
        val binding: EditorsPicksEachItemBinding ,
        private val onSongClick: (AudioModel.YourTopMixesModel) -> Unit,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(editorsPicks: AudioModel.YourTopMixesModel) {
            binding.playlistName.text = editorsPicks.title // Display song title

            // Load album art
            Glide.with(context)
                .load(editorsPicks.albumArt)
                .placeholder(R.drawable.your_top_mixes_1) // Placeholder image
                .into(binding.playlistImage)

            binding.root.setOnClickListener {
                onSongClick(editorsPicks) // Trigger callback to play the song
            }
        }

    }

}

