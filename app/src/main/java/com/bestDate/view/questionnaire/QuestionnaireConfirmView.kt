package com.bestDate.view.questionnaire

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.bestDate.data.extension.textIsChanged
import com.bestDate.databinding.ViewQuestionnaireConfirmBinding

class QuestionnaireConfirmView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewQuestionnaireConfirmBinding =
        ViewQuestionnaireConfirmBinding.inflate(LayoutInflater.from(context), this)

    var codeIsReady: ((String) -> Unit)? = null

    init {
        binding.input.textIsChanged {
            if (it.length > 5) {
                codeIsReady?.invoke(it)
            }
        }
    }

    fun setFocus() {
        binding.input.isFocusableInTouchMode = true
        binding.input.requestFocus()
    }

    val hasFocus: Boolean
        get() = binding.input.hasFocus()
}