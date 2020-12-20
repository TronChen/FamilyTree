package com.tron.familytree.family.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tron.familytree.R
import com.tron.familytree.data.Event
import com.tron.familytree.databinding.FragmentAlbumBinding
import com.tron.familytree.ext.getVmFactory
import java.util.*


class AlbumFragment(val position : Int) : Fragment() {

    private val viewModel by viewModels<AlbumViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentAlbumBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter =  AlbumAdapter(AlbumAdapter.AlbumOnItemClickListener{
            findNavController().navigate(AlbumFragmentDirections.actionGlobalAlbumDetailFragment(it))
        })
        binding.recComAlbum.adapter = adapter


        viewModel.liveEvent.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                    val beforeEvent = mutableListOf<Event>()
                it.forEach { event ->
                    event.eventTime
                    val currentTime = Calendar.getInstance().timeInMillis
                    if (currentTime > event.eventTime!!){
                        //以結束的活動
                        beforeEvent.add(event)
                    }
                }
                        adapter.submitList(beforeEvent)
            }
        })



        return binding.root
    }
}