package com.bestDate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.bestDate.data.extension.observe
import com.bestDate.data.utils.SessionManager
import com.bestDate.data.utils.notifications.NotificationCenter
import com.bestDate.databinding.ActivityMainBinding
import com.bestDate.presentation.main.UserUseCase
import com.bestDate.presentation.main.chats.ChatListUseCase
import com.bestDate.view.bottomNav.BottomButton
import com.bestDate.view.bottomNav.CustomBottomNavView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//HG3g8wnkTaUGgQHxFcw4
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    var bottomNavView: CustomBottomNavView? = null

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var userUseCase: UserUseCase

    @Inject
    lateinit var chatListUseCase: ChatListUseCase

    @Inject
    lateinit var notificationCenter: NotificationCenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setUpNavigation()
        setUpUserObserver()
        setUpUserListObserver()
        bottomNavView = binding.bottomNavigationView

        observe(sessionManager.loggedOut) {
            if (it) {
                userUseCase.clearUserData()
                navController.navigate(
                    R.id.routes,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.authFragment, true).build()
                )
            }
        }
        observe(notificationCenter.notificationsAction) {
            notificationCenter.showPush(this)
        }
        observe(notificationCenter.navigationAction) {
            navController.navigate(it)
        }
    }


    private fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isBottomVisible = destination.isBottomNavVisible()
            binding.bottomNavigationView.post {
                binding.bottomNavigationView.isVisible = isBottomVisible
                binding.bottomNavigationView.setActive(destination)
            }
        }
        binding.bottomNavigationView.setupWithNavController(navController)

    }

    private fun setUpUserObserver() {
        userUseCase.getMyUser.asLiveData().observe(this) {
            val newGuests = it?.new_guests ?: 0
            binding.bottomNavigationView.setBadge(
                BottomButton.GUESTS,
                newGuests > 0
            )
        }
    }

    private fun setUpUserListObserver() {
        chatListUseCase.hasNewChats.observe(this) {
            binding.bottomNavigationView.setBadge(BottomButton.CHATS, it)
        }
    }
}

fun NavDestination.isBottomNavVisible(): Boolean {
    return when (displayName.split(":").getOrNull(1)) {
        "id/searchFragment",
        "id/matchesFragment",
        "id/chatsFragment",
        "id/duelsFragment",
        "id/guestsFragment" -> true
        else -> false
    }
}