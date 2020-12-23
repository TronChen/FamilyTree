package com.tron.familytree.family.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tron.familytree.R
import com.tron.familytree.data.Event
import com.tron.familytree.databinding.ItemListCalendarEventBinding
import com.tron.familytree.databinding.ItemListEventBinding
import com.tron.familytree.family.create_event.EventType



    class CalendarAdapter(private val itemClickListener: CalendarOnItemClickListener)
        : ListAdapter<Event, RecyclerView.ViewHolder>(CalendarDiffCallback()) {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return CalendarViewHolder.from(parent)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = getItem(position)
            when (holder) {
                is CalendarViewHolder ->{
                    val event = item as Event
                    holder.bind(event)
                }
            }

        }

        class CalendarDiffCallback : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem == newItem
            }
        }

        class CalendarViewHolder(val binding: ItemListCalendarEventBinding): RecyclerView.ViewHolder(binding.root) {
            fun bind(event: Event) {
                binding.event = event
                // This is important, because it forces the data binding to execute immediately,
                // which allows the RecyclerView to make the correct view size measurements
                binding.executePendingBindings()
            }
            companion object {
                fun from(parent: ViewGroup): CalendarViewHolder {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val binding = ItemListCalendarEventBinding.inflate(layoutInflater, parent, false)
                    return CalendarViewHolder(binding)
                }
            }
        }

        class CalendarOnItemClickListener(val clickListener: (event: Event) -> Unit ){
            fun onItemClicked(event: Event) = clickListener(event)
        }
    }
