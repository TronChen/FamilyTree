package com.tron.familytree.family.album

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tron.familytree.R
import com.tron.familytree.data.Event
import com.tron.familytree.data.Photo
import com.tron.familytree.databinding.ItemListAlbumBinding
import com.tron.familytree.databinding.ItemListEventBinding
import com.tron.familytree.family.create_event.EventType
import com.tron.familytree.family.event.EventAdapter

class AlbumAdapter(private val itemClickListener: AlbumOnItemClickListener)
    : androidx.recyclerview.widget.ListAdapter<Event, RecyclerView.ViewHolder>(UserDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AlbumViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is AlbumViewHolder ->{
                val event = item as Event
                holder.bind(event)
                holder.binding.conAlbum.setOnClickListener {
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

    class AlbumViewHolder(val binding: ItemListAlbumBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.event = event
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): AlbumViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListAlbumBinding.inflate(layoutInflater, parent, false)
                return AlbumViewHolder(binding)
            }
        }
    }

    class AlbumOnItemClickListener(val clickListener: (event: Event) -> Unit ){
        fun onItemClicked(event: Event) = clickListener(event)
    }
}