package com.bestDate.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bestDate.R
import com.bestDate.data.extension.animateError
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.extension.vibratePhone
import com.bestDate.databinding.ViewStandardInfoBinding

class StandardInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: ViewStandardInfoBinding
    var onClick: (() -> Unit)? = null

    init {
        val view = View.inflate(context, R.layout.view_standard_info, this)
        binding = ViewStandardInfoBinding.bind(view)

        binding.root.setOnSaveClickListener {
            onClick?.invoke()
        }
    }

    var hint: String
        get() = binding.placeholder.text.toString()
        set(value) {
            binding.placeholder.text = value
        }

    var text: String
        get() = binding.info.text.toString()
        set(value) {
            binding.info.text = value
            setDefault()
        }

    var icon: Int = R.drawable.ic_message
        set(value) {
            binding.icon.setImageResource(value)
        }

    fun setError() {
        with(binding) {
            placeholder.setTextColor(ContextCompat.getColor(context, R.color.red))
            root.setBackgroundResource(R.drawable.error_input_shape)
            info.setTextColor(ContextCompat.getColor(context, R.color.red))
            icon.setColorFilter(ContextCompat.getColor(context, R.color.red))
        }
        this.animateError()
        context.vibratePhone()
    }

    private fun setDefault() {
        with(binding) {
            placeholder.setTextColor(ContextCompat.getColor(context, R.color.white_60))
            root.setBackgroundResource(R.drawable.default_input_shape)
            info.setTextColor(ContextCompat.getColor(context, R.color.white_90))
            icon.setColorFilter(ContextCompat.getColor(context, R.color.white))
        }
    }
}