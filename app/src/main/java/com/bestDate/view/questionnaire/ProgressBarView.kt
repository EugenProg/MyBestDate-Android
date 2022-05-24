package com.bestDate.view.questionnaire

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar

class ProgressBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ProgressBar(context, attrs, defStyleAttr) {

    var onProgressChanged: ((progress: Int) -> Unit)? = null

    override fun setProgress(progress: Int) {
        super.setProgress(progress)
        onProgressChanged?.invoke(progress)
    }

    fun addProgress(progress: Int) {
        this.progress += progress
    }

    fun subtractProgress(progress: Int) {
        this.progress -= progress
    }
}