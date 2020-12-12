package com.tron.familytree.branch.add_people_dialog

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storageMetadata
import com.mikhaellopez.circularimageview.CircularImageView
import com.tron.familytree.MainActivity
import com.tron.familytree.R
import com.tron.familytree.databinding.DialogAddPeopleBinding
import com.tron.familytree.ext.getVmFactory
import java.io.File
import java.util.*


class AddPeopleDialog : DialogFragment() {

    var mImageview = MutableLiveData<ImageView>()

    private val viewModel by viewModels<AddPeopleViewModel> {  getVmFactory(
        AddPeopleDialogArgs.fromBundle(
            requireArguments()
        ).userProperties)}


    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog!!.window?.setBackgroundDrawableResource(R.drawable.rounded_white_radius65)

        val binding = DialogAddPeopleBinding.inflate(inflater, container, false)

        val activity = activity as MainActivity

            binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel



        viewModel.selectedProperty.observe(viewLifecycleOwner, Observer {
            Log.e("AddMember", it.toString())
        })

        viewModel._userEditName.observe(viewLifecycleOwner, Observer {
            viewModel.userEditName = it
        })

        viewModel._userBirthLocation.observe(viewLifecycleOwner, Observer {
            viewModel.userBirthLocation = it
        })



        binding.conBirth.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(requireContext(),{_, year, month, dayOfMonth ->
                binding.textDate.text = "${setDateFormat(year,month,dayOfMonth)}"
                viewModel.birthDate = "${setDateFormat(year,month,dayOfMonth)}"
            },year,month,dayOfMonth).show()
        }

        binding.conDeath.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(requireContext(),{_, year, month, dayOfMonth ->
                binding.textDeath.text = "${setDateFormat(year,month,dayOfMonth)}"
                viewModel.deathDate = "${setDateFormat(year,month,dayOfMonth)}"
            },year,month,dayOfMonth).show()
        }

        binding.radioGender.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.radioMale -> viewModel.gender = "male"
                R.id.radioFemale -> viewModel.gender = "female"
            }
        }

        binding.textSelectPic.setOnClickListener {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start(1)
        }


        viewModel.userEditName = binding.editName.text.toString()
        viewModel.userBirthLocation = binding.editBirthLocation.text.toString()

        binding.conConfirm.setOnClickListener {
            //判斷 加入的類別
            when (viewModel.selectedProperty.value?.name){
                "No father" -> {
                    viewModel.updateMemberFatherId(viewModel.selectedProperty.value!!, viewModel.setNewMember())
                    viewModel.addMember(viewModel.setNewMember())
                }
                "No mother" -> {
                    viewModel.updateMemberMotherId(viewModel.selectedProperty.value!!, viewModel.setNewMember())
                    viewModel.addMember(viewModel.setNewMember())
                }
                "No mateFather" -> {
                    viewModel.updateMemberFatherId(viewModel.selectedProperty.value!!, viewModel.setNewMember())
                    viewModel.addMember(viewModel.setNewMember())
                }
                "No mateMother" -> {
                    viewModel.updateMemberMotherId(viewModel.selectedProperty.value!!, viewModel.setNewMember())
                    viewModel.addMember(viewModel.setNewMember())
                }
                "No child" -> viewModel.addMember(viewModel.setChild())

            }
        }


        //得到選擇圖片的路徑upload到fire storage
        activity.imgPath.observe(viewLifecycleOwner, Observer {
            viewModel._updateImg.value = it
            Log.e("filePath", it)
            viewModel.uploadImage(it)
        })

        viewModel._userImage.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.e("URLLL", it)
            }
        })









        return binding.root
    }

    private fun setDateFormat(year: Int, month: Int, day: Int): String {
        return "$year/${month + 1}/$day"
    }

//    private fun downloadImg(ref: String) {
//        FirebaseStorage.getInstance().reference.child(ref)
//            .downloadUrl
//            .addOnSuccessListener {
//                viewModel._userImage.value = it.toString()
//                Log.e("URL", it.toString())
//            }
//    }
}
