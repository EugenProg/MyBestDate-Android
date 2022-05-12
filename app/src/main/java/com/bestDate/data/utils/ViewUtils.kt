package com.bestDate.data.utils

import android.view.View
import androidx.core.view.isVisible

class ViewUtils {

    fun hideViews(vararg views: View) {
        for (view in views) {
            view.isVisible = false
        }
    }

    fun showViews(vararg views: View) {
        for (view in views) {
            view.isVisible = true
        }
    }
}