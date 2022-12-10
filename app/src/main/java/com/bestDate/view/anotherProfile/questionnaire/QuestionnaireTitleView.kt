package com.bestDate.view.anotherProfile.questionnaire

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bestDate.R
import com.bestDate.data.extension.setAttrs
import com.bestDate.databinding.ViewQuestionnaireTitleBinding

class QuestionnaireTitleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewQuestionnaireTitleBinding = ViewQuestionnaireTitleBinding.inflate(
        LayoutInflater.from(context), this)

    init {
        setAttrs(attrs, R.styleable.QuestionnaireTitleView) {
            binding.title.text = it.getString(R.styleable.QuestionnaireTitleView_questionnaire_title)
        }
    }
}