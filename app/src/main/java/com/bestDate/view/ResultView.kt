package com.bestDate.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bestDate.databinding.ViewResultBinding

class ResultView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewResultBinding =
        ViewResultBinding.inflate(LayoutInflater.from(context), this)

    var percent: Double = 0.0
        set(value) {
            //binding.barView.percent = value
            field = value
        }
}