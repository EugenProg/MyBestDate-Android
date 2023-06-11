package com.bestDate.presentation.globalPushScreens

import androidx.navigation.fragment.navArgs
import com.bestDate.data.model.BackScreenType
import com.bestDate.data.model.Like
import com.bestDate.data.model.ShortUserData
import com.bestDate.presentation.main.chats.chat.ChatFragment
import com.bestDate.presentation.main.userProfile.invitationList.InvitationListFragment
import com.bestDate.presentation.main.userProfile.likesList.LikesListFragment
import com.bestDate.presentation.main.userProfile.matchesList.MatchesListFragment
import com.bestDate.view.button.InvitationActions

class GlobalLikePushScreen : LikesListFragment() {

    override fun navigateToUserProfile(like: Like) {
        navController.navigate(
            GlobalLikePushScreenDirections
                .actionLikePushToAnotherProfile(like.user, BackScreenType.PROFILE)
        )
    }

    override fun goBack() {
        if (navController.backQueue.size > 5) {
            super.goBack()
        } else {
            navController.navigate(
                GlobalLikePushScreenDirections.actionLikePushToSearch()
            )
        }
    }

    override var customBackNavigation: Boolean = true
}

class GlobalMatchPushScreen : MatchesListFragment() {

    override fun navigateToChat(userData: ShortUserData?) {
        navController.navigate(
            GlobalMatchPushScreenDirections
                .actionMatchPushToChat(userData, BackScreenType.PROFILE)
        )
    }

    override fun navigateToUserProfile(userData: ShortUserData?) {
        navController.navigate(
            GlobalMatchPushScreenDirections
                .actionMatchPushToAnotherProfile(userData, BackScreenType.PROFILE)
        )
    }

    override fun goBack() {
        if (navController.backQueue.size > 5) {
            super.goBack()
        } else {
            navController.navigate(
                GlobalMatchPushScreenDirections.actionMatchPushToSearch()
            )
        }
    }

    override var customBackNavigation: Boolean = true
}

class GlobalInvitationPushScreen : InvitationListFragment() {

    private val args by navArgs<GlobalInvitationPushScreenArgs>()

    override fun onInit() {
        super.onInit()
        if (args.page != InvitationActions.NEW) setPage(args.page)
    }

    override fun navigateToUserProfile(userData: ShortUserData?) {
        navController.navigate(
            GlobalInvitationPushScreenDirections
                .actionInvitationPushToAnotherProfile(userData, BackScreenType.PROFILE)
        )
    }

    override fun goBack() {
        if (navController.backQueue.size > 5) {
            super.goBack()
        } else {
            navController.navigate(
                GlobalInvitationPushScreenDirections.actionInvitationPushToSearch()
            )
        }
    }

    override var customBackNavigation: Boolean = true
}

class GlobalChatPushScreen : ChatFragment() {

    override fun navigateToUserProfile(userData: ShortUserData?) {
        navController.navigate(
            GlobalChatPushScreenDirections
                .actionChatPushToAnotherProfile(userData, BackScreenType.CHAT)
        )
    }

    override fun navigateToTariffList() {
        navController.navigate(
            GlobalChatPushScreenDirections.actionGlobalChatPushToTariffList()
        )
    }

    override fun navigateToSettings() {
        navController.navigate(
            GlobalChatPushScreenDirections.actionGlobalChatPushToSettings()
        )
    }

    override fun goBack() {
        if (navController.backQueue.size > 5) {
            super.goBack()
        } else {
            navController.navigate(
                GlobalChatPushScreenDirections.actionChatPushToSearch()
            )
        }
    }

    override var customBackNavigation: Boolean = true
}