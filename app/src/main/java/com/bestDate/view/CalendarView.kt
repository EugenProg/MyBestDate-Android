package com.bestDate.view

import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.parcelize.Parcelize
import java.util.*

class CalendarView {
    private val calendar = Calendar.getInstance()

    fun getDateSelectCalender(
        title: String,
        selectedDate: Date?,
        saveClick: ((Date) -> Unit),
        cancel: (() -> Unit)? = null
    ): MaterialDatePicker<Long> {

        if (selectedDate == null) {
            calendar.add(Calendar.YEAR, -18)
            calendar.add(Calendar.DATE, -1)
        } else calendar.timeInMillis = selectedDate.time

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(calendar.timeInMillis)
            .setTitleText(title)
            .setCalendarConstraints(getCalendarConstrains())
            .build()

        datePicker.addOnPositiveButtonClickListener {
            calendar.timeInMillis = it
            saveClick.invoke(calendar.time)
        }

        datePicker.addOnCancelListener {
            cancel?.invoke()
        }

        return datePicker
    }

    private fun getCalendarConstrains(): CalendarConstraints {
        return CalendarConstraints.Builder()
            .setEnd(calendar.timeInMillis)
            .setValidator(AgeValidator())
            .build()
    }

    @Parcelize
    class AgeValidator : CalendarConstraints.DateValidator {
        override fun isValid(date: Long): Boolean {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.YEAR, -18)
            calendar.add(Calendar.DATE, -1)
            return date < calendar.timeInMillis
        }
    }
}
