package com.bestDate.view.filter

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewFilterButtonBinding

class FilterButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewFilterButtonBinding =
        ViewFilterButtonBinding.inflate(LayoutInflater.from(context), this)

    var onClick: (() -> Unit)? = null

    init{
        binding.root.setOnSaveClickListener {
            onClick?.invoke()
        }
    }

    var isActive: Boolean = false
        set(value) {
            if (value) {
                binding.textView.setTextColor(ContextCompat.getColor(context, R.color.blue))
                binding.bgView.setBackgroundResource(R.drawable.bg_dark_solid_light_blue_stroke)
            } else {
                binding.textView.setTextColor(ContextCompat.getColor(context, R.color.white))
                binding.bgView.setBackgroundResource(R.drawable.bg_dark_solid_light_stroke)
            }
            field = value
        }

    var label: String = ""
        set(value) {
            binding.textView.text = value
            field = value
        }

}