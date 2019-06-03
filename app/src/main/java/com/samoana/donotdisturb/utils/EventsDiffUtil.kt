package com.samoana.donotdisturb.utils

import androidx.recyclerview.widget.DiffUtil
import com.samoana.donotdisturb.data.Event

class EventsDiffUtil(private val oldEvents: List<Event>, private val newEvents: List<Event>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldEvents[oldItemPosition] == newEvents[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldEvents.size
    }

    override fun getNewListSize(): Int {
        return newEvents.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldEvents[oldItemPosition] == newEvents[newItemPosition]
    }

}