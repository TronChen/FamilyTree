package com.tron.familytree.family.event_dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tron.familytree.data.Episode
import com.tron.familytree.data.User
import com.tron.familytree.databinding.ItemListEpisodeEditBinding
import com.tron.familytree.databinding.ItemListEventDialogAttenderBinding

class EventDialogAdapter (private val itemClickListener: EventDialogOnItemClickListener)
        : ListAdapter<User, RecyclerView.ViewHolder>(EventDialogDiffCallback()) {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return EventDialogViewHolder.from(
                parent
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = getItem(position)
            when (holder) {
                is EventDialogViewHolder ->{
                    val user = item as User
                    holder.bind(user)
                }
            }

        }

        class EventDialogDiffCallback : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }

        class EventDialogViewHolder(val binding: ItemListEventDialogAttenderBinding): RecyclerView.ViewHolder(binding.root) {
            fun bind(user: User) {
                binding.user = user
                // This is important, because it forces the data binding to execute immediately,
                // which allows the RecyclerView to make the correct view size measurements
                binding.executePendingBindings()
            }
            companion object {
                fun from(parent: ViewGroup): EventDialogViewHolder {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val binding = ItemListEventDialogAttenderBinding.inflate(layoutInflater, parent, false)
                    return EventDialogViewHolder(
                        binding
                    )
                }
            }
        }

        class EventDialogOnItemClickListener(val clickListener: (user: User) -> Unit ){
            fun onItemClicked(user: User) = clickListener(user)
        }
    }