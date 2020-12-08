package com.tron.familytree.message

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tron.familytree.MainActivity
import com.tron.familytree.R
import com.tron.familytree.databinding.FragmentMessageBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.family.FamilyViewModel
import com.tron.familytree.map.MapViewModel
import com.tron.familytree.profile.ProfileViewModel

class MessageFragment : Fragment() {

    private val viewModel by viewModels<MessageViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMessageBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.conChatRoom.setOnClickListener {
            findNavController().navigate(R.id.action_global_chatRoomFragment)
        }

        return binding.root
    }
}