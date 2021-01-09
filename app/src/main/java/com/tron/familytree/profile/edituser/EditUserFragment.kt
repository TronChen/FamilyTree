package com.tron.familytree.profile.edituser

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tron.familytree.MainActivity
import com.tron.familytree.NavigationDirections
import com.tron.familytree.R
import com.tron.familytree.check.CheckDialog
import com.tron.familytree.data.Episode
import com.tron.familytree.databinding.FragmentEditUserBinding
import com.tron.familytree.ext.getVmFactory
import java.util.*

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
            if (
                viewModel.userName.value != null && viewModel.userName.value != "" &&
            viewModel.userBirth.value != null && viewModel.userBirth.value != "" &&
            viewModel.selectedProperty.value!!.id != "" &&
            viewModel.userDeath.value != null && viewModel.userDeath.value != "" &&
            viewModel.userBirthLocation.value != null && viewModel.userBirthLocation.value != "" &&
            viewModel.userGender != ""
            ){
                viewModel.updateMember(viewModel.setUser())
                findNavController().navigate(
                    NavigationDirections.actionGlobalCheckDialog(
                        CheckDialog.MessageType.ADDED_SUCCESS))
                Log.e("Tron", viewModel.setUser().toString())
            }
            if (viewModel.selectedProperty.value?.familyId == null){
                Toast.makeText(requireContext(),"請選擇家族", Toast.LENGTH_SHORT).show()
            }
            if (
                viewModel.userName.value == "" ||
                viewModel.userBirth.value == "" ||
                viewModel.selectedProperty.value!!.id == "" ||
                viewModel.userDeath.value == "" ||
                viewModel.userBirthLocation.value == "" ||
                viewModel.userGender == ""
            ){
                Toast.makeText(requireContext(),"請輸入完整訊息", Toast.LENGTH_SHORT).show()
            }
        }

        binding.conFamily.setOnClickListener {
            findNavController().navigate(R.id.action_global_editFamilyFragment)
        }

        viewModel.selectedProperty.observe(viewLifecycleOwner, Observer {
            Log.e("User", it.toString())
            if (it.familyId != null){
                viewModel.updateMember(it.familyId!!)
            }
            when(it.gender){
                "male" -> (binding.radioGender.getChildAt(0) as RadioButton).isChecked = true
                "female" -> (binding.radioGender.getChildAt(1) as RadioButton).isChecked = true
            }



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

