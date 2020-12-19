package com.tron.familytree.family.album

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tron.familytree.data.Event
import com.tron.familytree.data.Photo
import com.tron.familytree.databinding.ItemListAlbumBinding
import com.tron.familytree.databinding.ItemListEventBinding
import com.tron.familytree.family.event.EventAdapter

class AlbumAdapter(private val itemClickListener: AlbumOnItemClickListener)
    : androidx.recyclerview.widget.ListAdapter<Photo, RecyclerView.ViewHolder>(PhotoDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhotoViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is PhotoViewHolder ->{
                val photo = item as Event
                holder.bind(photo)
//                holder.binding.imageView11.setOnClickListener {
//                    itemClickListener.onItemClicked(photo)
//                }
            }
        }

    }

    class PhotoDiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }

    class PhotoViewHolder(val binding: ItemListAlbumBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.event = event
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): PhotoViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListAlbumBinding.inflate(layoutInflater, parent, false)
                return PhotoViewHolder(binding)
            }
        }
    }

    class AlbumOnItemClickListener(val clickListener: (photo: Photo) -> Unit ){
        fun onItemClicked(photo: Photo) = clickListener(photo)
    }
}