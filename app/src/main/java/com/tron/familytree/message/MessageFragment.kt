package com.tron.familytree.message

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tron.familytree.data.ChatRoom
import com.tron.familytree.data.User
import com.tron.familytree.databinding.FragmentMessageBinding
import com.tron.familytree.ext.getVmFactory
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