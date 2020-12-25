package com.tron.familytree.profile.episode.episode_dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tron.familytree.R
import com.tron.familytree.databinding.DialogEpisodeDetailBinding
import com.tron.familytree.databinding.FragmentEditEpisodeBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.profile.editepisode.EditEpisodeDialogArgs
import com.tron.familytree.profile.editepisode.EditEpisodeViewModel


class EpisodeDetailDialog : DialogFragment() {

    private val viewModel by viewModels<EpisodeDetailViewModel> { getVmFactory(
        EpisodeDetailDialogArgs.fromBundle(requireArguments()).episodeProperties)
    }

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

        val binding = DialogEpisodeDetailBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.selectedProperty.observe(viewLifecycleOwner, Observer {
            viewModel.findUserById(it.userId)
        })

        binding.imageCancel.setOnClickListener {
            findNavController().navigateUp()
        }


        // Inflate the layout for this fragment
        return binding.root
    }
}