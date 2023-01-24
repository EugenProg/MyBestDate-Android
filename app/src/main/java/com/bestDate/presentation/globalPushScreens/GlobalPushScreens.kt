package com.bestDate.presentation.globalPushScreens

import com.bestDate.data.model.BackScreenType
import com.bestDate.data.model.Like
import com.bestDate.data.model.ShortUserData
import com.bestDate.presentation.main.chats.chat.ChatFragment
import com.bestDate.presentation.main.userProfile.invitationList.InvitationListFragment
import com.bestDate.presentation.main.userProfile.likesList.LikesListFragment
import com.bestDate.presentation.main.userProfile.matchesList.MatchesListFragment

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

class GlobalChatScreenPushScreen : ChatFragment() {

    override fun navigateToUserProfile(userData: ShortUserData?) {
        navController.navigate(
            GlobalChatScreenPushScreenDirections
                .actionChatPushToAnotherProfile(userData, BackScreenType.CHAT)
        )
    }

    override fun goBack() {
        if (navController.backQueue.size > 5) {
            super.goBack()
        } else {
            navController.navigate(
                GlobalChatScreenPushScreenDirections.actionChatPushToSearch()
            )
        }
    }

    override var customBackNavigation: Boolean = true
}