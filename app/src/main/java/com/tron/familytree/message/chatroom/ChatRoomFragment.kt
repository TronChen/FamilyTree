package com.tron.familytree.message.chatroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tron.familytree.MainActivity
import com.tron.familytree.R
import com.tron.familytree.databinding.FragmentChatRoomBinding
import com.tron.familytree.databinding.FragmentMessageBinding
import com.tron.familytree.ext.getVmFactory

class ChatRoomFragment : Fragment() {

    private val viewModel by viewModels<ChatRoomViewModel> { getVmFactory(
        ChatRoomFragmentArgs.fromBundle(
            requireArguments()
        ).chatRoomProperties)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentChatRoomBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        // Inflate the layout for this fragment
        return binding.root
    }
}