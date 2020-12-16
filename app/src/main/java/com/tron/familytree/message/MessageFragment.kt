package com.tron.familytree.message

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.UserManagerCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tron.familytree.MainActivity
import com.tron.familytree.R
import com.tron.familytree.branch.dialog.BranchUserDetailDialogArgs
import com.tron.familytree.data.ChatRoom
import com.tron.familytree.data.User
import com.tron.familytree.databinding.FragmentMessageBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.family.FamilyViewModel
import com.tron.familytree.map.MapViewModel
import com.tron.familytree.profile.ProfileViewModel
import com.tron.familytree.util.UserManager

class MessageFragment : Fragment() {

    private val viewModel by viewModels<MessageViewModel> { getVmFactory(
        MessageFragmentArgs.fromBundle(
            requireArguments()
        ).userProperties)}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMessageBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = MessageAdapter(MessageAdapter.MessageOnItemClickListener{
//            val attenderId = mutableListOf<String>()
//            val attenderName = mutableListOf<String>()
//            val userImage = mutableListOf<String>()
//            for (i in it.attenderId) {
//                attenderId.add(i)
//            }
//            attenderId.add(UserManager.email.toString())
//            for(i in it.attenderName) {
//                attenderName.add(i)
//            }
//            attenderName.add(UserManager.name.toString())
//            for (i in it.userImage) {
//                userImage.add(i)
//            }
//            userImage.add(UserManager.photo.toString())
//            it.attenderId = attenderId
//            it.attenderName = attenderName
//            it.userImage = userImage
            Log.e("MessageItemClick", it.toString())

            findNavController().navigate(MessageFragmentDirections.actionGlobalChatRoomFragment(it))
        })

        binding.recyclerChat.adapter = adapter

        viewModel.selectedProperty.observe(viewLifecycleOwner, Observer {
            Log.e("Message", it.toString())
            //Observe 帶過來的 user id , 如果與之沒有創建過聊天室,創建一個,如果id是自己則不創建
            if (it.id != UserManager.email){
                viewModel.findUserById(it.id)
            }
        })

        viewModel._chatMember.observe(viewLifecycleOwner, Observer {
                viewModel.addChatroom(setChatroom(it))
        })

        viewModel.liveChatroom.observe(viewLifecycleOwner, Observer {
            Log.e("LiveChatRoom", it.toString())
            adapter.submitList(it)
        })

        return binding.root
    }

    fun setChatroom(user: User) : ChatRoom{
        return ChatRoom(
            id = "",
            attenderId = listOf(user.id,UserManager.email.toString()),
            userImage = listOf(user.userImage.toString(),UserManager.photo.toString()),
            attenderName = listOf(viewModel._chatMember.value!!.name, UserManager.name.toString())
        )
    }
}