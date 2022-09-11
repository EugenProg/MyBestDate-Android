package com.bestDate.view.questionnaire.list

import android.os.Parcelable
import com.bestDate.presentation.questionnarie.Question
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionnaireQuestion(
    var questionInfo: Question? = null,
    var answer: String? = null
): Parcelable
