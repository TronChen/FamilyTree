package com.tron.familytree.profile.episode

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tron.familytree.data.Episode
import com.tron.familytree.data.Event
import com.tron.familytree.databinding.ItemListEpisodeBinding
import com.tron.familytree.databinding.ItemListEventBinding

class EpisodeAdapter (private val itemClickListener: EpisodeOnItemClickListener)
        : ListAdapter<Episode, RecyclerView.ViewHolder>(EpisodeDiffCallback()) {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return EpisodeViewHolder.from(parent)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = getItem(position)
            when (holder) {
                is EpisodeViewHolder ->{
                    val episode = item as Episode
                    holder.bind(episode)
                    holder.binding.conEpisode.setOnClickListener {
                        itemClickListener.onItemClicked(episode)
                    }
                }
            }

        }

        class EpisodeDiffCallback : DiffUtil.ItemCallback<Episode>() {
            override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                return oldItem == newItem
            }
        }

        class EpisodeViewHolder(val binding: ItemListEpisodeBinding): RecyclerView.ViewHolder(binding.root) {
            fun bind(episode: Episode) {
                binding.episode = episode
                // This is important, because it forces the data binding to execute immediately,
                // which allows the RecyclerView to make the correct view size measurements
                binding.executePendingBindings()
            }
            companion object {
                fun from(parent: ViewGroup): EpisodeViewHolder {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val binding = ItemListEpisodeBinding.inflate(layoutInflater, parent, false)
                    return EpisodeViewHolder(binding)
                }
            }
        }

        class EpisodeOnItemClickListener(val clickListener: (episode: Episode) -> Unit ){
            fun onItemClicked(episode: Episode) = clickListener(episode)
        }
    }
