package com.tron.familytree.profile.edit_family

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.tron.familytree.FamilyTreeApplication
import com.tron.familytree.R
import com.tron.familytree.data.AppResult
import com.tron.familytree.data.Episode
import com.tron.familytree.data.Family
import com.tron.familytree.data.User
import com.tron.familytree.data.source.remote.FamilyTreeRemoteDataSource
import com.tron.familytree.databinding.ItemListEditFamilyBinding
import com.tron.familytree.databinding.ItemListEpisodeBinding
import com.tron.familytree.profile.edit_family.create_family.CreateFamilyAdapter
import kotlin.coroutines.resume

class EditFamilyAdapter (
    private val itemClickListener: EditFamilyOnItemClickListener,
    private val viewModel: EditFamilyViewModel
)
    : ListAdapter<Family, RecyclerView.ViewHolder>(EditFamilyDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return EditFamilyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is EditFamilyViewHolder ->{
                val family = item as Family
                holder.bind(family)
                holder.binding.conChoose.setOnClickListener {
                    itemClickListener.onItemClicked(family)
                }
                val adapter = CreateFamilyAdapter(CreateFamilyAdapter.CreateFamilyOnItemClickListener{})
                holder.binding.recMember.adapter = adapter

                FirebaseFirestore.getInstance()
                    .collection("Family")
                    .document(family.id)
                    .collection("User")
                    .addSnapshotListener { value, error ->
                        if (value != null) {
                                val list = mutableListOf<User>()
                            for (index in value){
                                val user = index.toObject(User::class.java)
                                list.add(user)
                            }
                            adapter.submitList(list)
                        }
                    }
            }
        }

    }

    class EditFamilyDiffCallback : DiffUtil.ItemCallback<Family>() {
        override fun areItemsTheSame(oldItem: Family, newItem: Family): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Family, newItem: Family): Boolean {
            return oldItem == newItem
        }
    }

    class EditFamilyViewHolder(val binding: ItemListEditFamilyBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(family: Family) {
            binding.family = family


            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): EditFamilyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListEditFamilyBinding.inflate(layoutInflater, parent, false)
                return EditFamilyViewHolder(binding)
            }
        }
    }

    class EditFamilyOnItemClickListener(val clickListener: (family: Family) -> Unit ){
        fun onItemClicked(family: Family) = clickListener(family)
    }
}