package com.tron.familytree.branch.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tron.familytree.databinding.DialogBranchUserDetailBinding
import com.tron.familytree.ext.getVmFactory

class BranchUserDetailDialog : BottomSheetDialogFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View).setBackgroundColor(android.graphics.Color.TRANSPARENT)
    }

    private val viewModel by viewModels<BranchUserDetailDialogViewModel> {  getVmFactory(
        BranchUserDetailDialogArgs.fromBundle(
            requireArguments()
        ).userProperties)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DialogBranchUserDetailBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this

        val user = BranchUserDetailDialogArgs.fromBundle(
            requireArguments()
        ).userProperties
        Log.e(
            "userProperties",
            "BranchUserDetailDialogArgs.fromBundle(requireArguments()).userProperties = $user"
        )

        binding.viewModel = viewModel



        return binding.root
    }

}