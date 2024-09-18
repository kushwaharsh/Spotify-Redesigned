package com.example.spotify.activity.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotify.databinding.UniquelyYoursEachItemBinding
import com.example.spotify.models.UniquelyYoursPlayListDataModel

class UniquelyYoursPlayListAdapter(private val items: List<UniquelyYoursPlayListDataModel>) : RecyclerView.Adapter<UniquelyYoursPlayListAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: UniquelyYoursEachItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = UniquelyYoursEachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.binding.playlistImage.setImageResource(item.imageResId)
    }

    override fun getItemCount(): Int = items.size
}

