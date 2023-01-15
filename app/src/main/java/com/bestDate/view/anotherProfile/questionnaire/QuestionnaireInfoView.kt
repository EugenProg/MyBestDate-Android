package com.bestDate.view.anotherProfile.questionnaire

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.setAttrs
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewQuestionnaireInfoBinding

class QuestionnaireInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewQuestionnaireInfoBinding = ViewQuestionnaireInfoBinding.inflate(
        LayoutInflater.from(context), this)
    var translateClick: (() -> Unit)? = null

    init {
        setAttrs(attrs, R.styleable.QuestionnaireInfoView) {
            binding.title.text = it.getString(R.styleable.QuestionnaireInfoView_questionnaire_item_title)
        }
        binding.translateBox.setOnSaveClickListener {
            if (binding.info.text != "-") {
                toggleTranslateLoading(true)
                translateClick?.invoke()
            }
        }
    }

    fun setInfo(text: String?) {
        binding.info.text = if (!text.isNullOrBlank()) text else "-"
    }

    fun setTranslatable(show: Boolean) {
        toggleTranslateLoading(false)
        binding.translateBox.isVisible = show
    }

    private fun toggleTranslateLoading(enable: Boolean) {
        binding.translateButton.isVisible = !enable
        binding.translateLoading.isVisible = enable
    }
}