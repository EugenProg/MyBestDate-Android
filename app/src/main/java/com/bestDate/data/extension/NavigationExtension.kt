package com.bestDate.data.extension

import androidx.navigation.NavDestination

fun NavDestination.isBottomNavVisible(): Boolean {
    return when (this.getCurrentScreen()) {
        Screens.SEARCH,
        Screens.MATCHES,
        Screens.CHAT_LIST,
        Screens.DUELS,
        Screens.GUESTS -> true
        else -> false
    }
}

fun NavDestination.getCurrentScreen(): Screens {
    return Screens.values().firstOrNull {
        this.label == it.label
    } ?: Screens.UNKNOWN
}

enum class Screens(var label: String) {
    SEARCH("SearchFragment"),
    MATCHES("MatchesFragment"),
    CHAT_LIST("ChatsFragment"),
    GUESTS("GuestsFragment"),
    DUELS("DuelsFragment"),
    CHAT("ChatFragment"),
    UNKNOWN("")
}