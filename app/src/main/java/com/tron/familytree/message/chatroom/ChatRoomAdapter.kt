package com.tron.familytree.message.chatroom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tron.familytree.data.Message
import com.tron.familytree.data.User
import com.tron.familytree.databinding.ItemListChatRoomRecieverBinding
import com.tron.familytree.databinding.ItemListChatRoomSenderBinding
import com.tron.familytree.profile.member.MemberItem

class ChatRoomAdapter ()
    : ListAdapter<MessageItem, RecyclerView.ViewHolder>(ChatRoomDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_RECIEVER -> {
                ChatRoomReceiverViewHolder.from(parent)
            }
            ITEM_VIEW_SENDER -> {
                ChatRoomSenderViewHolder.from(parent)
            }
            else -> throw ClassCastException("Unknown ViewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is ChatRoomReceiverViewHolder ->{
//                val message = item as MessageItem.Receiver
//                holder.bind(message)
                var message: Message? = null
                when (item) {
                    is MessageItem.Receiver -> {
                        message = item.userId
                    }
                }
                if (message != null) {
                    holder.bind(message)
                }
            }
            is ChatRoomSenderViewHolder ->{
//                val message = item as MessageItem.Sender
//                holder.bind(message)
                var message: Message? = null
                when (item) {
                    is MessageItem.Sender -> {
                        message = item.userId
                    }
                }
                if (message != null) {
                    holder.bind(message)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MessageItem.Receiver -> ITEM_VIEW_RECIEVER
            is MessageItem.Sender -> ITEM_VIEW_SENDER
        }
    }

    class ChatRoomDiffCallback : DiffUtil.ItemCallback<MessageItem>() {
        override fun areItemsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean {
            return oldItem == newItem
        }
    }

    class ChatRoomReceiverViewHolder(val binding: ItemListChatRoomRecieverBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.message = message
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ChatRoomReceiverViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListChatRoomRecieverBinding.inflate(layoutInflater, parent, false)
                return ChatRoomReceiverViewHolder(binding)
            }
        }
    }

    class ChatRoomSenderViewHolder(val binding: ItemListChatRoomSenderBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.message = message
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ChatRoomSenderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListChatRoomSenderBinding.inflate(layoutInflater, parent, false)
                return ChatRoomSenderViewHolder(binding)
            }
        }
    }

    companion object{
        const val ITEM_VIEW_RECIEVER = 0
        const val ITEM_VIEW_SENDER = 1
    }
}

sealed class MessageItem{
    data class Receiver(val userId: Message): MessageItem() {}
    data class Sender(val userId: Message): MessageItem() {}
}