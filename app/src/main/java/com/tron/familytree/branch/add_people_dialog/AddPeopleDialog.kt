package com.tron.familytree.branch.add_people_dialog

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.tron.familytree.MainActivity
import com.tron.familytree.NavigationDirections
import com.tron.familytree.R
import com.tron.familytree.check.CheckDialog
import com.tron.familytree.databinding.DialogAddPeopleBinding
import com.tron.familytree.ext.getVmFactory
import java.util.*

const val ADD_USER = 111

class AddPeopleDialog : DialogFragment() {

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
                .start(ADD_USER)
        }

        binding.conScan.setOnClickListener {
            findNavController().navigate(AddPeopleDialogDirections.actionGlobalQrCodeReaderFragment(viewModel.selectedProperty.value!!))
        }

//        binding.conSelectMember.setOnClickListener {
//            findNavController().navigate(AddPeopleDialogDirections.actionGlobalSelectMemberFragment(viewModel.selectedProperty.value!!))
//        }


        viewModel.userEditName = binding.editName.text.toString()
        viewModel.userBirthLocation = binding.editBirthLocation.text.toString()

        binding.conConfirm.setOnClickListener {

            if (
            viewModel.userEditName != "" &&
            viewModel.birthDate != "" &&
            viewModel.gender != "" &&
            viewModel.userBirthLocation != ""
                    ) {

                //判斷 加入的類別
                when (viewModel.selectedProperty.value?.name) {

                    "No mate" -> {
                        viewModel.addMateReturnUser(viewModel.setMate())
                    }
                    "No father" -> {
                        viewModel.addFatherReturnUser(viewModel.setParent())
                    }
                    "No mother" -> {
                        viewModel.addMotherReturnUser(viewModel.setParent())
                    }
                    "No mateFather" -> {
                        viewModel.addFatherReturnUser(viewModel.setParent())
                    }
                    "No mateMother" -> {
                        viewModel.addMotherReturnUser(viewModel.setParent())
                    }
                    "No child" -> {
                        viewModel.addMember(viewModel.setChild())
                        Log.e("setChild()",viewModel.setChild().toString())
                        findNavController().navigate(NavigationDirections.actionGlobalCheckDialog(CheckDialog.MessageType.ADDED_SUCCESS))
                        findNavController().navigate(R.id.action_global_branchFragment)
                        imagePathCompleted()
                    }
                }
            }else{
                Toast.makeText(requireContext(),"請輸入完整訊息",Toast.LENGTH_SHORT).show()
            }

        }

        binding.imageCancel.setOnClickListener {
            findNavController().navigateUp()
            imagePathCompleted()
        }


        //得到選擇圖片的路徑upload到fire storage
        activity.viewModel.addUserImgPath.observe(viewLifecycleOwner, Observer {
            if(it != "") {
                viewModel._updateImg.value = it
                Log.e("filePath", it)
                viewModel.uploadImage(viewModel._updateImg.value.toString())
            }
        })

        viewModel._userImage.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.e("URLLL", it)
            }
        })

        viewModel.newFather.observe(viewLifecycleOwner, Observer {
            viewModel.updateMemberFatherId(viewModel.addUser.value!!, it)
            findNavController().navigate(NavigationDirections.actionGlobalCheckDialog(CheckDialog.MessageType.ADDED_SUCCESS))
            findNavController().navigate(R.id.action_global_branchFragment)
            imagePathCompleted()
        })

        viewModel.newMother.observe(viewLifecycleOwner, Observer {
            viewModel.updateMemberMotherId(viewModel.addUser.value!!, it)
            findNavController().navigate(NavigationDirections.actionGlobalCheckDialog(CheckDialog.MessageType.ADDED_SUCCESS))
            findNavController().navigate(R.id.action_global_branchFragment)
            imagePathCompleted()
        })

        viewModel.newMate.observe(viewLifecycleOwner, Observer {
            viewModel.updateMemberMateId(viewModel.addUser.value!!, it)
            findNavController().navigate(NavigationDirections.actionGlobalCheckDialog(CheckDialog.MessageType.ADDED_SUCCESS))
            findNavController().navigate(R.id.action_global_branchFragment)
            imagePathCompleted()
        })

        viewModel.addUser.observe(viewLifecycleOwner, Observer {
            Log.e("AddPeopleDialogUser", it.toString())
            if (viewModel.selectedProperty.value?.name == "No mate"){
                if (it.gender == "male"){
                    (binding.radioGender.getChildAt(1) as RadioButton).isChecked = true
                    binding.radioMale.visibility = View.GONE
                }
                if (it.gender == "female"){
                    (binding.radioGender.getChildAt(0) as RadioButton).isChecked = true
                    binding.radioFemale.visibility = View.GONE
                }
            }
        })

        viewModel.selectedProperty.observe(viewLifecycleOwner, Observer {
            Log.e("AddPeopleDialogUser", it.toString())
            when(it.name){
                "No father", "No mateFather" -> {
                    (binding.radioGender.getChildAt(0) as RadioButton).isChecked = true
                    binding.radioFemale.visibility = View.GONE
                }
                "No mother", "No mateMother" -> {
                    (binding.radioGender.getChildAt(1) as RadioButton).isChecked = true
                    binding.radioMale.visibility = View.GONE
                }
            }
        })

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        viewModel._updateImg.value = ""
    }

    private fun setDateFormat(year: Int, month: Int, day: Int): String {
        return "$year/${month + 1}/$day"
    }

    private fun imagePathCompleted(){
        val activity = activity as MainActivity
        activity.viewModel.addUserImgPath.value = ""
    }
}
