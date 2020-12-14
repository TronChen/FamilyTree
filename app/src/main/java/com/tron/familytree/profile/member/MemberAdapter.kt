package com.tron.familytree.profile.member

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tron.familytree.branch.BranchViewAdapter
import com.tron.familytree.branch.BranchViewModel
import com.tron.familytree.branch.TreeItem
import com.tron.familytree.data.User
import com.tron.familytree.databinding.*

class MemberAdapter(private val itemClickListener: UserOnItemClickListener)
    : ListAdapter<MemberItem, RecyclerView.ViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM_VIEW_TYPE_FATHER ->{ FatherViewHolder.from(parent) }
            ITEM_VIEW_TYPE_FATHER_ADD ->{ FatherViewHolder.from(parent) }
            ITEM_VIEW_TYPE_MOTHER ->{ MotherViewHolder.from(parent) }
            ITEM_VIEW_TYPE_MOTHER_ADD ->{ MotherViewHolder.from(parent) }
            ITEM_VIEW_TYPE_MATE ->{ MateViewHolder.from(parent) }
            ITEM_VIEW_TYPE_MATE_ADD ->{ MateViewHolder.from(parent) }
            ITEM_VIEW_TYPE_CHILDREN ->{ ChildrenViewHolder.from(parent) }
            ITEM_VIEW_TYPE_CHILDREN_ADD ->{ ChildrenViewHolder.from(parent) }

            else -> throw ClassCastException("Unknown ViewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = getItem(position)
        when(holder){
            is FatherViewHolder ->{
                var user: User? = null
                when (item) {
                    is MemberItem.Father -> {
                        user = item.user
                    }
                    is MemberItem.FatherAdd -> {
                        user = item.user
                    }
                }
                if (user != null) {
                    holder.bind(user)
                    holder.binding.cirImgMember.setOnClickListener {
                    itemClickListener.onItemClicked(user!!)
                    }
                }
            }
            is MotherViewHolder ->{
                var user: User? = null
                when (item) {
                    is MemberItem.Mother -> {
                        user = item.user
                    }
                    is MemberItem.MotherAdd -> {
                        user = item.user
                    }
                }
                if (user != null) {
                    holder.bind(user)
                    holder.binding.cirImgMember.setOnClickListener {
                        itemClickListener.onItemClicked(user!!)
                    }
                }
            }
            is MateViewHolder ->{
                var user: User? = null
                when (item) {
                    is MemberItem.Mate -> {
                        user = item.user
                    }
                    is MemberItem.MateAdd -> {
                        user = item.user
                    }
                }
                if (user != null) {
                    holder.bind(user)
                    holder.binding.cirImgMember.setOnClickListener {
                        itemClickListener.onItemClicked(user!!)
                    }
                }
            }
            is ChildrenViewHolder ->{
                var user: User? = null
                when (item) {
                    is MemberItem.Children -> {
                        user = item.user
                    }
                    is MemberItem.ChildrenAdd -> {
                        user = item.user
                    }
                }
                if (user != null) {
                    holder.bind(user)
                    holder.binding.cirImgMember.setOnClickListener {
                        itemClickListener.onItemClicked(user!!)
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MemberItem.Father -> ITEM_VIEW_TYPE_FATHER
            is MemberItem.FatherAdd -> ITEM_VIEW_TYPE_FATHER_ADD
            is MemberItem.Mother -> ITEM_VIEW_TYPE_MOTHER
            is MemberItem.MotherAdd -> ITEM_VIEW_TYPE_MOTHER_ADD
            is MemberItem.Mate -> ITEM_VIEW_TYPE_MATE
            is MemberItem.MateAdd -> ITEM_VIEW_TYPE_MATE_ADD
            is MemberItem.Children -> ITEM_VIEW_TYPE_CHILDREN
            is MemberItem.ChildrenAdd -> ITEM_VIEW_TYPE_CHILDREN_ADD
        }
    }



    class FatherViewHolder(val binding: ItemListMemberFatherBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user:User) {
            binding.user = user
            Log.e("father", user.toString())
            if (user.name == "No father"){
                user.name = "加入父親"
                binding.textSee.visibility = View.GONE
            }
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): FatherViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListMemberFatherBinding.inflate(layoutInflater, parent, false)
                return FatherViewHolder(binding)
            }
        }
    }

    class MotherViewHolder(val binding: ItemListMemberMotherBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user:User) {
            binding.user = user
            if (user.name == "No mother"){
                user.name = "加入母親"
                binding.textSee.visibility = View.GONE
            }
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): MotherViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListMemberMotherBinding.inflate(layoutInflater, parent, false)
                return MotherViewHolder(binding)
            }
        }
    }

    class MateViewHolder(val binding: ItemListMemberMateBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user:User) {
            binding.user = user
            if (user.name == "No mate"){
                user.name = "加入配偶"
                binding.textSee.visibility = View.GONE
            }
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): MateViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListMemberMateBinding.inflate(layoutInflater, parent, false)
                return MateViewHolder(binding)
            }
        }
    }

    class ChildrenViewHolder(val binding: ItemListMemberChildrenBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user:User) {
            binding.user = user
            if (user.name == "No children"){
                user.name = "加入孩子"
                binding.textSee.visibility = View.GONE
            }
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ChildrenViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListMemberChildrenBinding.inflate(layoutInflater, parent, false)
                return ChildrenViewHolder(binding)
            }
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<MemberItem>() {
        override fun areItemsTheSame(oldItem: MemberItem, newItem: MemberItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MemberItem, newItem: MemberItem): Boolean {
            return oldItem == newItem
        }
    }
    class UserOnItemClickListener(val clickListener: (user: User) -> Unit ){
        fun onItemClicked(user: User) = clickListener(user)
    }
    companion object {
        const val ITEM_VIEW_TYPE_FATHER = 0
        const val ITEM_VIEW_TYPE_FATHER_ADD = 1
        const val ITEM_VIEW_TYPE_MOTHER = 2
        const val ITEM_VIEW_TYPE_MOTHER_ADD = 3
        const val ITEM_VIEW_TYPE_MATE = 4
        const val ITEM_VIEW_TYPE_MATE_ADD = 5
        const val ITEM_VIEW_TYPE_CHILDREN = 6
        const val ITEM_VIEW_TYPE_CHILDREN_ADD = 7
    }
}

sealed class MemberItem {
    data class Father(val user: User, val isMine: Boolean, val index: Int): MemberItem() {}
    data class FatherAdd(val user: User, val isMine: Boolean, val index: Int): MemberItem() {}
    data class Mother(val user: User, val isMine: Boolean, val index: Int): MemberItem() {}
    data class MotherAdd(val user: User, val isMine: Boolean, val index: Int): MemberItem() {}
    data class Mate(val user: User, val isMe: Boolean): MemberItem() {}
    data class MateAdd(val user: User, val isMe: Boolean): MemberItem() {}
    data class Children(val user: User, val index: Int): MemberItem() {}
    data class ChildrenAdd(val user: User, val index: Int): MemberItem() {}
}