package com.tron.familytree.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.data.ChatRoom
import com.tron.familytree.data.Episode
import com.tron.familytree.message.chatroom.ChatRoomViewModel
import com.tron.familytree.profile.editepisode.EditEpisodeViewModel

@Suppress("UNCHECKED_CAST")
class ChatRoomViewModelFactory(
    private val repository: FamilyTreeRepository,
    private val chatRoom: ChatRoom?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ChatRoomViewModel::class.java)) {
            return chatRoom?.let {
                ChatRoomViewModel(
                    repository,
                    it
                )
            } as T
        }


        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
