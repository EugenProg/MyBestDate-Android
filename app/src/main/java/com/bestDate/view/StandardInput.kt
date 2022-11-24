package com.bestDate.view

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.*
import com.bestDate.databinding.ViewStandardInputBinding

class StandardInput @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: ViewStandardInputBinding
    var onTextChangeListener: ((String) -> Unit)? = null

    private var passIsVisible: Boolean = false
    var isPasswordField: Boolean = false
    set(value) {
        if (value) {
            icon = R.drawable.ic_eye_slash
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        field = value
    }

    val hasFocus: Boolean
        get() = binding.input.hasFocus()

    init {
        View.inflate(context, R.layout.view_standard_input, this)
        binding = ViewStandardInputBinding.bind(this)

        setAttrs(attrs, R.styleable.StandardInput) {
            isPasswordField = it.getBoolean(R.styleable.StandardInput_isPassField, false)
            icon = it.getResourceId(R.styleable.StandardInput_src, R.drawable.ic_message)
            hint = it.getString(R.styleable.StandardInput_hint).orEmpty()
        }

        binding.iconFrame.setOnClickListener {
            togglePassVisibility()
        }

        binding.input.textIsChanged {
            onTextChangeListener?.invoke(it)
            setDefault()
        }
    }

    var hint: String
    get() = binding.placeholder.text.toString()
    set(value) {
        binding.placeholder.text = value
    }

    var text: String
    get() = binding.input.text.toString()
    set(value) {
        binding.input.setText(value)
    }

    @DrawableRes var icon: Int? = R.drawable.ic_message
    set(value) {
        if (value == null) binding.icon.isVisible = false
        else {
            binding.icon.setImageResource(value)
            binding.icon.isVisible = true
        }

    }

    var inputType: Int =
        if (isPasswordField) InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        else InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    set(value) {
        binding.input.inputType = value
        field = value
    }

    private fun togglePassVisibility() {
        if (isPasswordField) {
            if (passIsVisible) {
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                icon = R.drawable.ic_eye_slash
                passIsVisible = false
            } else {
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                icon = R.drawable.ic_eye
                passIsVisible = true
            }
            binding.input.setSelection(binding.input.text.length)
        }
    }

    fun setError() {
        with(binding) {
            placeholder.setTextColor(ContextCompat.getColor(context, R.color.red))
            root.setBackgroundResource(R.drawable.error_input_shape)
            input.setTextColor(ContextCompat.getColor(context, R.color.red))
            icon.setColorFilter(ContextCompat.getColor(context, R.color.red))
        }
        this.animateError()
        context.vibratePhone()
    }

    private fun setDefault() {
        with(binding) {
            placeholder.setTextColor(ContextCompat.getColor(context, R.color.white_60))
            root.setBackgroundResource(R.drawable.default_input_shape)
            input.setTextColor(ContextCompat.getColor(context, R.color.white_90))
            icon.setColorFilter(ContextCompat.getColor(context, R.color.white))
        }
    }

    fun setFocus() {
        binding.input.isFocusableInTouchMode = true
        binding.input.requestFocus()
    }
}