package com.tron.familytree.family.calendar

import android.app.Activity
import android.content.Context
import android.graphics.Color.red
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.util.TimeUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.datepicker.SingleDateSelector
import com.prolificinteractive.materialcalendarview.*
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import com.tron.familytree.R
import com.tron.familytree.databinding.FragmentCalendarBinding
import com.tron.familytree.databinding.FragmentEventBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.family.event.EventViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class CalendarFragment : Fragment() {

    private val viewModel by viewModels<CalendarViewModel> { getVmFactory() }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var widget: MaterialCalendarView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCalendarBinding.inflate(inflater, container, false)

        val localDate = LocalDate.now()

        widget = binding.materialCalendarView
//        widget.setCurrentDate(localDate)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        val adapter = CalendarAdapter(CalendarAdapter.CalendarOnItemClickListener{
            viewModel._selectedEvent.value = it
        })

        binding.bottomCalendar.recEvent.adapter = adapter

        viewModel._selectedEvent.observe(viewLifecycleOwner, Observer {
            val year = SimpleDateFormat("yyyy").format(it.eventTime).toInt()
            val month   = SimpleDateFormat("MM").format(it.eventTime).toInt()
            val day   = SimpleDateFormat("dd").format(it.eventTime).toInt()
        })

        // Get the current selected date
        widget.setOnDateChangedListener { _, date, selected ->
            if (selected) {

//                oneDayDecorator.setDate(date.date)

                // Create a sorted list of event based on the current date
                val selectedDate =  SimpleDateFormat("yyyy-MM-dd").parse(date.date.toString()).time
                viewModel.createDailyEvent(selectedDate)

                Log.e("Tron","$selectedDate")
                Log.e("Tron","${date.date}")



            }

        }




        viewModel.liveUserEvent.observe(viewLifecycleOwner, Observer { it ->
            it.forEach {
                val year = SimpleDateFormat("yyyy").format(it.eventTime).toInt()
                val month   = SimpleDateFormat("MM").format(it.eventTime).toInt()
                val day   = SimpleDateFormat("dd").format(it.eventTime).toInt()
            widget.addDecorators(
            SingleDateDecorator(requireContext().resources.getColor(R.color.deep_blue),
                CalendarDay.from(year, month, day))
        )
            }
                adapter.submitList(it)
        })






        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomCalendar.conBottomSheet)

        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
            }

            override fun onStateChanged(bottomSheet: View, state: Int) {
                print(state)
                when (state) {
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.materialCalendarView.state().edit()
                            .setCalendarDisplayMode(CalendarMode.MONTHS)
                            .commit()
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.materialCalendarView.state().edit()
                            .setCalendarDisplayMode(CalendarMode.WEEKS)
                            .commit()
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }
        })

        return binding.root
    }



}


class SingleDateDecorator(private val color: Int, date: CalendarDay) :
    DayViewDecorator {
    var theDay = date
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == theDay
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan(10F, color))
    }

    init {

    }
}
