package com.bestDate.data.extension

import android.animation.ValueAnimator
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.widget.TextView
import androidx.annotation.ColorRes
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

fun TextView.setColorAnimated(@ColorRes color: Int) {
    val newColor = ContextCompat.getColor(this.context, color)
    val animator = ValueAnimator.ofArgb(this.currentTextColor, newColor)
        .setDuration(300)
    animator.addUpdateListener {
        this.setTextColor(it.animatedValue as Int)
    }
    animator.start()
}