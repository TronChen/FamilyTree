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
import com.tron.familytree.databinding.*


class TestAdapter(private val itemClickListener: UserOnItemClickListener)
    : ListAdapter<TreeItem, RecyclerView.ViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM_VIEW_TYPE_USER_TOP_WITH_QUERY -> {UserTopHolder.from(parent)}
            ITEM_VIEW_TYPE_USER_DOWN_MID -> {UserDownMiddleViewHolder.from(parent)}
            ITEM_VIEW_TYPE_USER_DOWN_LEFT -> {UserDownLeftViewHolder.from(parent)}
            ITEM_VIEW_TYPE_USER_MID -> {UserMidViewHolder.from(parent)}
            ITEM_VIEW_TYPE_EMPTY -> {EmptyViewHolder.from(parent)}
            ITEM_VIEW_TYPE_EMPTY_LINE -> {EmptyLineViewHolder.from(parent)}
            ITEM_VIEW_TYPE_EMPTY_LINE_BOTTOM -> {EmptyLineBottomViewHolder.from(parent)}
            else -> throw ClassCastException("Unknown ViewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = getItem(position)
        when(holder){
            is UserTopHolder ->{
                var user: User? = null
                when (item) {
                    is TreeItem.Parent -> {
                        user = item.user
                    }
                }
                val parent = item as TreeItem.Parent
                if (user != null) {
                    holder.bind(user, parent, position)
                }
                holder.itemView.setOnClickListener{
                    if (user != null) {
                        itemClickListener.onItemClicked(user)
                    }
                }
            }

            is UserDownLeftViewHolder ->{
//                val user = item as User
                when(position){
                currentList.size -1 -> holder.binding.left.visibility = View.GONE
                }
                val children = item as TreeItem.Children
                holder.bind(children,position)
            }

//            is EmptyViewHolder ->{
//                val user = item as User
//                holder.bind(user)
//                holder.itemView.setOnClickListener{
//                    itemClickListener.onItemClicked(item)
//                }
//            }
    }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TreeItem.Parent -> ITEM_VIEW_TYPE_USER_TOP_WITH_QUERY
            is TreeItem.Mate -> ITEM_VIEW_TYPE_USER_MID
            is TreeItem.Children -> ITEM_VIEW_TYPE_USER_DOWN_LEFT
            is TreeItem.ChildrenMid -> ITEM_VIEW_TYPE_USER_DOWN_MID
            is TreeItem.EmptyLine -> ITEM_VIEW_TYPE_EMPTY_LINE
            is TreeItem.EmptyLineBot -> ITEM_VIEW_TYPE_EMPTY_LINE_BOTTOM
//            {
//                when ((getItem(position) as TreeItem.Children).index) {
//                    0 -> ITEM_VIEW_TYPE_USER_DOWN_LEFT
//                    else -> ITEM_VIEW_TYPE_USER_DOWN_MID
//                }
//            }
            is TreeItem.Empty -> ITEM_VIEW_TYPE_EMPTY
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


    class UserTopHolder(val binding:ItemListUserTopLeftBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User,item: TreeItem.Parent, allItemCount: Int) {
            if (item.index == 0) {
                binding.right.visibility = View.GONE
            }
            if (item.index == 1) {
                binding.left.visibility = View.GONE
            }
            if (item.index == 2) {
                binding.right.visibility = View.GONE
            }
            if (item.index == 3) {
                binding.left.visibility = View.GONE
            }

            Log.e("UserViewHolder","$user")
            binding.user = user
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListUserTopLeftBinding.inflate(layoutInflater, parent, false)
                return UserTopHolder(binding)
            }
        }
    }

    class UserDownMiddleViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): UserDownMiddleViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_list_user_down_mid, parent, false)
                return UserDownMiddleViewHolder(view)
            }
        }
    }

    class UserDownLeftViewHolder(val binding:ItemListUserDownLeftBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TreeItem.Children, allItemCount: Int) {
//            binding.user = user
            if (item.index == 0) {
                // background = |''
                binding.right.visibility = View.GONE
            }
            Log.e("adapterPosition", adapterPosition.toString())
            Log.e("allItemCount", allItemCount.toString())
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): UserDownLeftViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListUserDownLeftBinding.inflate(layoutInflater, parent, false)
                return UserDownLeftViewHolder(binding)
            }
        }
    }

    class UserMidViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): UserMidViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_list_user_mid_left, parent, false)
                return UserMidViewHolder(view)
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

    class EmptyLineViewHolder(val binding:ItemListEmptyLineBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            Log.e("EmptyViewHolder","$user")
            binding.user = user

            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): EmptyLineViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListEmptyLineBinding.inflate(layoutInflater, parent, false)
                return EmptyLineViewHolder(binding)
            }
        }
    }

    class EmptyLineBottomViewHolder(val binding:ItemListEmptyLineBottomBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            Log.e("EmptyViewHolder","$user")
            binding.user = user

            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): EmptyLineBottomViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListEmptyLineBottomBinding.inflate(layoutInflater, parent, false)
                return EmptyLineBottomViewHolder(binding)
            }
        }
    }



    class UserOnItemClickListener(val clickListener: (user: User) -> Unit ){
        fun onItemClicked(user: User) = clickListener(user)
    }

    companion object {
        const val ITEM_VIEW_TYPE_USER_TOP_WITH_QUERY = 0
        const val ITEM_VIEW_TYPE_USER_DOWN_MID = 1
        const val ITEM_VIEW_TYPE_USER_DOWN_LEFT = 2
        const val ITEM_VIEW_TYPE_USER_MID = 3
        const val ITEM_VIEW_TYPE_EMPTY = 4
        const val ITEM_VIEW_TYPE_EMPTY_LINE = 5
        const val ITEM_VIEW_TYPE_EMPTY_LINE_BOTTOM = 6
    }
}

sealed class TreeItem {
    data class Parent(val user: User, val isMine: Boolean,  val index: Int): TreeItem() {

    }

    data class Mate(val user: User, val isMe: Boolean): TreeItem() {

    }

    data class Children(val user: User, val index: Int): TreeItem() {
    }

    data class ChildrenMid(val user: User, val index: Int): TreeItem() {
    }

    data class Empty(val type: Int): TreeItem() {
    }

    data class EmptyLine(val type: Int): TreeItem() {
    }

    data class EmptyLineBot(val type: Int): TreeItem() {
    }


}