package com.tron.familytree.profile.edit_family.create_family

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tron.familytree.data.User
import com.tron.familytree.databinding.ItemListEditFamilyMemberBinding
import com.tron.familytree.profile.edit_family.EditFamilyViewModel

class CreateFamilyAdapter
        (private val itemClickListener: CreateFamilyOnItemClickListener
    )
        : ListAdapter<User, RecyclerView.ViewHolder>(CreateFamilyDiffCallback()) {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return CreateFamilyViewHolder.from(parent)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = getItem(position)
            when (holder) {
                is CreateFamilyViewHolder ->{
                    val user = item as User
                    holder.bind(user)
//                    holder.binding.conChoose.setOnClickListener {
//                        itemClickListener.onItemClicked(user)
//                    }
                }
            }

        }

        class CreateFamilyDiffCallback : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }

        class CreateFamilyViewHolder(val binding: ItemListEditFamilyMemberBinding): RecyclerView.ViewHolder(binding.root) {
            fun bind(user: User) {
                binding.user = user
                // This is important, because it forces the data binding to execute immediately,
                // which allows the RecyclerView to make the correct view size measurements
                binding.executePendingBindings()
            }
            companion object {
                fun from(parent: ViewGroup): CreateFamilyViewHolder {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val binding = ItemListEditFamilyMemberBinding.inflate(layoutInflater, parent, false)
                    return CreateFamilyViewHolder(binding)
                }
            }
        }

        class CreateFamilyOnItemClickListener(val clickListener: (user: User) -> Unit ){
            fun onItemClicked(user: User) = clickListener(user)
        }
    }