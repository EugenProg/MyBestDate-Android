package com.bestDate.presentation.mainActivity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.bestDate.R
import com.bestDate.data.extension.Screens
import com.bestDate.data.extension.getCurrentScreen
import com.bestDate.data.extension.isBottomNavVisible
import com.bestDate.data.extension.observe
import com.bestDate.data.utils.notifications.NotificationType
import com.bestDate.data.utils.notifications.TypingEventCoordinator
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
    private lateinit var chatListTypingEventCoordinator: TypingEventCoordinator
    private lateinit var chatTypingEventCoordinator: TypingEventCoordinator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setUpNavigation()
        setUpUserObserver()
        setUpUserListObserver()
        setUpPusherObserver()
        setUpChatListTypingCoordinator()
        setUpChatTypingListener()
        bottomNavView = binding.bottomNavigationView

        if (!allPermissionsGranted()) {
            pushPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

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
            if (!(it == NotificationType.MESSAGE && (isInCurrentUserChat() || isInChatList()))) {
                viewModel.showPush(this)
            }
        }
        observe(viewModel.navigationAction) {
            navController.navigate(it.first, it.second)
        }
    }

    var pushPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

    private fun setUpPusherObserver() {
        observe(viewModel.newMessageLiveData) {
            binding.bottomNavigationView.setBadge(BottomButton.CHATS, true)
            when {
                isInChatList() -> {
                    viewModel.refreshChatList()
                }
                isInCurrentUserChat(it?.sender_id) -> {
                    viewModel.addChatMessage(it)
                    viewModel.sendReadingEvent(it?.sender_id)
                }
            }
        }
        observe(viewModel.editMessageLiveData) {
            when {
                isInChatList() -> {
                    viewModel.refreshChatList()
                }
                isInCurrentUserChat(it?.sender_id) -> {
                    viewModel.editChatMessage(it)
                }
            }
        }
        observe(viewModel.deleteMessageLiveData) {
            when {
                isInChatList() -> {
                    viewModel.refreshChatList()
                }
                isInCurrentUserChat(it?.sender_id) -> {
                    viewModel.deleteChatMessage(it)
                }
            }
        }
        observe(viewModel.typingLiveData) {
            when {
                isInChatList() -> {
                    viewModel.setChatListTypingEvent(it, true)
                    chatListTypingEventCoordinator.setTypingEvent(it)
                }
                isInCurrentUserChat(it) -> {
                    viewModel.setChatTypingEvent(true)
                    chatTypingEventCoordinator.setTypingEvent(it)
                }
            }
        }
        observe(viewModel.readingLiveData) {
            when {
                isInChatList() -> {
                    viewModel.refreshChatList()
                }
                isInCurrentUserChat(it?.recipient_id) -> {
                    viewModel.editChatMessage(it)
                }
            }
        }
        observe(viewModel.coinsLiveData) {
            viewModel.setCoinsCount(it)
        }
    }

    private fun setUpChatListTypingCoordinator() {
        chatListTypingEventCoordinator = TypingEventCoordinator {
            viewModel.setChatListTypingEvent(it, false)
        }
    }

    private fun setUpChatTypingListener() {
        chatTypingEventCoordinator = TypingEventCoordinator {
            viewModel.setChatTypingEvent(false)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshData(getString(R.string.app_locale))
        if (isInChatList()) viewModel.refreshChatList()
        if (isInChat()) viewModel.refreshChatMessages()
    }

    override fun onPause() {
        super.onPause()
        viewModel.disconnectPusher()
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
        observe(viewModel.hasNewGuests) {
            binding.bottomNavigationView.setBadge(BottomButton.GUESTS, it)
        }
    }

    private fun setUpUserListObserver() {
        observe(viewModel.hasNewChats) {
            binding.bottomNavigationView.setBadge(BottomButton.CHATS, it)
        }
    }

    private fun isInChatList() =
        navController.currentDestination?.getCurrentScreen() == Screens.CHAT_LIST

    private fun isInCurrentUserChat(senderId: Int?) = isInChat() &&
            viewModel.isCurrentUserChat(senderId)

    private fun isInCurrentUserChat() = isInChat() && viewModel.isCurrentUserChat()

    private fun isInChat() = navController.currentDestination?.getCurrentScreen() == Screens.CHAT
}