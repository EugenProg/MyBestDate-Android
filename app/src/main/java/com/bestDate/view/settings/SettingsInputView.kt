package com.bestDate.view.settings

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bestDate.R
import com.bestDate.data.extension.animateError
import com.bestDate.data.extension.setAttrs
import com.bestDate.data.extension.textIsChanged
import com.bestDate.data.extension.vibratePhone
import com.bestDate.databinding.ViewSettingsInputBinding

class SettingsInputView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewSettingsInputBinding =
        ViewSettingsInputBinding.inflate(LayoutInflater.from(context), this)
    var onTextChangeListener: ((String) -> Unit)? = null

    init {
        setAttrs(attrs, R.styleable.SettingsInputView) {
            binding.hint.text = it.getString(R.styleable.SettingsInputView_settings_input_hint)
            binding.icon.setImageResource(
                it.getResourceId(R.styleable.SettingsInputView_settings_input_icon, 0)
            )
        }

        binding.input.textIsChanged {
            onTextChangeListener?.invoke(it)
            setDefault()
        }
    }

    fun setText(text: String?) {
        binding.input.setText(text)
    }

    fun getText(): String {
        return binding.input.text.toString()
    }

    fun setError() {
        with(binding) {
            hint.setTextColor(ContextCompat.getColor(context, R.color.red))
            input.setTextColor(ContextCompat.getColor(context, R.color.red))
            icon.setColorFilter(ContextCompat.getColor(context, R.color.red))
        }
        this.animateError()
        context.vibratePhone()
    }

    private fun setDefault() {
        with(binding) {
            hint.setTextColor(ContextCompat.getColor(context, R.color.white_50))
            input.setTextColor(ContextCompat.getColor(context, R.color.white_80))
            icon.setColorFilter(ContextCompat.getColor(context, R.color.white_80))
        }
    }
}