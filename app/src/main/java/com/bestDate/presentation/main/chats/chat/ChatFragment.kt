package com.bestDate.presentation.main.chats.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bestDate.R
import com.bestDate.data.extension.*
import com.bestDate.data.model.BackScreenType
import com.bestDate.data.model.Message
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.FragmentChatBinding
import com.bestDate.db.entity.Invitation
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.view.alerts.showCreateInvitationDialog
import com.bestDate.view.bottomSheet.chatActionsSheet.ChatActions
import com.bestDate.view.bottomSheet.chatActionsSheet.ChatActionsSheet
import com.bestDate.view.bottomSheet.imageSheet.ImageListSheet
import com.bestDate.view.chat.ChatStatusType
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

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

    override fun onInit() {
        super.onInit()
        user = args.user
        setUserInfo()
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
                viewModel.sendTextMessage(user?.id, parentId, text)
            }
            chatView.editClick = { text, messageId ->
                viewModel.editMessage(messageId, text)
            }
            chatView.translateClick = {
                viewModel.translateText(it, user?.language)
            }
            chatView.showInvitationClick = {
                requireActivity().showCreateInvitationDialog(invitationList) {
                    viewModel.sendInvitation(user?.id, it.id)
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
                imageListSheet.show(childFragmentManager, imageListSheet.tag)
            }
            chatView.typingEvent = {
                viewModel.sendTypingEvent()
            }
            chatView.loadNextPage = {
                viewModel.loadNextPage()
            }
            imageListSheet.itemClick = {
                imageListSheet.dismiss()
                val fragment = ChatAddImageFragment(user, it)
                open(fragment, binding.container)

                fragment.closeAction = {
                    close(fragment, binding.container) {
                        reDrawBars()
                    }
                }
            }
        }
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
    }

    override var customBackNavigation: Boolean = true

    override fun goBack() {
        viewModel.clearMessages()
        super.goBack()
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
}