package com.tron.familytree.profile.editepisode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.data.Episode
import com.tron.familytree.data.User

class EditEpisodeViewModel(
    private val repository: FamilyTreeRepository, private val user: User
): ViewModel() {

    private val _selectedProperty = MutableLiveData<User>()
    // The external LiveData for the SelectedProperty
    val selectedProperty: LiveData<User>
        get() = _selectedProperty

    val editTitle = MutableLiveData<String>()
    val editLocation = MutableLiveData<String>()
    val editContent = MutableLiveData<String>()
    var editDate : String = "選擇時間"


    init {
        _selectedProperty.value = user
    }


    fun setEpisode() : Episode{
        return Episode(
            user = user.id,
            title = editTitle.value!!,
            time = editDate,
            content = editContent.value!!,
            location = editLocation.value!!
        )

    }

}