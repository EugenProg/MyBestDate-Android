package com.bestDate.data.utils.notifications

import com.bestDate.R

enum class NotificationType(var code: String, var destination: Int) {
    DEFAULT_PUSH("", R.id.searchFragment),
    LIKE("like", R.id.likesListFragment),
    MATCH("match", R.id.matchesListFragment),
    INVITATION("invitation", R.id.invitationListFragment),
    MESSAGE("message", R.id.chatFragment),
    GUEST("guest", R.id.guestsFragment)
}