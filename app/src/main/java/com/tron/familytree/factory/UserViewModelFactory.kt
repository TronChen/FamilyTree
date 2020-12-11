package com.tron.familytree.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.branch.add_people_dialog.AddPeopleViewModel
import com.tron.familytree.branch.dialog.BranchUserDetailDialogViewModel
import com.tron.familytree.data.User


@Suppress("UNCHECKED_CAST")
class UserViewModelFactory(
    private val repository: FamilyTreeRepository,
    private val user: User?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(BranchUserDetailDialogViewModel::class.java)) {
            return user?.let {
                BranchUserDetailDialogViewModel(
                    repository,
                    it
                )
            } as T
        }
        if (modelClass.isAssignableFrom(AddPeopleViewModel::class.java)) {
            return user?.let {
                AddPeopleViewModel(
                    repository,
                    it
                )
            } as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}