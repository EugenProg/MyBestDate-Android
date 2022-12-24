package com.bestDate.base.questionnaire

import android.os.Parcelable
import com.bestDate.R
import kotlinx.parcelize.Parcelize

data class QuestionnairePage(
    var number: Int,
    var type: QuestionnairePageType,
    var title: Int,
    var questions: MutableList<QuestionnaireQuestion>? = null,
    var nextButtonText: Int = R.string.next
)

enum class QuestionnairePageType {
    QUESTION_LIST,
    MULTILINE_INPUT
}

enum class QuestionnaireViewType(val id: Int) {
    ONE_LINE_INFO_VIEW(0),
    MULTILINE_INFO_VIEW(1),
    SEEKBAR_VIEW(2),
    RANGE_BAR_VIEW(3),
    CONFIRMATION_VIEW(4),
    INPUT_LOCATION_VIEW(5)
}

@Parcelize
data class QuestionnaireQuestion(
    var questionInfo: Question? = null,
    var answer: String? = null
): Parcelable