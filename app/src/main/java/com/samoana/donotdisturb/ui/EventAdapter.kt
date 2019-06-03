package com.samoana.donotdisturb.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.samoana.donotdisturb.R
import com.samoana.donotdisturb.data.Event
import com.samoana.donotdisturb.utils.EventsDiffUtil
import kotlinx.android.synthetic.main.event_item.view.*

class EventAdapter : RecyclerView.Adapter<EventAdapter.EventItemViewHolder>() {

    private var events : List<Event> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventItemViewHolder {
        val eventItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_item, parent, false) as View
        return EventItemViewHolder(eventItem)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: EventItemViewHolder, position: Int) {
        holder.title.text = events[position].title
        holder.item.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("event", events[position])
            Navigation.findNavController(it).navigate(R.id.action_viewEventsFragment2_to_addEventFragment2, bundle)
        }
    }

    fun updateEvents(events: List<Event>) {
        val diffResult = DiffUtil.calculateDiff(EventsDiffUtil(this.events, events), true)
        this.events = events
        diffResult.dispatchUpdatesTo(this)
    }

    class EventItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.event_item_title!!
        val item = view.event_item!!
    }

}