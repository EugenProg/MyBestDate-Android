package com.bestDate.presentation.main.userProfile.questionnaire

import com.bestDate.base.questionnaire.BaseQuestionnaireFragment

class ProfileQuestionnaireFragment: BaseQuestionnaireFragment() {
    override fun back() {
        navController.popBackStack()
    }

    override fun forward() {
        navController.popBackStack()
    }

    override var showSkipButton: Boolean = false
}