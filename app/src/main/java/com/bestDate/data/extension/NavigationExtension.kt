package com.bestDate.data.extension

import androidx.navigation.NavDestination
import com.bestDate.R

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
    UNKNOWN("");

    fun navigateToSearch(): Int {
        return when(this) {
            MATCHES -> R.id.action_global_matches_to_search
            CHAT_LIST -> R.id.action_global_chat_list_to_search
            DUELS -> R.id.action_global_top_to_search
            GUESTS -> R.id.action_global_guests_to_search
            else -> R.id.searchFragment
        }
    }

    fun navigateToMatches(): Int {
        return when(this) {
            SEARCH -> R.id.action_global_search_to_matches
            CHAT_LIST -> R.id.action_global_chat_list_to_matches
            DUELS -> R.id.action_global_top_to_matches
            GUESTS -> R.id.action_global_guests_to_matches
            else -> R.id.matchesFragment
        }
    }

    fun navigateToChatList(): Int {
        return when(this) {
            SEARCH -> R.id.action_global_search_to_chat_list
            MATCHES -> R.id.action_global_matches_to_chat_list
            DUELS -> R.id.action_global_top_to_chat_list
            GUESTS -> R.id.action_global_guests_to_chat_list
            else -> R.id.chatsFragment
        }
    }

    fun navigateToDuels(): Int {
        return when(this) {
            SEARCH -> R.id.action_global_search_to_top
            MATCHES -> R.id.action_global_matches_to_top
            CHAT_LIST -> R.id.action_global_chat_list_to_top
            GUESTS -> R.id.action_global_guests_to_top
            else -> R.id.duelsFragment
        }
    }

    fun navigateToGuests(): Int {
        return when(this) {
            SEARCH -> R.id.action_global_search_to_guests
            MATCHES -> R.id.action_global_matches_to_guests
            CHAT_LIST -> R.id.action_global_chat_list_to_guests
            DUELS -> R.id.action_global_top_to_guests
            else -> R.id.guestsFragment
        }
    }
}