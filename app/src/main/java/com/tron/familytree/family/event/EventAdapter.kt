package com.tron.familytree.family.event

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tron.familytree.R
import com.tron.familytree.branch.BranchViewAdapter
import com.tron.familytree.branch.TreeItem
import com.tron.familytree.data.Event
import com.tron.familytree.data.User
import com.tron.familytree.databinding.ItemListEmptyBinding
import com.tron.familytree.databinding.ItemListEventBinding
import com.tron.familytree.family.create_event.EventType

class EventAdapter(private val itemClickListener: EventOnItemClickListener)
    : ListAdapter<Event, RecyclerView.ViewHolder>(UserDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return EventViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is EventViewHolder ->{
                val event = item as Event
                holder.bind(event)
                holder.binding.conEvent.setOnClickListener {
                    itemClickListener.onItemClicked(event)
                }
            }
        }

    }

    class UserDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }

    class EventViewHolder(val binding: ItemListEventBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.event = event
            when(event.eventType){
                EventType.MEAL -> binding.imagEventType.setImageResource(R.drawable.meal)
                EventType.CASUAL -> binding.imagEventType.setImageResource(R.drawable.casual)
                EventType.SPORT -> binding.imagEventType.setImageResource(R.drawable.sport)
                EventType.BIG_THING -> binding.imagEventType.setImageResource(R.drawable.big_thing)
            }
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): EventViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListEventBinding.inflate(layoutInflater, parent, false)
                return EventViewHolder(binding)
            }
        }
    }

    class EventOnItemClickListener(val clickListener: (event: Event) -> Unit ){
        fun onItemClicked(event: Event) = clickListener(event)
    }
}