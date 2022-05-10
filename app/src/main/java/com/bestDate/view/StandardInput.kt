package com.bestDate.view

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bestDate.R
import com.bestDate.data.extension.animateError
import com.bestDate.data.extension.textIsChanged
import com.bestDate.data.extension.vibratePhone
import com.bestDate.databinding.StandardInputViewBinding

class StandardInput @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: StandardInputViewBinding

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
        View.inflate(context, R.layout.standard_input_view, this)
        binding = StandardInputViewBinding.bind(this)

        binding.iconFrame.setOnClickListener {
            togglePassVisibility()
        }

        binding.input.textIsChanged {
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

    var icon: Int = R.drawable.ic_message
    set(value) {
        binding.icon.setImageResource(value)
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
        binding.placeholder.setTextColor(ContextCompat.getColor(context, R.color.red))
        binding.root.setBackgroundResource(R.drawable.error_input_shape)
        binding.input.setTextColor(ContextCompat.getColor(context, R.color.red))
        this.animateError()
        context.vibratePhone()
    }

    private fun setDefault() {
        binding.placeholder.setTextColor(ContextCompat.getColor(context, R.color.white_60))
        binding.root.setBackgroundResource(R.drawable.default_input_shape)
        binding.input.setTextColor(ContextCompat.getColor(context, R.color.white_90))
    }

    fun setFocus() {
        binding.input.isFocusableInTouchMode = true
        binding.input.requestFocus()
    }

}