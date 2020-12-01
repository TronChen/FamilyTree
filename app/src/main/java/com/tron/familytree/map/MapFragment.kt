package com.tron.familytree.map

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.tron.familytree.R
import com.tron.familytree.databinding.FragmentMapBinding
import com.tron.familytree.message.MessageViewModel
import com.tron.familytree.profile.ProfileViewModel

class MapFragment : Fragment() {

    private val viewModel: MapViewModel by lazy {
        ViewModelProvider(this).get(MapViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMapBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        val viewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }
}