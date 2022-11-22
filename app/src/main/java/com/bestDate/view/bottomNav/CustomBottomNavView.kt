package com.bestDate.view.bottomNav

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.bestDate.R
import com.bestDate.databinding.ViewBottomNavBinding
import com.bestDate.databinding.ViewBottomNavButtonBinding

class CustomBottomNavView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var binding: ViewBottomNavBinding =
        ViewBottomNavBinding.inflate(LayoutInflater.from(context), this)

    private var listOfButtons: MutableList<CustomBottomNavButtonView> = mutableListOf()

    init {
        listOfButtons.add(binding.buttonSearch)
        listOfButtons.add(binding.buttonMatches)
        listOfButtons.add(binding.buttonChats)
        listOfButtons.add(binding.buttonTop)
        listOfButtons.add(binding.buttonGuests)

        setUpButtons()
    }

    private fun setUpButtons() {
        setUpButton(
            binding.buttonSearch,
            R.drawable.ic_search,
            R.drawable.ic_search_active,
            R.string.search
        )
        setUpButton(
            binding.buttonMatches,
            R.drawable.ic_matches,
            R.drawable.ic_matches_active,
            R.string.matches
        )
        setUpButton(
            binding.buttonChats,
            R.drawable.ic_chats,
            R.drawable.ic_chats_active,
            R.string.chats
        )
        setUpButton(binding.buttonTop, R.drawable.ic_top, R.drawable.ic_top_active, R.string.top_50)
        setUpButton(
            binding.buttonGuests,
            R.drawable.ic_guests,
            R.drawable.ic_guests_active,
            R.string.guests
        )

        binding.buttonSearch.isActive = true

    }

    private fun setUpButton(
        button: CustomBottomNavButtonView,
        icon: Int,
        iconActive: Int,
        label: Int,
        hasBadge: Boolean = false
    ) {
        button.icon = icon
        button.iconActive = iconActive
        button.label = context.getString(label)
        button.onClick = {
            handleButtonsActive(button)
        }
        button.hasBadge = hasBadge

    }

    private fun handleButtonsActive(button: CustomBottomNavButtonView) {
        button.isActive = true
        listOfButtons.forEach { buttonFromList ->
            if (buttonFromList != button)
                buttonFromList.isActive = false
        }
    }

    fun setupWithNavController(navController: NavController) {
        listOfButtons.forEach { button ->
            button.onNavigationChange = {
                when (button) {
                    binding.buttonSearch -> {
                        navController.navigate(R.id.search_nav_graph)
                    }
                    binding.buttonMatches -> {
                        navController.navigate(R.id.matches_nav_graph)
                    }
                    binding.buttonChats -> {
                        navController.navigate(R.id.chats_nav_graph)
                    }
                    binding.buttonTop -> {
                        navController.navigate(R.id.top_nav_graph)
                    }
                    binding.buttonGuests -> {
                        navController.navigate(R.id.guests_nav_graph)
                    }
                }
            }
        }
    }

    fun setActive(destination: NavDestination) {
        when (destination.displayName.split(":").getOrNull(1)) {
            "id/searchFragment" -> {
                handleButtonsActive(binding.buttonSearch)
            }
            "id/matchesFragment" -> {
                handleButtonsActive(binding.buttonMatches)
            }
            "id/chatsFragment" -> {
                handleButtonsActive(binding.buttonChats)
            }
            "id/topFragment" -> {
                handleButtonsActive(binding.buttonTop)
            }
            "id/guestsFragment" -> {
                handleButtonsActive(binding.buttonGuests)
            }
        }
    }
}