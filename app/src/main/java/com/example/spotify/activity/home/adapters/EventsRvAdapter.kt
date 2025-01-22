package com.example.spotify.activity.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotify.databinding.ItemLayoutRvEventsBinding
import com.example.spotify.models.EventsResponseModel

class EventsRvAdapter (
    private val itemList: List<EventsResponseModel>,
    private val itemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<EventsRvAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemLayoutRvEventsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(itemList[position], position)
    }

    override fun getItemCount(): Int = itemList.size

    inner class MyViewHolder(private val binding: ItemLayoutRvEventsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EventsResponseModel, position: Int) {
            binding.eventNameTv.text = item.eventName
            binding.eventVenueTv.text = item.eventVenue
            binding.bgEventImage.setImageResource(item.eventImage)

            // Set click listener using the higher-order function
            binding.root.setOnClickListener {
                itemClickListener(position)
            }
        }
    }

    }