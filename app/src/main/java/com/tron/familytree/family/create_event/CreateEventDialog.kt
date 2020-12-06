package com.tron.familytree.family.create_event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.tron.familytree.R
import com.tron.familytree.databinding.DialogCreateEventBinding
import com.tron.familytree.databinding.FragmentEditUserBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.profile.edituser.EditUserViewModel
import java.util.*

class CreateEventDialog : DialogFragment() {

    private val viewModel by viewModels<CreateEventViewModel> { getVmFactory() }



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

        val binding = DialogCreateEventBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.conTIme.setOnClickListener {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        TimePickerDialog(requireContext(), {
                _, hour, minute->  binding.textSelectedTime.text = "$hour:$minute"
        }, hour, minute, true).show()
        }

        binding.conDate.setOnClickListener {
            val builder = datePicker()
            val picker = builder.build()
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(requireContext(),{_, year, month, dayOfMonth -> binding.textDate.text = "$year/$month/$dayOfMonth"
            },year,month,dayOfMonth).show()
            picker.addOnPositiveButtonClickListener { Log.e("DatePicker","$it}")}
            picker.addOnNegativeButtonClickListener { dismiss() }
        }



        return binding.root
    }

}