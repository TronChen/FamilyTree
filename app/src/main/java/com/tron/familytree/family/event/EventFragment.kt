package com.tron.familytree.family.event

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tron.familytree.R
import com.tron.familytree.data.Event
import com.tron.familytree.databinding.FragmentEventBinding
import com.tron.familytree.ext.getVmFactory
import java.util.*


class EventFragment(val position: Int) : Fragment() {

    private val viewModel by viewModels<EventViewModel> { getVmFactory() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEventBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

//        binding.imageView10.setOnClickListener {
//            findNavController().navigate(R.id.action_global_eventDialog)
//        }

        val adapter = EventAdapter(EventAdapter.EventOnItemClickListener {
            Log.e("EventCLick", it.toString())
            findNavController().navigate(EventFragmentDirections.actionGlobalEventDialog(it))
        })
        binding.recyclerEvent.adapter = adapter

        binding.menuEvent.setOnClickListener {
            findNavController().navigate(R.id.action_global_createEventDialog)
        }

        viewModel.liveEvent.observe(viewLifecycleOwner, Observer {
            it?.let {
                val afterEvent = mutableListOf<Event>()
                it.forEach { event ->
                    event.eventTime
                    val currentTime = Calendar.getInstance().timeInMillis
                    if ( currentTime < event.eventTime!! ) {
                        //即將到來的活動
                        afterEvent.add(event)
                    }
                }
                adapter.submitList(afterEvent)
            }
        })

//        binding.imageView8.setImageResource(R.drawable.sport)



        return binding.root
    }
}