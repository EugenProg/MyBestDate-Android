package com.bestDate.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bestDate.databinding.ViewBarBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewBarBinding =
        ViewBarBinding.inflate(LayoutInflater.from(context), this)

    var percent: BigDecimal? = BigDecimal.ZERO
        @SuppressLint("SetTextI18n")
        set(value) {
            val progress = value?.setScale(2, RoundingMode.UP) ?: BigDecimal.ZERO
            binding.progressBar.progress = progress.toInt()
            binding.percentTextView.text = "$progress%"
            field = value
        }
}