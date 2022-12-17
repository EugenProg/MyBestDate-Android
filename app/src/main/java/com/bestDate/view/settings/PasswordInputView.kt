package com.bestDate.view.settings

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bestDate.R
import com.bestDate.data.extension.*
import com.bestDate.databinding.ViewSettingsInputBinding

class PasswordInputView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val binding: ViewSettingsInputBinding =
        ViewSettingsInputBinding.inflate(LayoutInflater.from(context), this)
    private var passIsVisible: Boolean = false

    init {
        setAttrs(attrs, R.styleable.PasswordInputView) {
            binding.hint.text = it.getString(R.styleable.PasswordInputView_pass_input_hint)
            passIsVisible = it.getBoolean(R.styleable.PasswordInputView_show_pass, false)
            togglePassVisibility(passIsVisible)
        }

        binding.icon.setOnSaveClickListener {
            togglePassVisibility(!passIsVisible)
        }

        binding.input.textIsChanged {
            setDefault()
        }
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

    private fun togglePassVisibility(show: Boolean) {
        passIsVisible = show
        binding.icon.setImageResource(if (show) R.drawable.ic_eye else R.drawable.ic_eye_slash)
        binding.input.inputType =
            if (show) InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }
}