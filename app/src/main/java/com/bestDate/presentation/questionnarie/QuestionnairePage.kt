package com.bestDate.presentation.questionnarie

import com.bestDate.R
import com.bestDate.view.questionnaire.list.QuestionnaireQuestion

data class QuestionnairePage(
    var number: Int,
    var type: QuestionnairePageType,
    var title: Int,
    var questions: MutableList<QuestionnaireQuestion>? = null,
    var nextButtonText: Int = R.string.next
)
