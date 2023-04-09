package com.bestDate.presentation.questionnaire

import com.bestDate.presentation.base.questionnaire.BaseQuestionnaireFragment

class StartQuestionnaireFragment : BaseQuestionnaireFragment() {
    override fun back() {
        navController.popBackStack()
    }

    override fun forward() {
        viewModel.increaseQuestionnaireSkipCount()
        if (viewModel.isFirstEnter()) {
            navController.navigate(
                StartQuestionnaireFragmentDirections
                    .actionQuestionnaireToGeoEnable()
            )
        } else {
            navController.navigate(
                StartQuestionnaireFragmentDirections
                    .actionQuestionnaireFragmentToMainNavGraph()
            )
        }
    }
}