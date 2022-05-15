package com.bestDate.data.extension

import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.updatePadding
import com.bestDate.R

fun View.setMarginTop(marginTop: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(0, marginTop, 0, 0)
    this.layoutParams = menuLayoutParams
}

fun View.animateError() {
    this.startAnimation(
        AnimationUtils.loadAnimation(context, R.anim.show_shake)
    )
}

fun View.setPaddingBottom(paddingBottom: Int) {
    this.updatePadding(bottom = paddingBottom.toPx())
}