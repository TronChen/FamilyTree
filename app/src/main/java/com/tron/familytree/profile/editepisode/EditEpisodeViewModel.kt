package com.tron.familytree.profile.editepisode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.data.source.FamilyTreeRepository

class EditEpisodeViewModel(
    private val repository: FamilyTreeRepository
): ViewModel() {

    val editTitle = MutableLiveData<String>()

}