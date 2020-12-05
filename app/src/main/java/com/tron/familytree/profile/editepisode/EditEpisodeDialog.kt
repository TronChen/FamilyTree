package com.tron.familytree.profile.editepisode

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tron.familytree.R
import com.tron.familytree.databinding.FragmentEditEpisodeBinding
import com.tron.familytree.databinding.FragmentEditUserBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.profile.edituser.EditUserViewModel

class EditEpisodeDialog : DialogFragment() {

    private val viewModel by viewModels<EditEpisodeViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setStyle(STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.rounded_white_radius65)

        val binding = FragmentEditEpisodeBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.editTitle.observe(viewLifecycleOwner, Observer {
            Log.e("EditTitle",it)
        })


        return binding.root
    }
    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}