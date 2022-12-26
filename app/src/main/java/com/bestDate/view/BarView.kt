package com.bestDate.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bestDate.data.extension.decimalAfterDot
import com.bestDate.databinding.ViewBarBinding

class BarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewBarBinding =
        ViewBarBinding.inflate(LayoutInflater.from(context), this)

    var percent: Double? = 0.0
        @SuppressLint("SetTextI18n")
        set(value) {
            val layoutParams = binding.resultView.layoutParams
            layoutParams.width =
                (binding.backGroundView.measuredWidth * (String.format("%.2f", value ?: 0.0)
                    .toDouble()) / 100).toInt()
            binding.resultView.layoutParams = layoutParams
            binding.percentTextView.text = "${value.decimalAfterDot()}%"
            field = value
        }
}