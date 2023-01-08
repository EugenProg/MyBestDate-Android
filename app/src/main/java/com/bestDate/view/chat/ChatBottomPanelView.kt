package com.bestDate.view.chat

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ViewChatBottomPanelBinding
import com.bumptech.glide.Glide

class ChatBottomPanelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewChatBottomPanelBinding =
        ViewChatBottomPanelBinding.inflate(LayoutInflater.from(context), this)
    var sendClick: ((text: String) -> Unit)? = null
    var translateClick: ((text: String) -> Unit)? = null
    var addImageClick: (() -> Unit)? = null
    var closeEditMode: (() -> Unit)? = null

    init {
        with(binding) {
            sendButton.setOnSaveClickListener {
                val text = messageInput.text
                if (text.isNotBlank()) {
                    toggleSendLoading(true)
                    sendClick?.invoke(text.toString())
                    clearEditBox()
                }
            }
            translateButton.setOnSaveClickListener {
                val text = messageInput.text
                if (text.isNotBlank()) {
                    toggleTranslateLoading(true)
                    translateClick?.invoke(text.toString())
                }
            }
            imageButton.setOnSaveClickListener {
                addImageClick?.invoke()
            }
            editCloseBtn.setOnSaveClickListener {
                clearEditBox()
                closeEditMode?.invoke()
            }
        }
    }

    private fun clearEditBox() {
        with(binding) {
            editBox.isVisible = false
            editMessage.text = ""
        }
    }

    fun clearInput() {
        binding.messageInput.setText("")
    }

    fun setUser(user: ShortUserData?) {
        when {
            user?.isBot() == true -> binding.root.isVisible = false
            user?.allow_chat == false -> setOnlyCardsMode(user)
            else -> {
                binding.closedBox.isVisible = false
                binding.openBox.isVisible = true
            }
        }
    }

    fun setEditMode(message: String?) {
        with(binding) {
            editBox.isVisible = true
            editMessage.text = message
            editIcon.setImageResource(R.drawable.ic_edit)
            messageInput.setText(message)
        }
    }

    fun setReplyMode(message: String?) {
        with(binding) {
            editBox.isVisible = true
            editMessage.text = message
            editIcon.setImageResource(R.drawable.ic_reply_white)
        }
    }

    fun toggleSendLoading(enabled: Boolean) {
        binding.sendButton.isVisible = !enabled
        binding.sendLoading.isVisible = enabled
    }

    fun toggleTranslateLoading(enabled: Boolean) {
        binding.translateButton.isVisible = !enabled
        binding.translateLoading.isVisible = enabled
    }

    fun setTranslatedText(text: String?) {
        binding.messageInput.setText(text)
        toggleTranslateLoading(false)
    }

    private fun setOnlyCardsMode(user: ShortUserData?) {
        binding.closedBox.isVisible = true
        binding.openBox.isVisible = false
        Glide.with(context)
            .load(user?.getMainPhoto()?.thumb_url)
            .circleCrop()
            .into(binding.avatar)
    }
}