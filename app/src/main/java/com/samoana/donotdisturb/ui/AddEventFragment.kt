package com.samoana.donotdisturb.ui


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar

import com.samoana.donotdisturb.R
import com.samoana.donotdisturb.data.Event
import com.samoana.donotdisturb.utils.*
import com.samoana.donotdisturb.viewmodels.EventViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_add_event.*
import org.joda.time.DateTime
import javax.inject.Inject
import android.content.Intent
import android.app.NotificationManager
import android.content.Context


class AddEventFragment : DaggerFragment(), View.OnClickListener {


    private lateinit var startDateTime: DateTime
    private lateinit var endDateTime: DateTime
    private lateinit var eventViewModel: EventViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var eventID = 0

    private var isValid = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_event, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(arguments != null) {
          fillData()
        } else {
            setDateTime()
        }
        eventViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(EventViewModel::class.java)
        updateTexts()
        setOnClickListeners()
    }

    private fun fillData() {
        val event = arguments?.getSerializable("event") as Event
        eventID = event.id
        event_title.text.append(event.title)
        note.text.append(event.note)
        startDateTime = DateTime(event.startDateTime)
        endDateTime = DateTime(event.endDateTime)

    }

    override fun onResume() {
        super.onResume()
        checkForRequiredPermission()
    }

    private fun checkForRequiredPermission() {
        if( Build.VERSION.SDK_INT >= 23 ) {
            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (!notificationManager.isNotificationPolicyAccessGranted) {
                val intent = Intent(
                    android.provider.Settings
                        .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
                )
                startActivity(intent)
            }
        }
    }

    private fun validateStartDateTimeWithCurrentTime() {
        if(startDateTime.isBeforeNow) {
            isValid = false
            setStartDateTimeTextColor(ContextCompat.getColor(context!!, R.color.red))
        } else {
            isValid = true
            setStartDateTimeTextColor(ContextCompat.getColor(context!!, R.color.black))
        }
    }

    private fun validateEndDateTimeWithCurrentTime() {
        if(endDateTime.isBeforeNow) {
            isValid = false
            setEndDateTimeTextColor(ContextCompat.getColor(context!!, R.color.red))
        } else {
            isValid = true
            setEndDateTimeTextColor(ContextCompat.getColor(context!!, R.color.black))
        }

    }
    private fun setDateTime() {
        startDateTime = DateTime.now()
        endDateTime = DateTime.now()
        if(startDateTime.hourOfDay != 23) {
            endDateTime = endDateTime.withHourOfDay(startDateTime.hourOfDay + 1)
        }
    }

    private fun updateTexts() {
        updateStartTexts()
        updateEndTexts()
    }

    private fun updateStartTexts() {
        updateStartDateText()
        updateStartTimeText()

    }

    private fun updateEndTexts() {
        updateEndDateText()
        updateEndTimeText()
    }

    private fun updateStartDateText() {
        start_date.text = getDateString(startDateTime)
        validateStartDateTimeWithCurrentTime()
    }

    private fun updateStartTimeText() {
        start_time.text = getTimeString(startDateTime)
        validateStartDateTimeWithCurrentTime()
    }

    private fun updateEndDateText() {
        end_date.text = getDateString(endDateTime)
        checkStartEndValidity()
        validateStartDateTimeWithCurrentTime()
    }

    private fun updateEndTimeText() {
        end_time.text = getTimeString(endDateTime)
        checkStartEndValidity()
        validateEndDateTimeWithCurrentTime()
    }

    private fun checkStartEndValidity() {
        val textColor =
            if (startDateTime.isAfter(endDateTime)) {
                isValid = false
                ContextCompat.getColor(context!!, R.color.red)
            }
            else {
                isValid = true
                ContextCompat.getColor(context!!, R.color.black)
            }
        setEndDateTimeTextColor(textColor)
    }

    private fun setOnClickListeners() {
        start_date.setOnClickListener(this)
        start_time.setOnClickListener(this)
        end_date.setOnClickListener(this)
        end_time.setOnClickListener(this)
        save.setOnClickListener(this)
        close.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.start_date -> setUpStartDate()
            R.id.start_time -> setUpStartTime()
            R.id.end_date -> setUpEndDate()
            R.id.end_time -> setUpEndTime()
            R.id.save -> save(view)
            R.id.close -> close(view)
        }
    }

    private fun setUpStartDate() {
        val datePicker = DatePickerDialog(
            context!!, startDateSetListener, startDateTime.year, startDateTime.monthOfYear - 1,
            startDateTime.dayOfMonth
        )
        datePicker.show()
    }

    private fun setUpStartTime() {
        val datePicker = TimePickerDialog(
            context!!, startTimeSetListener, startDateTime.hourOfDay, startDateTime.minuteOfHour,
            false
        )
        datePicker.show()
    }

    private fun setUpEndDate() {
        val datePicker = DatePickerDialog(
            context!!, endDateSetListener, endDateTime.year, endDateTime.monthOfYear - 1,
            endDateTime.dayOfMonth
        )
        datePicker.show()
    }

    private fun setUpEndTime() {
        val datePicker = TimePickerDialog(
            context!!, endTimeSetListener, endDateTime.hourOfDay, endDateTime.minuteOfHour,
            false
        )
        datePicker.show()
    }

    private val startDateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        dateSet(year, monthOfYear, dayOfMonth, true)
    }

    private val startTimeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        timeSet(hourOfDay, minute, true)
    }

    private val endDateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        dateSet(
            year,
            monthOfYear,
            dayOfMonth,
            false
        )
    }

    private val endTimeSetListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute -> timeSet(hourOfDay, minute, false) }

    private fun dateSet(year: Int, month: Int, day: Int, isStart: Boolean) {
        if (isStart) {
            startDateTime = startDateTime.withDate(year, month + 1, day)
            updateStartDateText()
            endDateTime = endDateTime.withHourOfDay(startDateTime.hourOfDay)
            updateEndTexts()
        } else {
            endDateTime = endDateTime.withDate(year, month + 1, day)
            updateEndDateText()
        }
    }

    private fun timeSet(hours: Int, minutes: Int, isStart: Boolean) {
        if (isStart) {
//            val diff = mEventEndDateTime.seconds() - mEventStartDateTime.seconds()
            startDateTime = startDateTime.withHourOfDay(hours).withMinuteOfHour(minutes)
            updateStartTimeText()
            endDateTime = endDateTime.withHourOfDay(hours).withMinuteOfHour(minutes)
            updateEndTexts()
        } else {
            endDateTime = endDateTime.withHourOfDay(hours).withMinuteOfHour(minutes)
            updateEndTimeText()
        }
    }

    private fun setStartDateTimeTextColor(textColor: Int) {
        start_date.setTextColor(textColor)
        start_time.setTextColor(textColor)
    }

    private fun setEndDateTimeTextColor(textColor: Int) {
        end_date.setTextColor(textColor)
        end_time.setTextColor(textColor)
    }

    private fun save(view: View) {
        when {
            event_title.text.isEmpty() -> Snackbar.make(view, "Title cannot be empty", Snackbar.LENGTH_SHORT).show()
            !isValid -> Snackbar.make(view, "Please choose start time and end time correctly", Snackbar.LENGTH_SHORT).show()
            else -> {
                eventViewModel.addEvent(Event(eventID, event_title.text.toString(), startDateTime.toString(), endDateTime.toString(), note.text.toString()), context!!)
                close(view)
            }
        }
    }

    private fun close(view: View) {
        view.findNavController().navigate(R.id.action_addEventFragment_pop)
    }
}
