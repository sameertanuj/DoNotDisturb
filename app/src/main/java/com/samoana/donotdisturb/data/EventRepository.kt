package com.samoana.donotdisturb.data

import android.os.AsyncTask
import android.util.Log
import javax.inject.Inject

class EventRepository @Inject constructor(private val eventDAO: EventDAO) {


    fun getAllEvents() = eventDAO.getAllEvents()

    fun insertEvent(event: Event) = eventDAO.insert(event)

    fun deleteEvent(event: Event) {
        DeleteAsyncTask(eventDAO).execute(event)
    }

    private class DeleteAsyncTask (private val eventDAO: EventDAO) : AsyncTask<Event, Void, String>() {
        override fun doInBackground(vararg params: Event): String {
            eventDAO.delete(params[0])
            return "Success"
        }

        override fun onPostExecute(result: String) {
            Log.i("", "Deletion Success")
        }
    }
}