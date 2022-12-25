package com.bestDate.view.questionnaire

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.bestDate.R
import com.bestDate.data.extension.animateError
import com.bestDate.data.extension.setAttrs
import com.bestDate.data.extension.textIsChanged
import com.bestDate.data.extension.vibratePhone
import com.bestDate.databinding.ViewQuestionnaireInputBinding

class QuestionnaireInputView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewQuestionnaireInputBinding =
        ViewQuestionnaireInputBinding.inflate(LayoutInflater.from(context), this)

    var onTextChangeListener: ((String) -> Unit)? = null

    init {
        setAttrs(attrs, R.styleable.QuestionnaireInputView) {
            binding.icon.setImageResource(
                it.getResourceId(
                    R.styleable.QuestionnaireInputView_questionnaire_input_icon,
                    R.drawable.ic_message
                )
            )
            binding.input.hint =
                it.getString(R.styleable.QuestionnaireInputView_questionnaire_input_hint)
        }

        binding.input.textIsChanged {
            setDefault()
            onTextChangeListener?.invoke(it)
        }
    }

    var inputType: Int = binding.input.inputType
        set(value) {
            binding.input.inputType = value
            field = value
        }

    fun setPlaceholder(text: String?) {
        binding.input.hint = text
    }

    fun setText(text: String?) {
        binding.input.setText(text)
        isChecked(!text.isNullOrBlank())
    }

    fun getText(): String {
        return binding.input.text.toString().trim()
    }

    fun isChecked(isChecked: Boolean) {
        binding.check.alpha = if (isChecked) 1f else 0.2f
    }

    fun setFocus() {
        binding.input.isFocusableInTouchMode = true
        binding.input.requestFocus()
    }

    override fun hasFocus(): Boolean = binding.input.hasFocus()

    fun setError() {
        with(binding) {
            input.setTextColor(ContextCompat.getColor(context, R.color.red))
            icon.setColorFilter(ContextCompat.getColor(context, R.color.red))
        }
        this.animateError()
        context.vibratePhone()
    }

    private fun setDefault() {
        with(binding) {
            input.setTextColor(ContextCompat.getColor(context, R.color.bg_main))
            icon.setColorFilter(ContextCompat.getColor(context, R.color.bg_main))
        }
    }
}