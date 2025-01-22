package com.example.spotify.activity.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotify.R
import com.example.spotify.databinding.SuggestedArtistEachItemBinding

import com.example.spotify.models.SuggestedArtist

class SuggestedArtistAdapter(private val items: List<SuggestedArtist?>?) : RecyclerView.Adapter<SuggestedArtistAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: SuggestedArtistEachItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = SuggestedArtistEachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items!!.get(position)
        holder.binding.artistName.text = item?.name
        Glide.with(holder.binding.artistImage.context)
            .load(item?.images)
            .placeholder(R.drawable.artist_img_1) // Fallback image
            .into(holder.binding.artistImage)
    }

    override fun getItemCount(): Int = items!!.size
}

