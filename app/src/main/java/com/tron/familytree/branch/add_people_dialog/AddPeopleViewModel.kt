package com.tron.familytree.branch.add_people_dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.data.User

class AddPeopleViewModel(
    private val repository: FamilyTreeRepository
    , private val user: User
) : ViewModel() {

    private val _selectedProperty = MutableLiveData<User>()
    // The external LiveData for the SelectedProperty
    val selectedProperty: LiveData<User>
        get() = _selectedProperty

    val _updateImg = MutableLiveData<String>()
    // The external LiveData for the SelectedProperty
    val updateImg: LiveData<String>
        get() = _updateImg


    init {
        _selectedProperty.value = user
    }
}