package com.tron.familytree.family.albumdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.tron.familytree.R
import com.tron.familytree.data.Photo
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

        val adapter = AlbumDetailAdapter(AlbumDetailAdapter.AlbumDetailOnItemClickListener{

        })
        binding.recPhoto.adapter = adapter

        viewModel.liveAlbum.observe(viewLifecycleOwner, Observer {
            val photoList = mutableListOf<PhotoItem>()
            it.let {
                it.forEach { photo ->
            photoList.add(PhotoItem.AlbumPhoto(photo))
                }
            }
            photoList.add(PhotoItem.AlbumPhotoAdd(Photo(id = "addPhoto")))
            photoList.add(PhotoItem.AlbumPhotoAdd(Photo(id = "addPhoto")))
            photoList.add(PhotoItem.AlbumPhotoAdd(Photo(id = "addPhoto")))
            photoList.add(PhotoItem.AlbumPhotoAdd(Photo(id = "addPhoto")))
            photoList.add(PhotoItem.AlbumPhotoAdd(Photo(id = "addPhoto")))
            photoList.add(PhotoItem.AlbumPhotoAdd(Photo(id = "addPhoto")))
            photoList.add(PhotoItem.AlbumPhotoAdd(Photo(id = "addPhoto")))
            photoList.add(PhotoItem.AlbumPhotoAdd(Photo(id = "addPhoto")))
            adapter.submitList(photoList)
        })


        // Inflate the layout for this fragment
        return binding.root
    }

}