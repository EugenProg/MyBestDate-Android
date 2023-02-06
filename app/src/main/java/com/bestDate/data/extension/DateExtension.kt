package com.bestDate.data.extension

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.min

fun getDaysBetween(firstDate: Date, secondDate: Date): Int {
    val days = (getMillisecondsBetween(firstDate, secondDate) / (24 * 60 * 60 * 1000)).toInt()
    return if (days > 0) days else 1
}

fun getHoursBetween(firstDate: Date, secondDate: Date): Int {
    return (getMillisecondsBetween(firstDate, secondDate) / (60 * 60 * 1000)).toInt()
}

fun getMinutesBetween(firstDate: Date, secondDate: Date): Int {
    return (getMillisecondsBetween(firstDate, secondDate) / (60 * 1000)).toInt()
}

fun getMillisecondsBetween(firstDate: Date, secondDate: Date): Long {
    val max = max(firstDate.time, secondDate.time)
    val min = min(firstDate.time, secondDate.time)
    return max - min
}

@SuppressLint("SimpleDateFormat")
fun Date.toShortDate(): String {
    val formatter = SimpleDateFormat("dd MMM")
    return formatter.format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.toWeekday(): String {
    val formatter = SimpleDateFormat("EE")
    return formatter.format(this)
}

fun getDiffYears(first: Date, last: Date): Int {
    val a = getCalendar(first) ?: return 0
    val b = getCalendar(last) ?: return 0
    var diff = b[Calendar.YEAR] - a[Calendar.YEAR]
    if (a[Calendar.MONTH] > b[Calendar.MONTH] ||
        a[Calendar.MONTH] == b[Calendar.MONTH] && a[Calendar.DATE] > b[Calendar.DATE]
    ) {
        diff--
    }
    return diff
}

private fun getCalendar(date: Date?): Calendar? {
    val cal = Calendar.getInstance(Locale.US)
    if (date != null) {
        cal.time = date
    }
    return cal
}

fun getEighteenYearDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.YEAR, -18)
    calendar.add(Calendar.DATE, -1)
    return calendar.time
}