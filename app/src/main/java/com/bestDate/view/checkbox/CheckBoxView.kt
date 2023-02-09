package com.bestDate.view.checkbox

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.animateError
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.extension.vibratePhone
import com.bestDate.databinding.ViewCheckBoxBinding

class CheckBoxView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewCheckBoxBinding =
        ViewCheckBoxBinding.inflate(LayoutInflater.from(context), this)

    var textClick: (() -> Unit)? = null

    init {
        binding.checkBox.setOnClickListener {
            checked = !checked
        }

        binding.text.setOnSaveClickListener {
            textClick?.invoke()
        }
    }

    var checked: Boolean = false
    set(value) {
        field = value
        toggleCheckable()
    }

    fun setError() {
        with(binding) {
            checkBox.setBackgroundResource(R.drawable.check_box_error_frame)
            text.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        this.animateError()
        context.vibratePhone()
    }

    private fun toggleCheckable() {
        if (checked) check()
        else uncheck()
    }

    private fun check() {
        with(binding) {
            checkBox.setBackgroundResource(R.drawable.check_box_frame)
            text.setTextColor(ContextCompat.getColor(context, R.color.white))
            isCheckedView.isVisible = true
        }
    }

    private fun uncheck() {
        with(binding) {
            checkBox.setBackgroundResource(R.drawable.check_box_frame)
            text.setTextColor(ContextCompat.getColor(context, R.color.white_70))
            isCheckedView.isVisible = false
        }
    }
}