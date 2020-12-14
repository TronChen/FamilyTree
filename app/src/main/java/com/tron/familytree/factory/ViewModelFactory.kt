package com.tron.familytree.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.MainActivityViewModel
import com.tron.familytree.family.FamilyViewModel
import com.tron.familytree.family.album.AlbumViewModel
import com.tron.familytree.family.create_album.CreateAlbumViewModel
import com.tron.familytree.family.create_event.CreateEventViewModel
import com.tron.familytree.family.event.EventViewModel
import com.tron.familytree.login.LogInViewModel
import com.tron.familytree.map.MapViewModel
import com.tron.familytree.message.MessageViewModel
import com.tron.familytree.profile.ProfileViewModel
import com.tron.familytree.profile.editepisode.EditEpisodeViewModel
import com.tron.familytree.profile.edituser.EditUserViewModel
import com.tron.familytree.profile.episode.EpisodeViewModel
import com.tron.familytree.profile.member.MemberViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val repository: FamilyTreeRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainActivityViewModel::class.java) ->
                    MainActivityViewModel(repository)

                isAssignableFrom(MessageViewModel::class.java) ->
                    MessageViewModel(repository)

                isAssignableFrom(MapViewModel::class.java) ->
                    MapViewModel(repository)

                isAssignableFrom(ProfileViewModel::class.java) ->
                    ProfileViewModel(repository)

                isAssignableFrom(FamilyViewModel::class.java) ->
                    FamilyViewModel(repository)

                isAssignableFrom(CreateEventViewModel::class.java) ->
                    CreateEventViewModel(repository)

                isAssignableFrom(CreateAlbumViewModel::class.java) ->
                    CreateAlbumViewModel(repository)

                isAssignableFrom(EventViewModel::class.java) ->
                    EventViewModel(repository)

                isAssignableFrom(AlbumViewModel::class.java) ->
                    AlbumViewModel(repository)

                isAssignableFrom(EpisodeViewModel::class.java) ->
                    EpisodeViewModel(repository)

                isAssignableFrom(MemberViewModel::class.java) ->
                    MemberViewModel(repository)

                isAssignableFrom(LogInViewModel::class.java) ->
                    LogInViewModel(repository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}