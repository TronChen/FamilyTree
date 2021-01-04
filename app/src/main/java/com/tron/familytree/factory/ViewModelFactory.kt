package com.tron.familytree.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.MainActivityViewModel
import com.tron.familytree.branch.BranchViewModel
import com.tron.familytree.family.FamilyViewModel
import com.tron.familytree.family.album.AlbumViewModel
import com.tron.familytree.family.calendar.CalendarViewModel
import com.tron.familytree.family.create_event.CreateEventViewModel
import com.tron.familytree.family.event.EventViewModel
import com.tron.familytree.login.LogInViewModel
import com.tron.familytree.map.MapsViewModel
import com.tron.familytree.message.MessageViewModel
import com.tron.familytree.profile.ProfileViewModel
import com.tron.familytree.profile.edit_family.EditFamilyViewModel
import com.tron.familytree.profile.edit_family.create_family.CreateFamilyViewModel
import com.tron.familytree.profile.editepisode.EditEpisodeViewModel
import com.tron.familytree.profile.edituser.EditUserViewModel
import com.tron.familytree.profile.episode.EpisodeViewModel
import com.tron.familytree.profile.member.MemberViewModel
import com.tron.familytree.profile.qrcode.QrCodeReaderViewModel
import com.tron.familytree.profile.qrcode.QrCodeViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val repository: FamilyTreeRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainActivityViewModel::class.java) ->
                    MainActivityViewModel(repository)

                isAssignableFrom(MapsViewModel::class.java) ->
                    MapsViewModel(repository)

                isAssignableFrom(ProfileViewModel::class.java) ->
                    ProfileViewModel(repository)

                isAssignableFrom(FamilyViewModel::class.java) ->
                    FamilyViewModel(repository)

                isAssignableFrom(CreateEventViewModel::class.java) ->
                    CreateEventViewModel(repository)

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

                isAssignableFrom(QrCodeViewModel::class.java) ->
                    QrCodeViewModel(repository)

                isAssignableFrom(CalendarViewModel::class.java) ->
                    CalendarViewModel(repository)

                isAssignableFrom(EditFamilyViewModel::class.java) ->
                    EditFamilyViewModel(repository)

                isAssignableFrom(CreateFamilyViewModel::class.java) ->
                    CreateFamilyViewModel(repository)

                isAssignableFrom(BranchViewModel::class.java) ->
                    BranchViewModel(repository)


                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}