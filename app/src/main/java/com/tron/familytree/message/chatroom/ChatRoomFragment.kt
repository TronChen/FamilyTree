package com.tron.familytree.message.chatroom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.tron.familytree.MainActivity
import com.tron.familytree.data.Message
import com.tron.familytree.databinding.FragmentChatRoomBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.util.UserManager
import java.util.Locale.filter

class ChatRoomFragment : Fragment() {

    private val viewModel by viewModels<ChatRoomViewModel> { getVmFactory(
        ChatRoomFragmentArgs.fromBundle(
            requireArguments()
        ).chatRoomProperties)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val activity = activity as MainActivity

        val binding = FragmentChatRoomBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = ChatRoomAdapter()
        binding.recyclerMessage.adapter = adapter

        binding.buttonChatboxSend.setOnClickListener {
            viewModel.addMessage(viewModel.selectedProperty.value!!,setMessage())
            viewModel.textSend.value = ""
            viewModel.liveMessage.value = null
        }

        viewModel.selectedProperty.observe(viewLifecycleOwner, Observer {
            Log.e("Message", it.toString())
        })

        viewModel.liveMessage.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.selectedProperty.observe(viewLifecycleOwner, Observer {
            viewModel.selectedProperty.value?.attenderName?.let{listName ->
                val attenderName = listName.filter { it != UserManager.name }
                var personName: String
                attenderName.forEach {
                    personName = it
                    activity.viewModel.currentFragmentType.value?.value = personName
                }
            }
        })



        // Inflate the layout for this fragment
        return binding.root
    }

    fun setMessage(): Message {

        return Message(
            user = viewModel.chatMember.value!!.id,
            text = viewModel.textSend.value,
            userName = viewModel.chatMember.value!!.name,
            userImage = viewModel.chatMember.value!!.userImage
        )

    }
}