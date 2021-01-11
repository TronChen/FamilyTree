package com.tron.familytree.family.albumdetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.tron.familytree.MainActivity
import com.tron.familytree.data.Photo
import com.tron.familytree.databinding.FragmentAlbumDetailBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.util.UserManager

var ADD_PHOTO = 333

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
                viewModel.photoPath.value = null
//                findNavController().navigate(AlbumDetailFragmentDirections.actionGlobalAlbumSinglePicFragment(it))
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

        viewModel.addPhotoPath.observe(viewLifecycleOwner, Observer {
            Log.e("AddPhotoFilePath", it)
            if (it != null) {
                viewModel.uploadImage(it)
            }
        })

        viewModel.photoPath.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.e("photoPath", it)
            viewModel.addPhoto(viewModel.selectedProperty.value!!,setPhoto())
            }
        })


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        Log.e("onPause","onPause")
        viewModel.photoPath.value = null
    }

    fun setPhoto() : Photo{
        return Photo(
            publisher = UserManager.email.toString(),
            photo = viewModel.photoPath.value
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {

                var filePath: String = ImagePicker.getFilePath(data) ?: ""
                when (requestCode and 0x0000ffff) {
                    com.tron.familytree.ADD_PHOTO -> {
                        if (data != null) {
                            if (filePath.isNotEmpty()) {
                                viewModel.addPhotoPath.value = filePath
                            }
                        }
                    }
                }
            }
        }
    }

}