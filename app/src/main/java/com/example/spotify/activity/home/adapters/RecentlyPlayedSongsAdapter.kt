package com.example.spotify.activity.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotify.databinding.RecentlyPlayedEachItemBinding
import com.example.spotify.models.RecentlyPlayedSongsModel

class RecentlyPlayedSongsAdapter(private val itemList: List<RecentlyPlayedSongsModel>) : RecyclerView.Adapter<RecentlyPlayedSongsAdapter.GridViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        // Using View Binding instead of inflating the layout manually
        val binding = RecentlyPlayedEachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)  // Bind data
    }

    override fun getItemCount(): Int = itemList.size

    class GridViewHolder(private val binding: RecentlyPlayedEachItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecentlyPlayedSongsModel) {
            binding.recentlyPlayedSongName.text = item.title
            binding.recentlyplayedSongImage.setImageResource(item.imageResId)
        }
    }
}
