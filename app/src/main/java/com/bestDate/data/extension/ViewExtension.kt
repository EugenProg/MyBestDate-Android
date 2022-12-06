package com.bestDate.data.extension

import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import androidx.core.view.isVisible
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

fun View.setHeight(height: Int) {
    layoutParams.height = height
}

fun View.setWidth(width: Int) {
    layoutParams.width = width
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

inline fun View.setAttrs(attrs: AttributeSet?, styleable: IntArray, crossinline body: (TypedArray) -> Unit) {
    context.theme.obtainStyledAttributes(attrs, styleable, 0, 0)
        .apply {
            try {
                body.invoke(this)
            } finally {
                recycle()
            }
        }
}

inline fun View.showWithSlideTopAnimation(crossinline body: () -> Unit) {
    this.isVisible = false
    this.animate()
        .translationY(600f)
        .setDuration(100)
        .start()

    body.invoke()

    postDelayed({
        this.isVisible = true
        this.animate()
            .translationY(0f)
            .setDuration(300)
            .start()
    }, 100)
}