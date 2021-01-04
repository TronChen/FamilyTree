package com.tron.familytree.instruction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tron.familytree.MainActivity
import com.tron.familytree.R
import com.tron.familytree.branch.add_people_dialog.AddPeopleDialogArgs
import com.tron.familytree.branch.add_people_dialog.AddPeopleViewModel
import com.tron.familytree.databinding.DialogAddPeopleBinding
import com.tron.familytree.databinding.DialogInstructionBinding
import com.tron.familytree.ext.getVmFactory

class InstructionDialog : DialogFragment() {

    private val viewModel by viewModels<InstructionViewModel> {  getVmFactory()}

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

        val binding = DialogInstructionBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.conConfirm.setOnClickListener {
            findNavController().navigate(InstructionDialogDirections.actionGlobalEditUserFragment(viewModel.user.value!!))
        }


        // Inflate the layout for this fragment
        return binding.root
    }
}