package com.bestDate.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bestDate.databinding.ViewBarBinding

class BarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewBarBinding =
        ViewBarBinding.inflate(LayoutInflater.from(context), this)

    var percent: Double = 0.0
        set(value) {
            val layoutParams = binding.resultView.layoutParams
            layoutParams.width =
                (binding.backGroundView.layoutParams.width).times(percent).div(100).toInt()
            binding.percentTextView.text = percent.toString()
            field = value
        }
}