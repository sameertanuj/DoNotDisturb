package com.samoana.donotdisturb.ui


import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.samoana.donotdisturb.R
import com.samoana.donotdisturb.viewmodels.EventViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_view_events.*
import javax.inject.Inject

class ViewEventsFragment : DaggerFragment(), View.OnClickListener {

    @Inject
    lateinit var eventAdapter: EventAdapter
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var eventViewModel: EventViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_events, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        eventViewModel = ViewModelProviders.of(this, viewModelFactory).get(EventViewModel::class.java)
        events.layoutManager = LinearLayoutManager(activity)
        events.adapter = eventAdapter
        updateEvents()
        add_event.setOnClickListener(this)
    }

    private fun updateEvents() {
        eventViewModel.getAllEvents().observe(this , Observer {
            if(it.isEmpty()) {
                no_events.visibility = View.VISIBLE
                events.visibility = View.GONE
            } else {
                eventAdapter.updateEvents(it)
                no_events.visibility = View.GONE
                events.visibility = View.VISIBLE
            }
        })
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.add_event -> view.findNavController().navigate(R.id.action_viewEventsFragment2_to_addEventFragment2)
        }
    }

}
