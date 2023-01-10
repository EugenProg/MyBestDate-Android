package com.bestDate.view.chat

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.data.model.Message
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ViewChatBinding

class ChatView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewChatBinding =
        ViewChatBinding.inflate(LayoutInflater.from(context), this)
    private var messageList: MutableList<Message> = mutableListOf()
    private var parentMessage: Message? = null
    private var user: ShortUserData? = null
    private var editMode: Boolean = false
    private var adapter: ChatAdapter = ChatAdapter()

    var showInvitationClick: (() -> Unit)? = null
    var sendClick: ((text: String, parentId: Int?) -> Unit)? = null
    var editClick: ((text: String, messageId: Int?) -> Unit)? = null
    var translateClick: ((text: String) -> Unit)? = null
    var addImageClick: (() -> Unit)? = null

    init {
        with(binding) {
            bottomPanelView.sendClick = {
                if (editMode) editClick?.invoke(it, parentMessage?.id)
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
            topPanelView.goToClick = {
                showInvitationClick?.invoke()
            }
            chatInvitation.goToClick = {
                showInvitationClick?.invoke()
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

    private fun setVisibility() {
        with(binding) {
            topPanelView.setVisibility(user, messageList.isNotEmpty())
            chatInvitation.setVisibility(user, messageList.isNotEmpty())
            bottomPanelView.setUser(user)
        }
    }

    fun setMessages(messages: MutableList<Message>?) {
        messageList = messages ?: mutableListOf()
        adapter.submitList(messageList)
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
}