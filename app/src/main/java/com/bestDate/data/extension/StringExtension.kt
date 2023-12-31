package com.bestDate.data.extension

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.bestDate.R
import com.bestDate.data.utils.Logger
import com.bestDate.data.utils.notifications.NotificationType
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

fun String?.toListOrEmpty(): MutableList<String>? {
    return this?.split(", ")?.toMutableList()
}

fun String?.toListOrNull(): MutableList<String>? {
    val list = this?.split(", ")?.toMutableList()
    return if (list.isNullOrEmpty() || list.firstOrNull().isNullOrBlank()) null else list
}

fun String?.getDateWithTimeOffset(): Date {
    if (this.isNullOrBlank()) return Date()
    val timeZone = TimeZone.getDefault()
    val offset = (timeZone.getOffset(Date().time) / 3600000) - 3

    val calendar = Calendar.getInstance()
    val date = this.substring(0, 10).split("-")
    val time = this.substring(11, 16).split(":")
    calendar.set(
        date[0].toInt(),
        date[1].toInt() - 1,
        date[2].toInt(),
        time[0].toInt(),
        time[1].toInt()
    )
    calendar.add(Calendar.HOUR_OF_DAY, offset)

    return calendar.time
}

fun String?.getDate(): Date {
    if (this.isNullOrBlank()) return Date()
    val calendar = Calendar.getInstance()
    val date = this.substring(0, 10).split("-")
    val time = this.substring(11, 16).split(":")
    calendar.set(
        date[0].toInt(),
        date[1].toInt() - 1,
        date[2].toInt(),
        time[0].toInt(),
        time[1].toInt()
    )

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

@SuppressLint("SimpleDateFormat")
fun String?.toShortDate(): String {
    val formatter = SimpleDateFormat("dd MMM")
    return formatter.format(this.getDateWithTimeOffset())
}

fun String?.getVisitPeriod(context: Context): String {
    if (this.isNullOrBlank()) return ""
    val now = Date()
    val date = this.getDateWithTimeOffset()
    return if (this.isToday()) {
        val hours = getHoursBetween(date, now)
        val minutes = getMinutesBetween(date, now)
        when {
            hours > 0 -> context.getString(R.string.was_n_hours_ago, hours)
            minutes > 0 -> context.getString(R.string.was_n_min_ago, minutes)
            else -> ""
        }
    } else {
        val days = getDaysBetween(date, now)
        if (days > 6) date.toShortDate()
        else context.getString(R.string.was_n_days_ago, days)
    }
}

@SuppressLint("SimpleDateFormat")
fun String?.getDateString(): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy")
    return formatter.format(this.getDateWithTimeOffset())
}

fun String.getPushType(): NotificationType {
    if (this.isBlank()) return NotificationType.DEFAULT_PUSH
    return NotificationType.values().firstOrNull { it.code == this }
        ?: NotificationType.DEFAULT_PUSH
}

fun String?.copyToClipboard(context: Context) {
    val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    manager.setPrimaryClip(ClipData.newPlainText("token", this))
}

fun String?.openAsLink(context: Context) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(this)))
    } catch (e: Exception) {
        Logger.print("Error by open link: ${e.message}")
    }
}

fun String.share(context: Context) {
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.putExtra(Intent.EXTRA_TEXT, this)
    intent.type = "text/plain"
    context.startActivity(Intent.createChooser(intent, "Choose one"))
}

fun String?.safetyToInt(): Int {
    return if (this?.matches(Regex("[0-9]+")) == true) this.toInt()
    else 0
}