package com.tron.familytree.branch.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tron.familytree.data.Episode
import com.tron.familytree.databinding.DialogBranchUserDetailBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.profile.episode.EpisodeAdapter

class BranchUserDetailDialog : BottomSheetDialogFragment() {

    private val viewModel by viewModels<BranchUserDetailDialogViewModel> {  getVmFactory(
        BranchUserDetailDialogArgs.fromBundle(
            requireArguments()
        ).userProperties)}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View).setBackgroundColor(android.graphics.Color.TRANSPARENT)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DialogBranchUserDetailBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this

//        val user = BranchUserDetailDialogArgs.fromBundle(
//            requireArguments()
//        ).userProperties
//        Log.e(
//            "userProperties",
//            "BranchUserDetailDialogArgs.fromBundle(requireArguments()).userProperties = $user"
//        )

        binding.viewModel = viewModel

        val adapter = EpisodeAdapter(EpisodeAdapter.EpisodeOnItemClickListener {
            Log.e("EventCLick", it.toString())
        })

        binding.recyclerEpisode.adapter = adapter



        viewModel.liveEpisodes.observe(viewLifecycleOwner, Observer {
            Log.e("UserEpisode", it.toString())
            adapter.submitList(it)
        })

        binding.imageChat.setOnClickListener {
            findNavController().navigate(BranchUserDetailDialogDirections.actionGlobalMessageFragment(viewModel.selectedProperty.value!!))
        }



        return binding.root
    }

    override fun onPause() {
        super.onPause()
        Log.e("onPause","onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e("onStop","onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("onDestroy","onDestroy")
    }
}