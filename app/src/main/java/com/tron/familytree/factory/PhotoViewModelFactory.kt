package com.tron.familytree.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.data.Photo

    @Suppress("UNCHECKED_CAST")
    class PhotoViewModelFactory(
        private val repository: FamilyTreeRepository,
        private val photo: Photo?
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {


            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
