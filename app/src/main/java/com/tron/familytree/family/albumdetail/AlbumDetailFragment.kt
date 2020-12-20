package com.tron.familytree.family.albumdetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.tron.familytree.MainActivity
import com.tron.familytree.R
import com.tron.familytree.data.Photo
import com.tron.familytree.databinding.DialogEventBinding
import com.tron.familytree.databinding.FragmentAlbumDetailBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.family.albumdetail.AlbumDetailFragmentArgs.Companion.fromBundle
import com.tron.familytree.family.event_dialog.EventDialogArgs
import com.tron.familytree.family.event_dialog.EventDialogViewModel
import com.tron.familytree.profile.edituser.EDIT_USER
import com.tron.familytree.util.UserManager

const val ADD_PHOTO = 333

class AlbumDetailFragment : Fragment() {

    private val viewModel by viewModels<AlbumDetailViewModel> {  getVmFactory(
        AlbumDetailFragmentArgs.fromBundle(
            requireArguments()
        ).eventProperties)}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val activity = activity as MainActivity

        val binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = AlbumDetailAdapter(AlbumDetailAdapter.AlbumDetailOnItemClickListener{
            Log.e("PhotoClick", it.toString())
            if (it.id == "addPhoto"){
                getActivity()
                ImagePicker.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(
                        1080,
                        1080
                    ) //Final image resolution will be less than 1080 x 1080(Optional)
                    .start(ADD_PHOTO)
            }else{
                findNavController().navigate(AlbumDetailFragmentDirections.actionGlobalAlbumSinglePicFragment(it))
            }
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

            adapter.submitList(photoList)
        })

        activity.viewModel.addPhotoPath.observe(viewLifecycleOwner, Observer {
            Log.e("AddPhotoFilePath", it)
            viewModel.uploadImage(it)
        })

        viewModel.photoPath.observe(viewLifecycleOwner, Observer {
            viewModel.addPhoto(viewModel.selectedProperty.value!!,setPhoto())
        })


        // Inflate the layout for this fragment
        return binding.root
    }

    fun setPhoto() : Photo{
        return Photo(
            publisher = UserManager.email.toString(),
            photo = viewModel.photoPath.value
        )
    }

}