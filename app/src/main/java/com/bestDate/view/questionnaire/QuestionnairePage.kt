package com.bestDate.view.questionnaire

import com.bestDate.R

data class QuestionnairePage(
    var number: Int,
    var type: QuestionnairePageType,
    var title: Int,
    var questions: MutableList<QuestionnaireQuestion>? = null,
    var nextButtonText: Int = R.string.next
)
