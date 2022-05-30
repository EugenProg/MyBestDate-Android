package com.bestDate.data.extension

import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.SeekBar
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

fun SeekBar.onChangeListener(
    progressChanged: ((SeekBar?, Int, Boolean) -> Unit)? = null,
    startTouch: ((SeekBar?) -> Unit)? = null,
    stopTouch: ((SeekBar?) -> Unit)? = null) {

    this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(bar: SeekBar?, progress: Int, fromUser: Boolean) {
            progressChanged?.invoke(bar, progress, fromUser)
        }

        override fun onStartTrackingTouch(bar: SeekBar?) {
            startTouch?.invoke(bar)
        }

        override fun onStopTrackingTouch(bar: SeekBar?) {
            stopTouch?.invoke(bar)
        }
    })
}