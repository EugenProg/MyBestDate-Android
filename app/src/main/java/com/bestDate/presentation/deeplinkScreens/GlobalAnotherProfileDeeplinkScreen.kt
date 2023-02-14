package com.bestDate.presentation.deeplinkScreens

import androidx.navigation.fragment.navArgs
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.BackScreenType
import com.bestDate.data.model.ShortUserData
import com.bestDate.presentation.base.anotherProfile.AnotherProfileSliderFragment
import com.bestDate.presentation.base.anotherProfile.BaseAnotherProfileFragment
import com.bestDate.presentation.base.anotherProfile.questionnaire.AnotherProfileQuestionnaireFragment
import com.bestDate.presentation.main.chats.chat.ChatFragment

class GlobalAnotherProfileDeeplinkScreen: BaseAnotherProfileFragment() {

    private val args by navArgs<GlobalAnotherProfileDeeplinkScreenArgs>()

    override fun onInit() {
        val id = args.userId
        viewModel.getUserById(id)
        super.onInit()
    }

    override fun getBackScreen() = args.backScreen

    override fun navigateToChat() {
        navController.navigate(
            GlobalAnotherProfileDeeplinkScreenDirections
                .actionGlobalAnotherProfileToChat(user, BackScreenType.ANOTHER_PROFILE)
        )
    }

    override fun navigateToSlider(position: Int) {
        navController.navigate(
            GlobalAnotherProfileDeeplinkScreenDirections
                .actionDeeplinkAnotherProfileToSlider(position, fullUser?.id.orZero)
        )
    }

    override fun navigateToQuestionnaire() {
        navController.navigate(
            GlobalAnotherProfileDeeplinkScreenDirections
                .actionDeeplinkAnotherProfileToQuestionnaire()
        )
    }

    override fun goBack() {
        navController.navigate(
            GlobalAnotherProfileDeeplinkScreenDirections
                .actionGlobalDeeplinkToMain()
        )
    }
}

class GlobalAnotherProfileDeeplinkQuestionnaireScreen: AnotherProfileQuestionnaireFragment()

class GlobalAnotherProfileDeeplinkSliderScreen: AnotherProfileSliderFragment() {

    private val args by navArgs<GlobalAnotherProfileDeeplinkSliderScreenArgs>()

    override fun getPosition() = args.position

    override fun getUserId() = args.userId
}

class GlobalChatDeeplinkScreen : ChatFragment() {

    override fun navigateToUserProfile(userData: ShortUserData?) {
        goBack()
    }

    override var customBackNavigation: Boolean = true
}
