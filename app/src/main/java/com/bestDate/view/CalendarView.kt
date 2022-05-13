package com.bestDate.view

import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class CalendarView {
    private val calendar = Calendar.getInstance()

    fun getDateSelectCalender(title: String,
                              selectedDate: Date?,
                              saveClick: ((Date) -> Unit),
                              cancel: (() -> Unit)? = null): MaterialDatePicker<Long> {

        if (selectedDate == null) calendar.add(Calendar.YEAR, -18)
        else calendar.timeInMillis = selectedDate.time

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(calendar.timeInMillis)
            .setTitleText(title)
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
}