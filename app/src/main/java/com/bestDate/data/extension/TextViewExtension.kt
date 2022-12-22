package com.bestDate.data.extension

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.widget.TextView
import androidx.core.content.ContextCompat

fun TextView.setTextViewDrawableColor(color: Int) {
    for (drawable in this.compoundDrawablesRelative) {
        if (drawable != null) {
            drawable.colorFilter =
                PorterDuffColorFilter(
                    ContextCompat.getColor(this.context, color),
                    PorterDuff.Mode.SRC_IN
                )
        }
    }
}