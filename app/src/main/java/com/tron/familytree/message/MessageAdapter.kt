package com.tron.familytree.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tron.familytree.data.ChatRoom
import com.tron.familytree.data.Episode
import com.tron.familytree.databinding.ItemListChatRoomBinding
import com.tron.familytree.databinding.ItemListEpisodeBinding

class MessageAdapter (private val itemClickListener: MessageOnItemClickListener)
    : ListAdapter<ChatRoom, RecyclerView.ViewHolder>(MessageDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessageViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is MessageViewHolder ->{
                val chatRoom = item as ChatRoom
                holder.bind(chatRoom)
                holder.itemView.setOnClickListener {
                    itemClickListener.onItemClicked(chatRoom)
                }
            }
        }

    }

    class MessageDiffCallback : DiffUtil.ItemCallback<ChatRoom>() {
        override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem == newItem
        }
    }

    class MessageViewHolder(val binding: ItemListChatRoomBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatRoom: ChatRoom) {
            binding.chatRoom = chatRoom
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): MessageViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListChatRoomBinding.inflate(layoutInflater, parent, false)
                return MessageViewHolder(binding)
            }
        }
    }

    class MessageOnItemClickListener(val clickListener: (chatRoom: ChatRoom) -> Unit ){
        fun onItemClicked(chatRoom: ChatRoom) = clickListener(chatRoom)
    }
}
