package com.bestDate.data.utils.notifications

import com.bestDate.R

enum class NotificationType(var code: String, var destination: Int) {
    DEFAULT_PUSH("", R.id.search_nav_graph),
    LIKE("like", R.id.globalLikePushScreen),
    MATCH("match", R.id.globalMatchPushScreen),
    INVITATION("invitation", R.id.globalInvitationPushScreen),
    INVITATION_ANSWER("invitation_answer", R.id.globalInvitationPushScreen),
    MESSAGE("message", R.id.globalChatPushScreen),
    GUEST("guest", R.id.guests_nav_graph)
}