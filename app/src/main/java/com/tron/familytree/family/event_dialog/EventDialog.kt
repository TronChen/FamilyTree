package com.tron.familytree.family.event_dialog

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tron.familytree.R
import com.tron.familytree.branch.add_people_dialog.AddPeopleDialogArgs
import com.tron.familytree.branch.add_people_dialog.AddPeopleViewModel
import com.tron.familytree.databinding.DialogCreateEventBinding
import com.tron.familytree.databinding.DialogEventBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.family.create_event.EventType
import com.tron.familytree.util.UserManager


class EventDialog : DialogFragment() {

    private val viewModel by viewModels<EventDialogViewModel> {  getVmFactory(
        EventDialogArgs.fromBundle(
            requireArguments()
        ).eventProperties)}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View).setBackgroundColor(android.graphics.Color.TRANSPARENT)
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

        val binding = DialogEventBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = EventDialogAdapter(EventDialogAdapter.EventDialogOnItemClickListener{})
        binding.recyclerAttender.adapter = adapter

        viewModel.selectedProperty.observe(viewLifecycleOwner, Observer {
            Log.e("Event", it.toString())
            when(it.eventType){
                    EventType.MEAL -> binding.imageEventType.setImageResource(R.drawable.meal)
                    EventType.CASUAL -> binding.imageEventType.setImageResource(R.drawable.casual)
                    EventType.SPORT -> binding.imageEventType.setImageResource(R.drawable.sport)
                    EventType.BIG_THING -> binding.imageEventType.setImageResource(R.drawable.big_thing)
            }

            if (it.publisherId == UserManager.email){
                binding.conSingleMessage.visibility = View.GONE
                binding.conConfirm.visibility = View.GONE
            }
        })

        binding.conConfirm.setOnClickListener {
            viewModel.addEventAttender(viewModel.user.value!!,viewModel.selectedProperty.value!!)
        }

        viewModel.liveAttender.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        binding.conSingleMessage.setOnClickListener {
            findNavController().navigate(EventDialogDirections.actionGlobalMessageFragment(viewModel.publisher.value))
        }







        return binding.root
    }
}