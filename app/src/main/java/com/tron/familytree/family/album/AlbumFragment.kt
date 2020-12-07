package com.tron.familytree.family.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tron.familytree.R
import com.tron.familytree.databinding.FragmentAlbumBinding
import com.tron.familytree.databinding.FragmentFamilyBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.family.FamilyViewModel


class AlbumFragment(val position : Int) : Fragment() {

    private val viewModel by viewModels<AlbumViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentAlbumBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.recyclerAlbum.adapter = AlbumAdapter()




        return binding.root
    }
}