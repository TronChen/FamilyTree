package com.tron.familytree.profile.edituser

import android.app.DatePickerDialog
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
import com.tron.familytree.data.Episode
import com.tron.familytree.databinding.FragmentEditUserBinding
import com.tron.familytree.ext.getVmFactory
import java.util.*

const val EDIT_USER = 222

class EditUserFragment : Fragment() {

    private val viewModel by viewModels<EditUserViewModel> { getVmFactory(
        EditUserFragmentArgs.fromBundle(
        requireArguments()
    ).userProperties)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentEditUserBinding.inflate(inflater, container, false)

        val activity = activity as MainActivity

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = EditEpisodeAdapter(
            EditEpisodeAdapter.EditEpisodeOnItemClickListener {
                Log.e("EditEpisodeClick", it.toString())
                findNavController().navigate(EditUserFragmentDirections.actionGlobalEditEpisodeDialog(it))
            })

        binding.recyclerEditEpisode.adapter = adapter


        binding.conAddEpisode.setOnClickListener {
            findNavController().navigate(EditUserFragmentDirections.actionGlobalEditEpisodeDialog(
                Episode(title = "", time = "請選擇時間", location = "", content = "")))
        }

        binding.conBirth.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(requireContext(),{_, year, month, day ->
                viewModel.userBirth.value ="${setDateFormat(year, month, day)}"
            },year,month,day).show()
        }

        binding.conDeath.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(requireContext(),{_, year, month, day ->
                viewModel.userDeath.value ="${setDateFormat(year, month, day)}"
            },year,month,day).show()
        }

        binding.radioGender.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.radioMale -> viewModel.userGender = "male"
                R.id.radioFemale -> viewModel.userGender = "female"
            }
        }

        binding.conComplete.setOnClickListener {
            viewModel.updateMember(viewModel.setUser())
            Log.e("Tron", viewModel.setUser().toString())
        }

        binding.conImage.setOnClickListener {
            getActivity()
                ImagePicker.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(
                        1080,
                        1080
                    ) //Final image resolution will be less than 1080 x 1080(Optional)
                    .start(EDIT_USER)
        }

        binding.conFamily.setOnClickListener {
            findNavController().navigate(R.id.action_global_editFamilyFragment)
        }

        viewModel.selectedProperty.observe(viewLifecycleOwner, Observer {
            Log.e("User", it.toString())
        })

        activity.viewModel.editUserImgPath.observe(viewLifecycleOwner, Observer {
            viewModel.userImage.value = it
            Log.e("filePath", it)
            viewModel.uploadImage(it)
        })


        viewModel.liveEpisodes.observe(viewLifecycleOwner, Observer {
            Log.e("Episodes", it.toString())
            adapter.submitList(it)
        })

        return binding.root
    }

    private fun setDateFormat(year: Int, month: Int, day: Int): String {
        return "$year-${month + 1}-$day"
    }
}

