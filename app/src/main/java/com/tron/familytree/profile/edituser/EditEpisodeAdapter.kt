package com.tron.familytree.profile.edituser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tron.familytree.data.Episode
import com.tron.familytree.databinding.ItemListEpisodeEditBinding

class EditEpisodeAdapter (private val itemClickListener: EditEpisodeOnItemClickListener)
    : ListAdapter<Episode, RecyclerView.ViewHolder>(EditEpisodeDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return EditEpisodeViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is EditEpisodeViewHolder ->{
                val episode = item as Episode
                holder.bind(episode)
                holder.binding.conEdit.setOnClickListener {
                    itemClickListener.onItemClicked(episode)
                }
            }
        }

    }

    class EditEpisodeDiffCallback : DiffUtil.ItemCallback<Episode>() {
        override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem == newItem
        }
    }

    class EditEpisodeViewHolder(val binding: ItemListEpisodeEditBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(episode: Episode) {
            binding.episode = episode
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): EditEpisodeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListEpisodeEditBinding.inflate(layoutInflater, parent, false)
                return EditEpisodeViewHolder(
                    binding
                )
            }
        }
    }

    class EditEpisodeOnItemClickListener(val clickListener: (episode: Episode) -> Unit ){
        fun onItemClicked(episode: Episode) = clickListener(episode)
    }
}