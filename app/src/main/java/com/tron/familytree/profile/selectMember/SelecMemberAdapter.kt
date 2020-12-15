package com.tron.familytree.profile.selectMember

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tron.familytree.data.Episode
import com.tron.familytree.data.User
import com.tron.familytree.databinding.ItemListEpisodeEditBinding
import com.tron.familytree.databinding.ItemListSelectMemberBinding

class SelecMemberAdapter(private val itemClickListener: SelecMemberOnItemClickListener)
    : ListAdapter<User, RecyclerView.ViewHolder>(SelecMemberDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SelecMemberViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is SelecMemberViewHolder ->{
                val user = item as User
                holder.bind(user)
                holder.binding.textSelect.setOnClickListener {
                    itemClickListener.onItemClicked(user)
                }
            }
        }

    }

    class SelecMemberDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    class SelecMemberViewHolder(val binding: ItemListSelectMemberBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.user = user
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): SelecMemberViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListSelectMemberBinding.inflate(layoutInflater, parent, false)
                return SelecMemberViewHolder(
                    binding
                )
            }
        }
    }

    class SelecMemberOnItemClickListener(val clickListener: (user: User) -> Unit ){
        fun onItemClicked(user: User) = clickListener(user)
    }
}