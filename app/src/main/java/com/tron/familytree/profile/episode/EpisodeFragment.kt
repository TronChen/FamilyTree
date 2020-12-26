package com.tron.familytree.profile.episode

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.tron.familytree.data.Episode
import com.tron.familytree.databinding.FragmentEpisodeBinding
import com.tron.familytree.ext.getVmFactory


class EpisodeFragment() : Fragment() {
    var type: Int = 0

    constructor(int : Int) : this() {
        this.type = int
    }


    private val viewModel by viewModels<EpisodeViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentEpisodeBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = EpisodeAdapter(EpisodeAdapter.EpisodeOnItemClickListener {
            Log.e("EventCLick", it.toString())
        })
        binding.recyclerEpisode.adapter = adapter

        viewModel.liveEpisodes.observe(viewLifecycleOwner, Observer {
            Log.e("Episodes", it.toString())
        adapter.submitList(it)
        })

        binding.layoutSwipeRefreshHome.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.refreshStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                binding.layoutSwipeRefreshHome.isRefreshing = it
            }
        })

        return binding.root
    }
}