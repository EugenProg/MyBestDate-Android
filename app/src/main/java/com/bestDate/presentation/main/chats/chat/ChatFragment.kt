package com.bestDate.presentation.main.chats.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.BackScreenType
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.FragmentChatBinding
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.view.chat.ChatStatusType
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : BaseVMFragment<FragmentChatBinding, ChatViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentChatBinding =
        { inflater, parent, attach -> FragmentChatBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<ChatViewModel> = ChatViewModel::class.java

    override val statusBarColor: Int = R.color.bg_main
    private val args by navArgs<ChatFragmentArgs>()
    private var user: ShortUserData? = null

    override fun onInit() {
        super.onInit()
        user = args.user
        setUserInfo()
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            backButton.onClick = {
                navController.popBackStack()
            }
            userBox.setOnSaveClickListener {
                if (user?.isBot() != true) {
                    if (args.backScreen == BackScreenType.ANOTHER_PROFILE) {
                        navController.popBackStack()
                    } else {
                        navController.navigate(
                            ChatFragmentDirections
                                .actionGlobalChatToAnotherProfile(user, BackScreenType.CHAT)
                        )
                    }
                }
            }
            chatView.sendClick = { text, parentId ->

            }
            chatView.editClick = { text, messageId ->

            }
            chatView.translateClick = {

            }
            chatView.showInvitationClick = {

            }
            chatView.addImageClick = {

            }
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
            if (user?.is_online == true) status.setStatus(ChatStatusType.ONLINE)
            else status.setStatus(ChatStatusType.LAST_ONLINE, user?.last_online_at)

            chatView.setUser(user)
        }
    }
}