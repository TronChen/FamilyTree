package com.tron.familytree.family.albumdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tron.familytree.data.Event
import com.tron.familytree.data.Photo
import com.tron.familytree.data.User
import com.tron.familytree.databinding.ItemListAlbumBinding
import com.tron.familytree.databinding.ItemListAlbumDetailAddBinding
import com.tron.familytree.databinding.ItemListAlbumDetailBinding
import com.tron.familytree.profile.member.MemberAdapter
import com.tron.familytree.profile.member.MemberItem

class AlbumDetailAdapter(private val itemClickListener: AlbumDetailOnItemClickListener)
    : androidx.recyclerview.widget.ListAdapter<PhotoItem, RecyclerView.ViewHolder>(AlbumDetailDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM_VIEW_TYPE_PHOTO -> {AlbumDetailViewHolder.from(parent)}
            ITEM_VIEW_TYPE_PHOTO_ADD -> {AlbumDetailAddViewHolder.from(parent)}

            else -> throw ClassCastException("Unknown ViewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is AlbumDetailViewHolder ->{
                var photo: Photo? = null
                when (item) {
                    is PhotoItem.AlbumPhoto -> {
                        photo = item.photo
                    }
                }
                if (photo != null) {
                    holder.bind(photo)
                    holder.binding.imageView7.setOnClickListener {
                        itemClickListener.onItemClicked(photo!!)
                    }
                }
            }
            is AlbumDetailAddViewHolder ->{
                var photo: Photo? = null
                when (item) {
                    is PhotoItem.AlbumPhotoAdd -> {
                        photo = item.photo
                    }
                }
                if (photo != null) {
                    holder.bind(photo)
                    holder.binding.imgAdd.setOnClickListener {
                        itemClickListener.onItemClicked(photo!!)
                    }
                }
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PhotoItem.AlbumPhoto -> ITEM_VIEW_TYPE_PHOTO
            is PhotoItem.AlbumPhotoAdd -> ITEM_VIEW_TYPE_PHOTO_ADD
        }
    }

    class AlbumDetailDiffCallback : DiffUtil.ItemCallback<PhotoItem>() {
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem == newItem
        }
    }

    class AlbumDetailViewHolder(val binding: ItemListAlbumDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            binding.photo = photo
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AlbumDetailViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListAlbumDetailBinding.inflate(layoutInflater, parent, false)
                return AlbumDetailViewHolder(binding)
            }
        }
    }

    class AlbumDetailAddViewHolder(val binding: ItemListAlbumDetailAddBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            binding.photo = photo
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AlbumDetailAddViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListAlbumDetailAddBinding.inflate(layoutInflater, parent, false)
                return AlbumDetailAddViewHolder(binding)
            }
        }
    }

    class AlbumDetailOnItemClickListener(val clickListener: (photo: Photo) -> Unit) {
        fun onItemClicked(photo: Photo) = clickListener(photo)
    }

    companion object {
        const val ITEM_VIEW_TYPE_PHOTO = 0
        const val ITEM_VIEW_TYPE_PHOTO_ADD = 1
    }
}

sealed class PhotoItem{
    data class AlbumPhoto(val photo: Photo,val index : Int): PhotoItem() {}
    data class AlbumPhotoAdd(val photo: Photo,val index : Int): PhotoItem() {}
}