package com.bestDate.data.extension

import java.util.*

object CalendarUtils {
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
}