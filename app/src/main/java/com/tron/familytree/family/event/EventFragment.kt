package com.tron.familytree.family.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tron.familytree.R
import com.tron.familytree.databinding.FragmentEventBinding
import com.tron.familytree.databinding.FragmentFamilyBinding


class EventFragment(val position: Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEventBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
//        binding.viewModel = viewModel

        val adapter = EventAdapter()
        binding.recyclerEvent.adapter = adapter

        return binding.root
    }
}