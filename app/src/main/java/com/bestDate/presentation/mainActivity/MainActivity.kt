package com.bestDate.presentation.mainActivity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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
import com.bestDate.data.utils.NetworkStateListener
import com.bestDate.data.utils.NetworkStatus
import com.bestDate.data.utils.notifications.NotificationType
import com.bestDate.data.utils.notifications.TypingEventCoordinator
import com.bestDate.data.utils.subscription.SubscriptionManager
import com.bestDate.databinding.ActivityMainBinding
import com.bestDate.view.alerts.LostConnectionDialog
import com.bestDate.view.bottomNav.BottomButton
import com.bestDate.view.bottomNav.CustomBottomNavView
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//HG3g8wnkTaUGgQHxFcw4
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    var bottomNavView: CustomBottomNavView? = null
    private val viewModel: MainViewModel by viewModels()
    private lateinit var chatListTypingEventCoordinator: TypingEventCoordinator
    private lateinit var chatTypingEventCoordinator: TypingEventCoordinator
    private lateinit var lostConnectionDialog: LostConnectionDialog

    @Inject
    lateinit var subscriptionManager: SubscriptionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setUpNetworkListener()
        setUpNavigation()
        setUpUserObserver()
        setUpUserListObserver()
        setUpPusherObserver()
        setUpChatListTypingCoordinator()
        setUpChatTypingListener()
        setUpSubscriptionManager()
        setUpAdMobs()
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
                when {
                    it == NotificationType.MODERATION_SUCCESS -> viewModel.refreshUserData()
                    it == NotificationType.MODERATION_FAILED -> {
                        viewModel.refreshUserData()
                        viewModel.setStartWithPhotoSelector(true)
                    }
                }
                viewModel.showPush(this)
            }
        }
        observe(viewModel.navigationAction) {
            navController.navigate(it.first, it.second)
        }
    }

    private fun setUpAdMobs() {
        MobileAds.initialize(this) { }
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
            if (isInCurrentUserChat(it?.sender_id)) {
                viewModel.addChatMessage(it)
                viewModel.sendReadingEvent(it?.sender_id)
            }
            viewModel.setMessageToChatList(it)
        }
        observe(viewModel.editMessageLiveData) {
            if (isInCurrentUserChat(it?.sender_id)) {
                viewModel.editChatMessage(it)
            }
            viewModel.setMessageToChatList(it)
        }
        observe(viewModel.deleteMessageLiveData) {
            if (isInCurrentUserChat(it?.sender_id)) {
                viewModel.deleteChatMessage(it)
            }
            viewModel.refreshChatList()
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
            if (isInCurrentUserChat(it?.recipient_id)) {
                viewModel.editChatMessage(it)
            }
            viewModel.setMessageToChatList(it)
        }
        observe(viewModel.coinsLiveData) {
            viewModel.setCoinsCount(it)
        }
    }

    private fun setUpNetworkListener() {
        lostConnectionDialog = LostConnectionDialog(this)
        NetworkStateListener.init(this)
        if (NetworkStateListener.currentStatus == NetworkStatus.LOST) lostConnectionDialog.startLoading()

        NetworkStateListener.statusChanged = {
            if (it == NetworkStatus.LOST) lostConnectionDialog.startLoading()
            else lostConnectionDialog.stopLoading()
        }
    }

    private fun setUpSubscriptionManager() {
        subscriptionManager.updateSubscriptionData = { id, start, end ->
            viewModel.updateSubscriptionInfo(id, start, end)
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
        if (NetworkStateListener.currentStatus == NetworkStatus.LOST) return
        subscriptionManager.startConnection()
        viewModel.refreshAppSettings()
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