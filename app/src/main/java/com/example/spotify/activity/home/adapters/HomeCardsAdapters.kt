package com.example.spotify.activity.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotify.databinding.HomeCardEachItemBinding
import com.example.spotify.databinding.SuggestedArtistEachItemBinding
import com.example.spotify.models.HomeCardDataModel
import com.example.spotify.models.SuggestedArtistDataModel

class HomeCardsAdapters(private val items: List<HomeCardDataModel>) : RecyclerView.Adapter<HomeCardsAdapters.ItemViewHolder>() {

    class ItemViewHolder(val binding: HomeCardEachItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = HomeCardEachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.binding.titleTV.text = item.playListTitle
        holder.binding.playListImage.setImageResource(item.image)
        holder.binding.episodeTV.text = item.episodeDescription
        holder.binding.playlistDescriptionTV.text = item.playlistDecription
    }

    override fun getItemCount(): Int = items.size
}
