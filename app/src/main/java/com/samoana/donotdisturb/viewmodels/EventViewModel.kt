package com.samoana.donotdisturb.viewmodels

import END_TIME_REQUEST_CODE
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.samoana.donotdisturb.data.Event
import com.samoana.donotdisturb.data.EventRepository
import javax.inject.Inject
import com.samoana.donotdisturb.receivers.EventReceiver
import org.joda.time.DateTime
import java.lang.ref.WeakReference


class EventViewModel @Inject constructor(private val eventRepository: EventRepository) : ViewModel() {
    fun addEvent(event: Event, context: Context) {
        AddEventAsyncTask(eventRepository, context).execute(event)
    }

    fun deleteEvent(event: Event) {
        eventRepository.deleteEvent(event)
    }

    fun getAllEvents() = eventRepository.getAllEvents()

    private class AddEventAsyncTask(
        private val eventRepository: EventRepository,
        @SuppressLint("StaticFieldLeak") private val context: Context
    ) :
        AsyncTask<Event, Void, String>() {
        private var contextRef: WeakReference<Context> = WeakReference<Context>(context)

        override fun doInBackground(vararg events: Event): String {
            val event = events[0]
            val eventId = eventRepository.insertEvent(event)
            scheduleEvent(eventId.toInt(), DateTime(event.startDateTime), DateTime(event.endDateTime))
            return "Success"
        }

        fun scheduleEvent(eventId: Int, startDateTime: DateTime, endDateTime: DateTime) {
            val context = contextRef.get() ?: return
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    RTC_WAKEUP,
                    startDateTime.millis,
                    getPendingIntent(context, AudioManager.RINGER_MODE_VIBRATE, eventId)
                )
                alarmManager.setExactAndAllowWhileIdle(
                    RTC_WAKEUP,
                    endDateTime.millis,
                    getPendingIntent(context, AudioManager.RINGER_MODE_NORMAL, END_TIME_REQUEST_CODE + eventId)
                )

            } else {
                alarmManager.setExact(
                    RTC_WAKEUP,
                    startDateTime.millis,
                    getPendingIntent(context, AudioManager.RINGER_MODE_VIBRATE, eventId)
                )
                alarmManager.setExact(
                    RTC_WAKEUP,
                    endDateTime.millis,
                    getPendingIntent(context, AudioManager.RINGER_MODE_NORMAL, END_TIME_REQUEST_CODE + eventId)
                )
            }
        }

        private fun getPendingIntent(context: Context, mode: Int, requestCode: Int): PendingIntent {
            val intent = Intent(context, EventReceiver::class.java)
            intent.putExtra("mode", mode)
            return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }


        override fun onPostExecute(result: String) {
            Log.i("", "Insertion Success")
        }
    }
}