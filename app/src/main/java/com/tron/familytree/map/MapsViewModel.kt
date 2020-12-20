package com.tron.familytree.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.util.UserManager

class MapsViewModel(
    private val repository: FamilyTreeRepository
) : ViewModel() {
    val userImage = MutableLiveData<String>()
    init {
        userImage.value = UserManager.photo.toString()
    }


}