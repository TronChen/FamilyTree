package com.tron.familytree.family

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.tron.familytree.R
import com.tron.familytree.databinding.FragmentFamilyBinding
import com.tron.familytree.map.MapViewModel

class FamilyFragment : Fragment() {

    private val viewModel: FamilyViewModel by lazy {
        ViewModelProvider(this).get(FamilyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentFamilyBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        val viewModel = ViewModelProvider(this).get(FamilyViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }
}