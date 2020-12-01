package com.tron.familytree.branch

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tron.familytree.R
import com.tron.familytree.data.User
import com.tron.familytree.databinding.ItemListBranchUserBinding
import com.tron.familytree.databinding.ItemListEmptyBinding
import com.tron.familytree.databinding.ItemListQueryDownBinding
import com.tron.familytree.databinding.ItemListQueryUpBinding



class TestAdapter(private val itemClickListener: UserOnItemClickListener)
    : ListAdapter<TreeItem, RecyclerView.ViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM_VIEW_TYPE_USER -> {UserViewHolder.from(parent)}
            ITEM_VIEW_TYPE_HORIZONTAL -> {HorStrViewHolder.from(parent)}
            ITEM_VIEW_TYPE_VERTICAL -> {VerticalViewHolder.from(parent)}
            else -> throw ClassCastException("Unknown ViewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when(holder){
            is UserViewHolder ->{
                var user: User? = null
                when (item) {
                    is TreeItem.Parent -> {
                        user = item.user
                    }
                    is TreeItem.Mate -> {
                        user = item.user
                    }
                    is TreeItem.Children -> {
                        user = item.user
                    }
                }
                holder.bind(user)
                holder.itemView.setOnClickListener{
                    itemClickListener.onItemClicked(user)
                }
            }
            is EmptyViewHolder ->{
                val user = item as User
                holder.bind(user)
                holder.itemView.setOnClickListener{
                    itemClickListener.onItemClicked(item)
                }
            }
            is QueryUpViewHolder ->{
                val user = item as User
                holder.bind(user)
                holder.itemView.setOnClickListener{
                    itemClickListener.onItemClicked(item)
                }
            }
            is QueryDownViewHolder ->{
                val user = item as User
                holder.bind(user)
                holder.itemView.setOnClickListener{
                    itemClickListener.onItemClicked(item)
                }
            }
    }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TreeItem.Parent -> ITEM_VIEW_TYPE_USER
            is TreeItem.Mate -> ITEM_VIEW_TYPE_USER
            is TreeItem.Children -> {
                when ((getItem(position) as TreeItem.Children).index) {
                    0 -> ITEM_VIEW_TYPE_VERTICAL
                    else -> ITEM_VIEW_TYPE_HORIZONTAL
                }
            }
        }
    }


    class UserDiffCallback : DiffUtil.ItemCallback<TreeItem>() {
        override fun areItemsTheSame(oldItem: TreeItem, newItem: TreeItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TreeItem, newItem: TreeItem): Boolean {
            return oldItem == newItem
        }
    }


    class UserViewHolder(val binding:ItemListBranchUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            Log.e("UserViewHolder","$user")
            binding.user = user
            val aaa = mutableListOf<User>()
            aaa.add(user)
            Log.e("aaa", aaa.toString())
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListBranchUserBinding.inflate(layoutInflater, parent, false)
                return UserViewHolder(binding)
            }
        }
    }

    class EmptyViewHolder(val binding:ItemListEmptyBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            Log.e("EmptyViewHolder","$user")
            binding.user = user

            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): EmptyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListEmptyBinding.inflate(layoutInflater, parent, false)
                return EmptyViewHolder(binding)
            }
        }
    }

    class QueryUpViewHolder(val binding:ItemListQueryUpBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            Log.e("QueryUpViewHolder","$user")
            binding.user = user

            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): QueryUpViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListQueryUpBinding.inflate(layoutInflater, parent, false)
                return QueryUpViewHolder(binding)
            }
        }
    }

    class QueryDownViewHolder(val binding:ItemListQueryDownBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            Log.e("QueryDownViewHolder","$user")
            binding.user = user

            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): QueryDownViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListQueryDownBinding.inflate(layoutInflater, parent, false)
                return QueryDownViewHolder(binding)
            }
        }
    }

    class HorStrViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): HorStrViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_list_branch_horizontal_straight, parent, false)
                return HorStrViewHolder(view)
            }
        }
    }


    class VerticalViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): VerticalViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_list_vertical, parent, false)
                return VerticalViewHolder(view)
            }
        }
    }

    class UserOnItemClickListener(val clickListener: (user: User) -> Unit ){
        fun onItemClicked(user: User) = clickListener(user)
    }

    companion object {
        const val ITEM_VIEW_TYPE_USER = 0
        const val ITEM_VIEW_TYPE_HORIZONTAL = 2
        const val ITEM_VIEW_TYPE_VERTICAL = 11
    }
}

sealed class TreeItem {
    data class Parent(val user: User, val isMine: Boolean): TreeItem() {

    }

    data class Mate(val user: User, val isMe: Boolean): TreeItem() {

    }

    data class Children(val user: User, val index: Int): TreeItem() {
    }
}