package com.tron.familytree.family.create_album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.tron.familytree.R
import com.tron.familytree.databinding.DialogCreateAlbumBinding
import com.tron.familytree.databinding.DialogCreateEventBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.family.create_event.CreateEventViewModel

class CreateAlbumDialog : DialogFragment() {

    private val viewModel by viewModels<CreateAlbumViewModel> { getVmFactory() }

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

        val binding = DialogCreateAlbumBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel





        return binding.root
    }
}