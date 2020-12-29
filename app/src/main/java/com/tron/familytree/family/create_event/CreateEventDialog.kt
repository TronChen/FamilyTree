package com.tron.familytree.family.create_event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.tron.familytree.NavigationDirections
import com.tron.familytree.R
import com.tron.familytree.check.CheckDialog
import com.tron.familytree.data.Event
import com.tron.familytree.databinding.DialogCreateEventBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.util.UserManager
import java.text.SimpleDateFormat
import java.util.*

class CreateEventDialog : DialogFragment() {

    private val viewModel by viewModels<CreateEventViewModel> { getVmFactory() }

    val time = Calendar.getInstance()
    val eventTime = MutableLiveData<Long>()

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
                _, hour, minute->
            viewModel.editTime.value = "$hour:$minute"
        }, hour, minute, true).show()
        }


        binding.conDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val yearr = calendar.get(Calendar.YEAR)
            val monthh = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(requireContext(),{_, year, month, day ->
                viewModel.editDate.value ="${setDateFormat(year, month, day)}"
                viewModel.eventMonth.value = "${setMonthFormat(year, month, day)}"
                Log.e("DatePicker","$year,$month,$day}")
                time.set(year,month,day)
                eventTime.value = time.timeInMillis

                Log.e("time", SimpleDateFormat("yyyy.MM.dd").format(1609051617782).toString())
            },yearr,monthh,dayOfMonth).show()
            DatePickerDialog.OnDateSetListener { _, _, _, _ ->
            }

        }

        val spinner = binding.spinEventType
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    0 -> viewModel.eventType.value = EventType.MEAL
                    1 -> viewModel.eventType.value = EventType.CASUAL
                    2 -> viewModel.eventType.value = EventType.SPORT
                    3 -> viewModel.eventType.value = EventType.BIG_THING
                }
            }
        }

        binding.conConfirm.setOnClickListener {

            if (viewModel.editTitle.value != null &&
                viewModel.editTime.value != "選擇時間" &&
                viewModel.editDate.value != "選擇日期" &&
                viewModel.editContent.value != null &&
                viewModel.editLocation.value != null    ){
                if (viewModel.eventType.value != EventType.BIG_THING ||
                    viewModel.eventType.value != EventType.SPORT ||
                    viewModel.eventType.value != EventType.MEAL ||
                    viewModel.eventType.value != EventType.CASUAL){

                    viewModel.addEvent(setEvent())
                    findNavController().navigate(
                        NavigationDirections.actionGlobalCheckDialog(
                            CheckDialog.MessageType.ADDED_SUCCESS))
                    findNavController().navigate(R.id.action_global_familyFragment)
                    Log.e("Event", setEvent().toString())

                }
            } else{
                Toast.makeText(requireContext(),"請輸入完整訊息", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imageCancel.setOnClickListener {
            findNavController().navigateUp()
        }


        return binding.root
    }

    private fun setDateFormat(year: Int, month: Int, day: Int): String {
        return "$year-${month + 1}-$day"
    }
    private fun setMonthFormat(year: Int, month: Int, day: Int): String {
        return "$year-${month + 1}"
    }

    fun setEvent(): Event {
        return Event(
            publisher = UserManager.name.toString(),
            publisherId = UserManager.email.toString(),
            title = viewModel.editTitle.value!!,
            time = viewModel.editTime.value!!,
            date = viewModel.editDate.value!!,
            attender = emptyList(),
            content = viewModel.editContent.value!!,
            location = viewModel.editLocation.value!!,
            eventType = viewModel.eventType.value,
            eventTime = eventTime.value,
            eventMonth = viewModel.eventMonth.value!!
        )
    }


}