package com.bestDate.view.anotherProfile.questionnaire

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bestDate.R
import com.bestDate.data.extension.setAttrs
import com.bestDate.databinding.ViewQuestionnaireInfoBinding

class QuestionnaireInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewQuestionnaireInfoBinding = ViewQuestionnaireInfoBinding.inflate(
        LayoutInflater.from(context), this)

    init {
        setAttrs(attrs, R.styleable.QuestionnaireInfoView) {
            binding.title.text = it.getString(R.styleable.QuestionnaireInfoView_questionnaire_item_title)
        }
    }

    fun setInfo(text: String?) {
        binding.info.text = if (!text.isNullOrBlank()) text else "-"
    }
}