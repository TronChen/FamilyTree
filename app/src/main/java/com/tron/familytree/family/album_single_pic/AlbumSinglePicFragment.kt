package com.tron.familytree.family.album_single_pic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tron.familytree.MainActivity
import com.tron.familytree.R
import com.tron.familytree.databinding.FragmentAlbumDetailBinding
import com.tron.familytree.databinding.FragmentAlbumSinglePicBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.family.albumdetail.AlbumDetailFragmentArgs
import com.tron.familytree.family.albumdetail.AlbumDetailViewModel

class AlbumSinglePicFragment : Fragment() {

    private val viewModel by viewModels<AlbumSinglePicViewModel> {  getVmFactory(
        AlbumSinglePicFragmentArgs.fromBundle(
            requireArguments()
        ).photoProperties)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentAlbumSinglePicBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        // Inflate the layout for this fragment
        return binding.root
    }

}