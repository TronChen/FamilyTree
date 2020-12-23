package com.tron.familytree.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.data.Event
import com.tron.familytree.data.Photo
import com.tron.familytree.family.album_single_pic.AlbumSinglePicViewModel
import com.tron.familytree.family.albumdetail.AlbumDetailViewModel
import com.tron.familytree.family.event_dialog.EventDialogViewModel

    @Suppress("UNCHECKED_CAST")
    class PhotoViewModelFactory(
        private val repository: FamilyTreeRepository,
        private val photo: Photo?
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            if (modelClass.isAssignableFrom(AlbumSinglePicViewModel::class.java)) {
                return photo?.let {
                    AlbumSinglePicViewModel(
                        repository,
                        it
                    )
                } as T
            }



            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
