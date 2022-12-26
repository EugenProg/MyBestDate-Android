package com.bestDate.data.extension

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.bestDate.data.utils.Logger
import kotlin.math.abs

open class OnSwipeTouchListener(context: Context) : OnTouchListener {

    private val gestureDetector: GestureDetector = GestureDetector(context, GestureListener())

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    inner class GestureListener : SimpleOnGestureListener() {
        private val verticalSwipeThreshold = 100
        private val horizontalSwipeThreshold = 100

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false

            try {
                val diffY = (e2?.y ?: 0f) - (e1?.y ?: 0f)
                val diffX = (e2?.x ?: 0f) - (e1?.x ?: 0f)

                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > horizontalSwipeThreshold && abs(velocityX) > horizontalSwipeThreshold) {
                        if (diffX > 0) onSwipeRight()
                        else onSwipeLeft()
                        result = true
                    }
                } else {
                    if (abs(diffY) > verticalSwipeThreshold && abs(velocityY) > verticalSwipeThreshold) {
                        if (diffY > 0) onSwipeBottom()
                        else onSwipeTop()
                        result = true
                    }
                }
            } catch (e: Exception) {
                Logger.print("Swipe error: ${e.message}")
            }

            return result
        }
    }
    open fun onSwipeRight() {}
    open fun onSwipeLeft() {}
    open fun onSwipeTop() {}
    open fun onSwipeBottom() {}
}

fun View.onSwipeListener(onSwipeTop: (() -> Unit)? = null,
                         onSwipeRight: (() -> Unit)? = null,
                         onSwipeLeft: (() -> Unit)? = null,
                         onSwipeBottom: (() -> Unit)? = null) {
    this.setOnTouchListener(object : OnSwipeTouchListener(this.context) {
        override fun onSwipeRight() {
            onSwipeRight?.invoke()
        }

        override fun onSwipeLeft() {
            onSwipeLeft?.invoke()
        }

        override fun onSwipeTop() {
            onSwipeTop?.invoke()
        }

        override fun onSwipeBottom() {
            onSwipeBottom?.invoke()
        }
    })
}