package com.bestDate.data.extension

import android.os.SystemClock
import android.view.View

class SaveOnClickListener(
    private var defaultInterval: Int = 1000,
    private val action: (View) -> Unit
) : View.OnClickListener {

    private var lastTimeClicked: Long = 0

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        action(v)
    }
}

fun View.setOnSaveClickListener(action: (View) -> Unit) {
    setOnClickListener(SaveOnClickListener {
        action(it)
    })
}