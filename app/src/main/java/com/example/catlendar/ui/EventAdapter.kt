package com.example.catlendar.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.catlendar.databinding.ItemEventBinding
import com.example.catlendar.model.CatEvent

class EventAdapter(
    private val showDate: Boolean = false,
    private val onDelete: (CatEvent) -> Unit
) : ListAdapter<CatEvent, EventAdapter.EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class EventViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: CatEvent) {
            binding.textEventTitle.text = event.title
            
            if (showDate) {
                binding.textEventTime.text = "${event.date} • ${event.time}"
            } else {
                binding.textEventTime.text = event.time
            }
            
            binding.buttonDeleteEvent.setOnClickListener {
                onDelete(event)
            }
        }
    }

    class EventDiffCallback : DiffUtil.ItemCallback<CatEvent>() {
        override fun areItemsTheSame(oldItem: CatEvent, newItem: CatEvent): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CatEvent, newItem: CatEvent): Boolean {
            return oldItem == newItem
        }
    }
}
