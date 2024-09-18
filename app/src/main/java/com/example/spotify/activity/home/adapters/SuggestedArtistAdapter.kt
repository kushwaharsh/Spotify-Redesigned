package com.example.spotify.activity.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotify.databinding.SuggestedArtistEachItemBinding
import com.example.spotify.databinding.UniquelyYoursEachItemBinding
import com.example.spotify.models.SuggestedArtistDataModel
import com.example.spotify.models.UniquelyYoursPlayListDataModel

class SuggestedArtistAdapter(private val items: List<SuggestedArtistDataModel>) : RecyclerView.Adapter<SuggestedArtistAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: SuggestedArtistEachItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = SuggestedArtistEachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.binding.artistName.text = item.name
        holder.binding.artistImage.setImageResource(item.imageResId)
    }

    override fun getItemCount(): Int = items.size
}

