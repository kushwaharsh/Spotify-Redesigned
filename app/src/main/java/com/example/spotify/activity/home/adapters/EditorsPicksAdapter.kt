package com.example.spotify.activity.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotify.databinding.EditorsPicksEachItemBinding
import com.example.spotify.models.ImageAndTitleDataModel

class EditorsPicksAdapter(private val items: List<ImageAndTitleDataModel>) : RecyclerView.Adapter<EditorsPicksAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: EditorsPicksEachItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = EditorsPicksEachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.binding.playlistName.text = item.playlistName
        holder.binding.playlistImage.setImageResource(item.imageResId)
    }

    override fun getItemCount(): Int = items.size
}

