package com.bestDate.presentation.questionnaire

import com.bestDate.base.questionnaire.BaseQuestionnaireFragment

class StartQuestionnaireFragment: BaseQuestionnaireFragment() {
    override fun back() {
        navController.popBackStack()
    }

    override fun forward() {
        navController.navigate(StartQuestionnaireFragmentDirections.actionQuestionnaireToGeoEnable())
    }
}