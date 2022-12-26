package com.bestDate.view.questionnaire

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewMultilineQuestionnaireInfoBinding

class MultilineQuestionnaireInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewMultilineQuestionnaireInfoBinding =
        ViewMultilineQuestionnaireInfoBinding.inflate(LayoutInflater.from(context), this)

    private var isEmpty: Boolean = true
    private var placeholder: String? = null
    var onClick: (() -> Unit)? = null

    init {
        binding.root.setOnSaveClickListener {
            onClick?.invoke()
        }
    }

    var hint: String
        get() = if (isEmpty) binding.info.text.toString()
        else binding.placeholder.text.toString()
        set(value) {
            placeholder = value
            if (isEmpty) binding.info.text = value
            else binding.placeholder.text = value
        }

    var info: String?
        get() = if (isEmpty) "" else binding.placeholder.text.toString()
        set(value) {
            when {
                !isEmpty && !value.isNullOrBlank() -> {
                    binding.info.text = value
                }
                !isEmpty && value.isNullOrBlank() -> {
                    binding.info.text = placeholder
                    binding.placeholder.isVisible = false
                    isEmpty = true
                    setUnActiveColor()
                }
                isEmpty && !value.isNullOrBlank() -> {
                    binding.info.text = value
                    binding.placeholder.text = placeholder
                    binding.placeholder.isVisible = true
                    isEmpty = false
                    setActiveColor()
                }
            }
        }

    var percent: Int
        get() = binding.percentNumber.text.toString().toInt()
        set(value) {
            binding.percentNumber.text = value.toString()
        }

    private fun setActiveColor() {
        with(binding) {
            info.setTextColor(ContextCompat.getColor(context, R.color.bg_main))
            val blueColor = ContextCompat.getColor(context, R.color.blue_90)

            percent.setTextColor(blueColor)
            percentNumber.setTextColor(blueColor)
            plus.setTextColor(blueColor)
        }
    }

    private fun setUnActiveColor() {
        with(binding) {
            val grayColor = ContextCompat.getColor(context, R.color.main_30)

            info.setTextColor(grayColor)
            percent.setTextColor(grayColor)
            percentNumber.setTextColor(grayColor)
            plus.setTextColor(grayColor)
        }
    }
}