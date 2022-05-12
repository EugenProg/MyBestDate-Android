package com.bestDate.view

import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class CalendarView {
    private val calendar = Calendar.getInstance()

    fun getDateSelectCalender(title: String,
                              saveClick: ((Date) -> Unit),
                              cancel: (() -> Unit)? = null): MaterialDatePicker<Long> {
        calendar.add(Calendar.YEAR, -18)
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