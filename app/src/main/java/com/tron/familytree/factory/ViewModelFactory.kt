package com.tron.familytree.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.family.FamilyViewModel
import com.tron.familytree.family.create_album.CreateAlbumViewModel
import com.tron.familytree.family.create_event.CreateEventViewModel
import com.tron.familytree.map.MapViewModel
import com.tron.familytree.message.MessageViewModel
import com.tron.familytree.profile.ProfileViewModel
import com.tron.familytree.profile.editepisode.EditEpisodeViewModel
import com.tron.familytree.profile.edituser.EditUserViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val repository: FamilyTreeRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MessageViewModel::class.java) ->
                    MessageViewModel(repository)

                isAssignableFrom(MapViewModel::class.java) ->
                    MapViewModel(repository)

                isAssignableFrom(ProfileViewModel::class.java) ->
                    ProfileViewModel(repository)

                isAssignableFrom(FamilyViewModel::class.java) ->
                    FamilyViewModel(repository)

                isAssignableFrom(EditUserViewModel::class.java) ->
                    EditUserViewModel(repository)

                isAssignableFrom(EditEpisodeViewModel::class.java) ->
                    EditEpisodeViewModel(repository)

                isAssignableFrom(CreateEventViewModel::class.java) ->
                    CreateEventViewModel(repository)

                isAssignableFrom(CreateAlbumViewModel::class.java) ->
                    CreateAlbumViewModel(repository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}