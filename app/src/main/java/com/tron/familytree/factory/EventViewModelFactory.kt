package com.tron.familytree.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.data.Episode
import com.tron.familytree.data.Event
import com.tron.familytree.family.event_dialog.EventDialogViewModel
import com.tron.familytree.profile.editepisode.EditEpisodeViewModel


    @Suppress("UNCHECKED_CAST")
    class EventViewModelFactory(
        private val repository: FamilyTreeRepository,
        private val event: Event?
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            if (modelClass.isAssignableFrom(EventDialogViewModel::class.java)) {
                return event?.let {
                    EventDialogViewModel(
                        repository,
                        it
                    )
                } as T
            }


            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }