package com.samoana.donotdisturb.utils

import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

private val dateFormat = SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH)

private val timeFormat = SimpleDateFormat("h:mm a", Locale.ENGLISH)

fun getDateString(dateTime: DateTime): String = dateFormat.format(dateTime.toDate())

fun getTimeString(dateTime: DateTime): String = timeFormat.format(dateTime.toDate())