package com.bestDate.view.chat

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.*
import com.bestDate.databinding.ViewChatBinding
import com.bestDate.view.bottomSheet.chatActionsSheet.ChatActions

class ChatView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewChatBinding =
        ViewChatBinding.inflate(LayoutInflater.from(context), this)
    private var messageList: MutableList<Message>? = null
    private var parentMessage: Message? = null
    private var user: ShortUserData? = null
    private var editMode: Boolean = false
    private var adapter: ChatAdapter = ChatAdapter()
    private var scrollToEditedId: Int? = null

    var showInvitationClick: (() -> Unit)? = null
    var sendClick: ((text: String, parentId: Int?) -> Unit)? = null
    var editClick: ((text: String, messageId: Int?) -> Unit)? = null
    var translateClick: ((text: String) -> Unit)? = null
    var translateMessageClick: ((message: Message) -> Unit)? = null
    var returnMessageClick: ((message: Message) -> Unit)? = null
    var typingEvent: (() -> Unit)? = null
    var addImageClick: (() -> Unit)? = null
    var imageOpenClick: ((ChatImage?) -> Unit)? = null
    var openActionSheet: ((Message?, MutableList<ChatActions>) -> Unit)? = null
    var loadNextPage: (() -> Unit)? = null
    var goToSettings: (() -> Unit)? = null

    init {
        with(binding) {
            bottomPanelView.sendClick = {
                if (editMode) {
                    scrollToEditedId = parentMessage?.id
                    editClick?.invoke(it, parentMessage?.id)
                }
                else sendClick?.invoke(it, parentMessage?.id)
            }
            bottomPanelView.translateClick = {
                translateClick?.invoke(it)
            }
            bottomPanelView.addImageClick = {
                addImageClick?.invoke()
            }
            bottomPanelView.closeEditMode = {
                parentMessage = null
            }
            bottomPanelView.typingEvent = {
                typingEvent?.invoke()
            }
            topPanelView.goToClick = {
                showInvitationClick?.invoke()
            }
            chatInvitation.goToClick = {
                showInvitationClick?.invoke()
            }
            adapter.messageClick = {
                openActionSheet?.invoke(it, getActions(it))
            }
            adapter.translateClick = {
                scrollToEditedId = it.id
                translateMessageClick?.invoke(it)
            }
            adapter.returnClick = {
                scrollToEditedId = it.id
                returnMessageClick?.invoke(it)
            }
            adapter.imageLongClick = {
                openActionSheet?.invoke(it, getActions(it))
            }
            adapter.imageOpenClick = {
                imageOpenClick?.invoke(it)
            }
            adapter.loadMoreItems = {
                loadNextPage?.invoke()
            }
            chatClosedView.click = {
                goToSettings?.invoke()
            }

            messagesListView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            messagesListView.adapter = adapter
        }
    }

    fun setUser(user: ShortUserData?) {
        this.user = user
        setVisibility()
    }

    private fun getActions(message: Message?): MutableList<ChatActions> {
        return when (message?.viewType) {
            ChatItemType.MY_TEXT_MESSAGE -> ChatActions.values().toMutableList()
            ChatItemType.MY_IMAGE_MESSAGE -> {
                if (message.text.isNullOrBlank()) mutableListOf(ChatActions.REPLY, ChatActions.DELETE)
                else ChatActions.values().toMutableList()
            }
            ChatItemType.USER_TEXT_MESSAGE,
            ChatItemType.USER_IMAGE_MESSAGE -> mutableListOf(ChatActions.REPLY, ChatActions.COPY)
            else -> mutableListOf()
        }
    }

    private fun setVisibility() {
        with(binding) {
            topPanelView.setVisibility(user, messageList?.isNotEmpty())
            chatInvitation.setVisibility(user, messageList?.isNotEmpty())
            bottomPanelView.setUser(user)
        }
    }

    fun setMessages(messages: MutableList<Message>?, meta: Meta) {
        val scroll = adapter.meta?.current_page == meta.current_page
        messageList = messages
        if (user?.allow_chat == true || user?.isBot() == true) {
            adapter.meta = meta
            adapter.submitList(messageList) {
                if (scroll) {
                    var position = 0
                    if (scrollToEditedId != null) {
                        position = messages?.indexOfFirst { it.id == scrollToEditedId }.orZero
                        scrollToEditedId = null
                    }
                    binding.messagesListView.scrollToPosition(position)
                }
            }
        }
        setVisibility()
    }

    fun setEditMode(message: Message?) {
        parentMessage = message
        binding.bottomPanelView.setEditMode(message?.text)
        editMode = true
    }

    fun setReplyMode(message: Message?) {
        parentMessage = message
        binding.bottomPanelView.setReplyMode(message?.text)
    }

    fun stopSendLoading() {
        binding.bottomPanelView.toggleSendLoading(false)
        binding.bottomPanelView.clearInput()
        parentMessage = null
        editMode = false
    }

    fun stopTranslateLoading() {
        binding.bottomPanelView.toggleTranslateLoading(false)
    }

    fun setTranslatedText(text: String?) {
        binding.bottomPanelView.setTranslatedText(text)
    }

    fun showInvitationView(show: Boolean) {
        binding.chatInvitation.isVisible = show && messageList?.isEmpty() == true
    }

    fun setChatClosed(closed: Boolean) {
        if (user?.allow_chat != true || user?.isBot() == true || messageList == null) return
        if (messageList?.isEmpty() != true) {
            binding.chatClosedView.isVisible = closed
        }
    }
}