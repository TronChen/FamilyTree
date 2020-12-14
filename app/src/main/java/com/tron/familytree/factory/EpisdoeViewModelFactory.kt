package com.tron.familytree.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.data.Episode
import com.tron.familytree.data.User
import com.tron.familytree.profile.editepisode.EditEpisodeViewModel


@Suppress("UNCHECKED_CAST")
class EpisdoeViewModelFactory(
    private val repository: FamilyTreeRepository,
    private val episode: Episode?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(EditEpisodeViewModel::class.java)) {
            return episode?.let {
                EditEpisodeViewModel(
                    repository,
                    it
                )
            } as T
        }


        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
