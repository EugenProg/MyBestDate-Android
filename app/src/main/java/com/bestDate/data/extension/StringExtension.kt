package com.bestDate.data.extension

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

fun String.isPhoneNumber(): Boolean {
    if (this.isBlank()) return false

    return this.replace("+", "")
        .replace(" ", "")
        .replace("-", "")
        .replace("(", "")
        .replace(")", "")
        .length in 10..14
}

fun String.formatToPhoneNumber(): String {
    return this.replace(" ", "")
        .replace("-", "")
        .replace("(", "")
        .replace(")", "")
}

fun String.isAEmail(): Boolean {
    return this.matches(Regex("\\S+@\\S+\\.[a-zA-Z]{2,3}"))
}

fun String?.toList(): MutableList<String>? {
    return this?.split(", ")?.toMutableList()
}

fun String?.getDateWithTimeOffset(): Date {
    if (this.isNullOrBlank()) return Date()
    val timeZone = TimeZone.getDefault()
    val offset = (timeZone.getOffset(Date().time) / 3600000) - 3

    val calendar = Calendar.getInstance()
    val date = this.substring(0, 10).split("-")
    val time = this.substring(11, 16).split(":")
    calendar.set(date[0].toInt(), date[1].toInt() - 1, date[2].toInt(), time[0].toInt(), time[1].toInt())
    calendar.add(Calendar.HOUR_OF_DAY, offset)

    return calendar.time
}

@SuppressLint("SimpleDateFormat")
fun String?.getWeekdayWithTime(): String {
    val formatter = SimpleDateFormat("EE HH:mm")
    return formatter.format(this.getDateWithTimeOffset())
}

@SuppressLint("SimpleDateFormat")
fun String?.getTime(): String {
    val formatter = SimpleDateFormat("HH:mm")
    return formatter.format(this.getDateWithTimeOffset())
}

@SuppressLint("SimpleDateFormat")
fun String?.isToday(): Boolean {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(Date()) == this?.substring(0..9)
}