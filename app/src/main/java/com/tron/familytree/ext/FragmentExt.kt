package com.tron.familytree.ext

import androidx.fragment.app.Fragment
import com.tron.familytree.FamilyTreeApplication
import com.tron.familytree.data.ChatRoom
import com.tron.familytree.data.Episode
import com.tron.familytree.data.Event
import com.tron.familytree.data.User
import com.tron.familytree.factory.*

fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as FamilyTreeApplication).repository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(user: User?): UserViewModelFactory {
    val repository = (requireContext().applicationContext as FamilyTreeApplication).repository
    return UserViewModelFactory(repository, user)
}

fun Fragment.getVmFactory(episode: Episode?): EpisdoeViewModelFactory {
    val repository = (requireContext().applicationContext as FamilyTreeApplication).repository
    return EpisdoeViewModelFactory(repository, episode)
}

fun Fragment.getVmFactory(chatRoom: ChatRoom?): ChatRoomViewModelFactory {
    val repository = (requireContext().applicationContext as FamilyTreeApplication).repository
    return ChatRoomViewModelFactory(repository, chatRoom)
}

fun Fragment.getVmFactory(event: Event?): EventViewModelFactory {
    val repository = (requireContext().applicationContext as FamilyTreeApplication).repository
    return EventViewModelFactory(repository, event)
}
