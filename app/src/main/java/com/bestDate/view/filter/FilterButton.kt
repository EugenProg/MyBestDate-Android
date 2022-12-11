package com.bestDate.view.filter

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewFilterButtonBinding


class FilterButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewFilterButtonBinding =
        ViewFilterButtonBinding.inflate(LayoutInflater.from(context), this)

    var onClick: (() -> Unit)? = null

    init {
        binding.root.setOnSaveClickListener {
            onClick?.invoke()
        }
    }

    var isActive: Boolean = false
        set(value) {
            if (value) {
                binding.textView.setTextColor(ContextCompat.getColor(context, R.color.blue))
                binding.bgView.setBackgroundResource(R.drawable.bg_dark_solid_light_blue_stroke)
                setTextViewDrawableColor(binding.textView, R.color.blue)
            } else {
                binding.textView.setTextColor(ContextCompat.getColor(context, R.color.white_30))
                binding.bgView.setBackgroundResource(R.drawable.bg_dark_solid_light_stroke)
                setTextViewDrawableColor(binding.textView, R.color.white_6)
            }
            field = value
        }

    var label: String = ""
        set(value) {
            binding.textView.text = value
            field = value
        }

    private fun setTextViewDrawableColor(textView: TextView, color: Int) {
        for (drawable in textView.compoundDrawablesRelative) {
            if (drawable != null) {
                drawable.colorFilter =
                    PorterDuffColorFilter(
                        ContextCompat.getColor(textView.context, color),
                        PorterDuff.Mode.SRC_IN
                    )
            }
        }
    }
}