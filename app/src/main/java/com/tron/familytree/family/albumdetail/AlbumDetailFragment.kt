package com.tron.familytree.family.albumdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tron.familytree.R
import com.tron.familytree.databinding.DialogEventBinding
import com.tron.familytree.databinding.FragmentAlbumDetailBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.family.albumdetail.AlbumDetailFragmentArgs.Companion.fromBundle
import com.tron.familytree.family.event_dialog.EventDialogArgs
import com.tron.familytree.family.event_dialog.EventDialogViewModel


class AlbumDetailFragment : Fragment() {

    private val viewModel by viewModels<AlbumDetailViewModel> {  getVmFactory(
        AlbumDetailFragmentArgs.fromBundle(
            requireArguments()
        ).eventProperties)}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        // Inflate the layout for this fragment
        return binding.root
    }

}