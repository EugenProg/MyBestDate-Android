package com.bestDate.presentation.main.chats.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bestDate.R
import com.bestDate.data.extension.AnimationType
import com.bestDate.data.extension.NavigationResultKey
import com.bestDate.data.extension.close
import com.bestDate.data.extension.copyToClipboard
import com.bestDate.data.extension.getNavigationResult
import com.bestDate.data.extension.observe
import com.bestDate.data.extension.open
import com.bestDate.data.extension.postDelayed
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.extension.show
import com.bestDate.data.model.BackScreenType
import com.bestDate.data.model.Image
import com.bestDate.data.model.Message
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.FragmentChatBinding
import com.bestDate.db.entity.Invitation
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.view.alerts.BanningDialog
import com.bestDate.view.alerts.BuyDialogType
import com.bestDate.view.alerts.showCreateInvitationDialog
import com.bestDate.view.bottomSheet.chatActionsSheet.ChatActions
import com.bestDate.view.bottomSheet.chatActionsSheet.ChatActionsSheet
import com.bestDate.view.bottomSheet.imageSheet.ImageListSheet
import com.bestDate.view.chat.ChatStatusType
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class ChatFragment : BaseVMFragment<FragmentChatBinding, ChatViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentChatBinding =
        { inflater, parent, attach -> FragmentChatBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<ChatViewModel> = ChatViewModel::class.java

    override val statusBarColor: Int = R.color.bg_main
    private val args by navArgs<ChatFragmentArgs>()
    private var imageListSheet: ImageListSheet = ImageListSheet()
    private var user: ShortUserData? = null
    private var invitationList: MutableList<Invitation> = mutableListOf()

    @Inject
    lateinit var banningDialog: BanningDialog

    override fun onInit() {
        super.onInit()
        user = args.user
        setUserInfo()
        viewModel.sendReadingEvent(user?.id)
        viewModel.getChatMessages(user?.id)
        getNavigationResult<ShortUserData?>(NavigationResultKey.USER) {
            it?.let { user = it }
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            backButton.onClick = {
                goBack()
            }
            userBox.setOnSaveClickListener {
                if (user?.isBot() != true) {
                    navigateToUserProfile(user)
                }
            }
            chatView.sendClick = { text, parentId ->
                if (viewModel.messageSendAllowed()) {
                    viewModel.sendTextMessage(user?.id, parentId, text)
                } else {
                    chatView.stopSendLoading()
                    setUpBanningDialog(BuyDialogType.MESSAGE, {
                        viewModel.withdrawMessageCoins(parentId, text)
                    }) {
                        viewModel.sendTextMessage(user?.id, parentId, text)
                    }
                }
            }
            chatView.editClick = { text, messageId ->
                viewModel.editMessage(messageId, text)
            }
            chatView.translateClick = {
                viewModel.translateText(it, user?.language)
            }
            chatView.translateMessageClick = {
                viewModel.translateMessage(it)
            }
            chatView.returnMessageClick = {
                viewModel.returnMessage(it)
            }
            chatView.showInvitationClick = {
                requireActivity().showCreateInvitationDialog(invitationList) {
                    if (viewModel.invitationSendAllowed()) {
                        viewModel.sendInvitation(user?.id, it.id)
                    } else {
                        setUpBanningDialog(BuyDialogType.INVITATION, {
                            viewModel.withdrawInvitationCoins(it.id)
                        }) {
                            viewModel.sendInvitation(user?.id, it.id)
                        }
                    }
                }
            }
            chatView.openActionSheet = { message, chatActions ->
                if (user?.isBot() != true) {
                    val actionSheet = ChatActionsSheet(chatActions)
                    actionSheet.itemClick = {
                        doChatAction(it, message)
                    }
                    actionSheet.show(childFragmentManager)
                }
            }
            chatView.imageOpenClick = {
                val fragment = ChatImageViewFragment(it)
                open(fragment, binding.container, AnimationType.SCALE)

                fragment.closeAction = {
                    close(fragment, binding.container, AnimationType.SCALE) {
                        reDrawBars()
                    }
                }
            }
            chatView.addImageClick = {
                if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable()) showPhotoPicker()
                else imageListSheet.show(childFragmentManager, imageListSheet.tag)
            }
            chatView.typingEvent = {
                viewModel.sendTypingEvent()
            }
            chatView.loadNextPage = {
                viewModel.loadNextPage()
            }
            chatView.goToSettings = {
                navigateToSettings()
            }
            chatView.setChatClosed(viewModel.chatClosed())
            imageListSheet.itemClick = {
                imageListSheet.dismiss()
                openImageEditor(it)
            }
        }
    }

    private fun setUpBanningDialog(
        type: BuyDialogType,
        buyForCoinsAction: () -> Unit,
        sendAction: () -> Unit
    ) {
        banningDialog.show(requireActivity(), type)
        banningDialog.buySubscriptionAction = {
            navigateToTariffList()
        }
        banningDialog.sendAction = {
            postDelayed({
                sendAction.invoke()
            }, 300)
        }
        banningDialog.buyForCoinsAction = {
            buyForCoinsAction.invoke()
        }
    }

    private fun openImageEditor(image: Image) {
        val fragment = ChatAddImageFragment(user, image)
        open(fragment, binding.container)

        fragment.closeAction = {
            close(fragment, binding.container) {
                reDrawBars()
            }
        }

        fragment.navigateToTariffList = {
            close(fragment, binding.container) {
                navigateToTariffList()
            }
        }
    }

    open fun navigateToSettings() {
        navController.navigate(
            ChatFragmentDirections.actionGlobalChatToSettings()
        )
    }

    open fun navigateToUserProfile(userData: ShortUserData?) {
        if (args.backScreen == BackScreenType.ANOTHER_PROFILE) {
            navController.popBackStack()
        } else {
            navController.navigate(
                ChatFragmentDirections
                    .actionGlobalChatToAnotherProfile(userData, BackScreenType.CHAT)
            )
        }
    }

    open fun navigateToTariffList() {
        navController.navigate(
            ChatFragmentDirections.actionGlobalChatToTariffList()
        )
    }

    private fun doChatAction(action: ChatActions, message: Message?) {
        when (action) {
            ChatActions.REPLY -> {
                binding.chatView.setReplyMode(message)
                showKeyboardAction()
            }

            ChatActions.EDIT -> {
                binding.chatView.setEditMode(message)
                showKeyboardAction()
            }

            ChatActions.DELETE -> {
                viewModel.deleteMessage(message?.id)
            }

            ChatActions.COPY -> {
                message?.text?.copyToClipboard(requireContext())
                showMessage(getString(R.string.message_is_copied_to_clipboard))
            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.invitations) {
            invitationList = it
        }
        observe(viewModel.sendInvitationLiveData) {
            showMessage(R.string.invitation_is_send_successful)
        }
        observe(viewModel.messages) {
            it?.let {
                binding.chatView.setMessages(it, viewModel.meta)
                binding.chatView.setChatClosed(viewModel.chatClosed())
            }
        }
        observe(viewModel.sendMessageLiveData) {
            binding.chatView.stopSendLoading()
        }
        observe(viewModel.translateLiveData) {
            binding.chatView.setTranslatedText(it)
        }
        observe(viewModel.errorLiveData) {
            showMessage(it.exception.message)
            with(binding.chatView) {
                stopSendLoading()
                stopTranslateLoading()
            }
        }
        observe(viewModel.typingMode) {
            if (it != null) {
                binding.statusView.setStatus(it)
                binding.online.isVisible = true
            }
        }
        observe(viewModel.withdrawMessageLiveData) {
            viewModel.sendTextMessage(user?.id, it.first, it.second.orEmpty())
        }
        observe(viewModel.withdrawInvitationLiveData) {
            viewModel.sendInvitation(user?.id, it)
        }
    }

    override var customBackNavigation: Boolean = true

    override fun goBack() {
        viewModel.clearMessages()
        super.goBack()
    }

    private fun showPhotoPicker() {
        picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val picker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                val image = Image(uri = it)
                openImageEditor(image)
            }
        }

    private fun setUserInfo() {
        with(binding) {
            Glide.with(requireContext())
                .load(
                    if (user?.isBot() == true) R.mipmap.ic_launcher
                    else user?.getMainPhoto()?.thumb_url
                )
                .circleCrop()
                .into(avatar)
            name.text = user?.name
            online.isVisible = user?.is_online == true
            if (user?.is_online == true) statusView.setStatus(ChatStatusType.ONLINE)
            else statusView.setStatus(ChatStatusType.LAST_ONLINE, user?.last_online_at)

            chatView.setUser(user)
        }
    }

    override fun scrollAction() {
        super.scrollAction()
        binding.chatView.showInvitationView(false)
    }

    override fun hideAction() {
        super.hideAction()
        binding.chatView.showInvitationView(true)
    }

    override fun onResume() {
        super.onResume()
        postDelayed({
            hideKeyboardAction()
        }, 100)
    }

    override fun networkIsUpdated() {
        super.networkIsUpdated()
        viewModel.getChatMessages(user?.id)
    }
}