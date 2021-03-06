package com.tron.familytree.branch

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tron.familytree.data.User
import com.tron.familytree.databinding.*


class BranchViewAdapter(private val itemClickListener: UserOnItemClickListener, val viewModel : BranchViewModel)
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
            ITEM_VIEW_TYPE_USER_ADD_TOP -> {UserTopAddViewHolder.from(parent)}
            ITEM_VIEW_TYPE_USER_ADD_MID -> {UserMidAddViewHolder.from(parent)}
            ITEM_VIEW_TYPE_USER_ADD_BOT -> {UserBotAddViewHolder.from(parent)}
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
                holder.binding.constraintLayout3.setOnClickListener{
                    if (user != null) {
                        itemClickListener.onItemClicked(user!!)
                        viewModel.itemClick.value = USER_CLICK
                    }
                }
                holder.binding.imageView2.setOnClickListener{
                    if (user != null) {
                        itemClickListener.onItemClicked(user!!)
                        viewModel.itemClick.value = USER_QUERY
                    }
                }
            }

            is UserTopAddViewHolder ->{
                var user: User? = null
                when (item) {
                    is TreeItem.ParentAdd -> {
                        user = item.user
                    }
                }
                val parent = item as TreeItem.ParentAdd
                if (user != null) {
                    holder.bind(user, parent, position)
                }
                holder.binding.constraintLayout3.setOnClickListener{
                    if (user != null) {
                        itemClickListener.onItemClicked(user!!)
                        viewModel.itemClick.value = ADD_MEMBER
                    }
                }
            }

            is UserMidViewHolder ->{
                var user: User? = null
                when (item) {
                    is TreeItem.Mate -> {
                        user = item.user
                    }
                }
                if (user != null) {
                    holder.bind(user)
                }
                holder.binding.constraintLayout3.setOnClickListener{
                    if (user != null) {
                        itemClickListener.onItemClicked(user!!)
                        viewModel.itemClick.value = USER_CLICK
                    }
                }
            }

            is UserMidAddViewHolder ->{
                var user: User? = null
                when (item) {
                    is TreeItem.MateAdd -> {
                        user = item.user
                    }
                }
                val mate = item as TreeItem.MateAdd
                if (user != null) {
                    holder.bind(user, mate)
                }
                holder.binding.constraintLayout3.setOnClickListener{
                    if (user != null) {
                        itemClickListener.onItemClicked(user!!)
                        viewModel.itemClick.value = ADD_MEMBER
                    }
                }
            }

            is UserDownMiddleViewHolder ->{
                var user: User? = null
                when (item) {
                    is TreeItem.ChildrenMid -> {
                        user = item.user
                    }
                }
                if (user != null) {
                    holder.bind(user)
                }
                holder.binding.imageView2.setOnClickListener{
                    if (user != null) {
                        itemClickListener.onItemClicked(user!!)
                        viewModel.itemClick.value = USER_QUERY

                    }
                }
                holder.binding.constraintLayout3.setOnClickListener{
                    if (user != null) {
                        itemClickListener.onItemClicked(user!!)
                        viewModel.itemClick.value = USER_CLICK
                    }
                }
            }


            is UserDownLeftViewHolder ->{
                    var user: User? = null
                    when (item) {
                        is TreeItem.Children -> {
                            user = item.user
                        }
                    }
                when(position){
                currentList.size -1 -> holder.binding.left.visibility = View.GONE
                }
                    val children = item as TreeItem.Children
                    if (user != null) {
                        holder.bind(user, children, position)
                    }
                holder.binding.queryDown.setOnClickListener{
                    if (user != null) {
                        itemClickListener.onItemClicked(user!!)
                        viewModel.itemClick.value = USER_QUERY
                    }
                }
                holder.binding.constraintLayout4.setOnClickListener{
                    if (user != null) {
                        itemClickListener.onItemClicked(user!!)
                        viewModel.itemClick.value = USER_CLICK
                    }
                }
            }

            is UserBotAddViewHolder ->{
                var user: User? = null
                when (item) {
                    is TreeItem.ChildrenAdd -> {
                        user = item.user
                    }
                }
                when(position){
                    currentList.size -1 -> holder.binding.left.visibility = View.GONE
                }
                val children = item as TreeItem.ChildrenAdd
                if (user != null) {
                    holder.bind(user, children, position)
                }
                holder.binding.constraintLayout4.setOnClickListener{
                    if (user != null) {
                        itemClickListener.onItemClicked(user!!)
                        viewModel.itemClick.value = ADD_MEMBER
                    }
                }
            }
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
            is TreeItem.Empty -> ITEM_VIEW_TYPE_EMPTY
            is TreeItem.ParentAdd -> ITEM_VIEW_TYPE_USER_ADD_TOP
            is TreeItem.MateAdd -> ITEM_VIEW_TYPE_USER_ADD_MID
            is TreeItem.ChildrenAdd -> ITEM_VIEW_TYPE_USER_ADD_BOT
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

            when (item.index) {
                0, 2 -> {
                    binding.right.visibility = View.GONE
                    binding.left.visibility = View.VISIBLE
                }
                1, 3 -> {
                    binding.right.visibility = View.VISIBLE
                    binding.left.visibility = View.GONE
                }
            }
            Log.e("UserViewHolder","$user")
            Log.e("UserViewHolder","${item.index}")
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

    class UserDownMiddleViewHolder(val binding:ItemListUserDownMidBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user:User) {
            binding.user = user
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): UserDownMiddleViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListUserDownMidBinding.inflate(layoutInflater, parent, false)
                return UserDownMiddleViewHolder(binding)
            }
        }
    }

    class UserDownLeftViewHolder(val binding:ItemListUserDownLeftBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user:User,item: TreeItem.Children, allItemCount: Int) {
            binding.user = user
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

    class UserMidViewHolder(val binding:ItemListUserMidLeftBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user:User) {
            binding.user = user
            Log.e("adapterPosition", adapterPosition.toString())
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): UserMidViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListUserMidLeftBinding.inflate(layoutInflater, parent, false)
                return UserMidViewHolder(binding)
            }
        }
    }

    class UserTopAddViewHolder(val binding:ItemListUserAddTopBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User,item: TreeItem.ParentAdd, allItemCount: Int) {
            when (item.index) {
                0,4 -> {
                    binding.right.visibility = View.GONE
                    binding.left.visibility = View.VISIBLE

                }
                1,5 -> {
                    binding.right.visibility = View.VISIBLE
                    binding.left.visibility = View.GONE
                }
            }
            Log.e("UserTopAddViewHolder", "$user")
            Log.e("UserTopAddViewHolder", "${item.index}")
        }
        companion object {
            fun from(parent: ViewGroup): UserTopAddViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListUserAddTopBinding.inflate(layoutInflater, parent, false)
                return UserTopAddViewHolder(binding)
            }
        }
    }

    class UserMidAddViewHolder(val binding:ItemListUserAddMidBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user:User,item: TreeItem.MateAdd) {
            binding.user = user
            if (item.user.name == "No mate"){
                binding.viewTop.visibility = View.GONE
            }
            Log.e("adapterPosition", adapterPosition.toString())
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): UserMidAddViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListUserAddMidBinding.inflate(layoutInflater, parent, false)
                return UserMidAddViewHolder(binding)
            }
        }
    }

    class UserBotAddViewHolder(val binding:ItemListUserAddBottomBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user:User,item: TreeItem.ChildrenAdd, allItemCount: Int) {
            binding.user = user
            if (item.index != 0) {
                // background = |''
                binding.left.visibility = View.GONE
            }
            if (item.index == 1){
                binding.left.visibility = View.VISIBLE
                binding.right.visibility = View.VISIBLE
            }
            if (item.index == 3){
                binding.left.visibility = View.VISIBLE
                binding.right.visibility = View.VISIBLE
            }
            Log.e("adapterPosition", adapterPosition.toString())
            Log.e("allItemCount", allItemCount.toString())
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): UserBotAddViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListUserAddBottomBinding.inflate(layoutInflater, parent, false)
                return UserBotAddViewHolder(binding)
            }
        }
    }


    class EmptyViewHolder(val binding:ItemListEmptyBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): EmptyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListEmptyBinding.inflate(layoutInflater, parent, false)
                return EmptyViewHolder(binding)
            }
        }
    }

    class EmptyLineViewHolder(val binding:ItemListEmptyLineBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): EmptyLineViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListEmptyLineBinding.inflate(layoutInflater, parent, false)
                return EmptyLineViewHolder(binding)
            }
        }
    }

    class EmptyLineBottomViewHolder(val binding:ItemListEmptyLineBottomBinding): RecyclerView.ViewHolder(binding.root) {
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
        const val ITEM_VIEW_TYPE_USER_ADD_TOP = 7
        const val ITEM_VIEW_TYPE_USER_ADD_MID = 8
        const val ITEM_VIEW_TYPE_USER_ADD_BOT = 9
        const val USER_CLICK = 100
        const val USER_QUERY = 200
        const val ADD_MEMBER = 300
    }
}

sealed class TreeItem {
    data class Parent(val user: User, val isMine: Boolean,  val index: Int): TreeItem() {}
    data class ParentAdd(val user: User, val isMine: Boolean,  val index: Int): TreeItem() {}
    data class Mate(val user: User, val isMe: Boolean): TreeItem() {}
    data class MateAdd(val user: User, val isMe: Boolean): TreeItem() {}
    data class Children(val user: User, val index: Int): TreeItem() {}
    data class ChildrenAdd(val user: User, val index: Int): TreeItem() {}
    data class ChildrenMid(val user: User, val index: Int): TreeItem() {}
    data class Empty(val type: Int): TreeItem() {}
    data class EmptyLine(val type: Int): TreeItem() {}
    data class EmptyLineBot(val type: Int): TreeItem() {}
}