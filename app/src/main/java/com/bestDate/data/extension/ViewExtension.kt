package com.bestDate.data.extension

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.viewpager2.widget.ViewPager2
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

fun View.showWithSlideTopAnimation(showViews: (() -> Unit)? = null) {
    this.isVisible = false
    this.animate()
        .translationY(600f)
        .setDuration(100)
        .start()

    showViews?.invoke()

    postDelayed({
        this.isVisible = true
        this.animate()
            .translationY(0f)
            .setDuration(300)
            .start()
    }, 100)
}

fun View.showWithSlideTopAndRotateAnimation(showViews: (() -> Unit)? = null) {
    this.isVisible = false
    this.animate()
        .translationY(800f)
        .rotationY(-180f)
        .setDuration(100)
        .start()

    showViews?.invoke()

    postDelayed({
        this.isVisible = true
        this.animate()
            .translationY(0f)
            .rotationY(0f)
            .setDuration(450)
            .start()
    }, 100)
}

fun View.rotateHorizontally(degree: Float = 180f, toggleVisibility: () -> Unit) {
    val animator = AnimatorSet()

    val rotation = ObjectAnimator.ofFloat(this, View.ROTATION_Y, degree)
    animator.duration = 300
    animator.interpolator = AccelerateDecelerateInterpolator()
    animator.play(rotation)
    animator.start()

    postDelayed({
        toggleVisibility.invoke()
    }, 150)
}

fun View.fadeInsert(toggleVisibility: () -> Unit) {
    postDelayed({
        toggleVisibility.invoke()
        this.animate()
            .alpha(1f)
            .setDuration(100)
            .start()
    }, 100)

    this.animate()
        .alpha(0.8f)
        .setDuration(110)
        .start()
}

fun ViewPager2?.onPageChanged(onScrolled: ((position: Int,
                                           positionOffset: Float,
                                           positionOffsetPixels: Int) -> Unit)? = null,
                             onSelected: ((position: Int) -> Unit)? = null) {
    this?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            onScrolled?.invoke(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            onSelected?.invoke(position)
        }
    })
}

fun View.showKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun View.hideKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 2)
}