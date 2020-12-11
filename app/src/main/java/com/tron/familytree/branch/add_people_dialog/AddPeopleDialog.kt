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

        activity.imgPath.observe(viewLifecycleOwner, Observer {
            viewModel._updateImg.value = it
            Log.e("filePath", it)

            val file = Uri.fromFile(File(it))
            file.lastPathSegment?.let { it1 -> Log.e("fileName", it1) }
            val mStorageRef = FirebaseStorage.getInstance().reference
        val metadata = StorageMetadata.Builder()
            .setContentDisposition("universe")
            .setContentType("image/jpg")
            .build()
        val riversRef = mStorageRef?.child(file.lastPathSegment ?: "")
        val uploadTask = riversRef?.putFile(file, metadata)
        uploadTask?.addOnFailureListener { exception ->
//            upload_info_text.text = exception.message
        }?.addOnSuccessListener {
//            upload_info_text.setText(R.string.upload_success)
        }?.addOnProgressListener { taskSnapshot ->
            val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
//            upload_progress.progress = progress
            if (progress >= 100) {
//                upload_progress.visibility = View.GONE
            }
        }

        })

            FirebaseStorage.getInstance().reference.child("IMG_20201211_002556209.jpg")
                .downloadUrl
                .addOnSuccessListener {
                    Log.e("URL", it.toString())
                }

        fun downloadImg(ref: StorageReference?) {
            ref?.downloadUrl?.addOnSuccessListener {
                Log.e("downloadUrl", it.toString())
            }
        }


//        downloadImg(FirebaseStorage.getInstance().reference)



            binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel



        viewModel.selectedProperty.observe(viewLifecycleOwner, Observer {
            Log.e("AddMember", it.toString())
        })




        binding.conBirth.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(requireContext(),{_, year, month, dayOfMonth
                -> binding.textDate.text = "${setDateFormat(year,month,dayOfMonth)}"
            },year,month,dayOfMonth).show()
        }

        binding.conDeath.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(requireContext(),{_, year, month, dayOfMonth
                -> binding.textDeath.text = "${setDateFormat(year,month,dayOfMonth)}"
            },year,month,dayOfMonth).show()
        }

        binding.textSelectPic.setOnClickListener {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }









        return binding.root
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (resultCode) {
//            Activity.RESULT_OK -> {
//                val filePath: String = ImagePicker.getFilePath(data) ?: ""
//                if (filePath.isNotEmpty()) {
//                   val  imgPath = filePath
//                    Toast.makeText(requireContext(), imgPath, Toast.LENGTH_LONG).show()
//                    mImageview?.let { Glide.with(requireContext()).load(filePath).into(it.value!!)
//                        Log.e("AddPic", mImageview.toString())
//                    }
//                } else {
////                    Toast.makeText(requireContext(), R.string.load_img_fail, Toast.LENGTH_SHORT).show()
//                }
//            }
//            ImagePicker.RESULT_ERROR -> Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
//            else -> Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun setDateFormat(year: Int, month: Int, day: Int): String {
        return "$year/${month + 1}/$day"
    }
}
