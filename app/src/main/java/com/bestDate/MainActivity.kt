package com.bestDate

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.bestDate.data.extension.Screens
import com.bestDate.data.extension.getCurrentScreen
import com.bestDate.data.extension.isBottomNavVisible
import com.bestDate.data.extension.observe
import com.bestDate.data.utils.notifications.NotificationType
import com.bestDate.databinding.ActivityMainBinding
import com.bestDate.view.bottomNav.BottomButton
import com.bestDate.view.bottomNav.CustomBottomNavView
import dagger.hilt.android.AndroidEntryPoint

//HG3g8wnkTaUGgQHxFcw4
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    var bottomNavView: CustomBottomNavView? = null
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setUpNavigation()
        setUpUserObserver()
        setUpUserListObserver()
        bottomNavView = binding.bottomNavigationView

        observe(viewModel.loggedOut) {
            if (it) {
                viewModel.clearUserData()
                navController.navigate(
                    R.id.routes,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.authFragment, true).build()
                )
            }
        }
        observe(viewModel.notificationsAction) {
            if (it == NotificationType.MESSAGE) {
                when (navController.currentDestination?.getCurrentScreen()) {
                    Screens.CHAT -> {
                        if (viewModel.isCurrentUserChat()) viewModel.refreshMessageList()
                        else viewModel.showPush(this)
                    }
                    Screens.CHAT_LIST -> {
                        viewModel.refreshChatList()
                    }
                    else -> viewModel.showPush(this)
                }
            } else {
                viewModel.showPush(this)
            }
        }
        observe(viewModel.navigationAction) {
            navController.navigate(it.first, it.second)
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
        observe(viewModel.myUser) {
            val newGuests = it?.new_guests ?: 0
            binding.bottomNavigationView.setBadge(
                BottomButton.GUESTS,
                newGuests > 0
            )
        }
    }

    private fun setUpUserListObserver() {
        viewModel.hasNewChats.observe(this) {
            binding.bottomNavigationView.setBadge(BottomButton.CHATS, it)
        }
    }
}