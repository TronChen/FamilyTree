package com.tron.familytree

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.util.CurrentFragmentType

class MainActivityViewModel(
    private val repository: FamilyTreeRepository
): ViewModel() {

    val currentFragmentType = MutableLiveData<CurrentFragmentType>()

}