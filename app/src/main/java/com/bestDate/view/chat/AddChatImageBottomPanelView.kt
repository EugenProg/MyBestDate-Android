package com.bestDate.view.chat

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.bestDate.data.extension.hideKeyboard
import com.bestDate.data.extension.orZero
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewAddChatImageBottomPanelBinding

class AddChatImageBottomPanelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewAddChatImageBottomPanelBinding =
        ViewAddChatImageBottomPanelBinding.inflate(LayoutInflater.from(context), this)
    var sendClick: ((text: String?) -> Unit)? = null
    var translateClick: ((text: String) -> Unit)? = null

    init {
        with(binding) {
            sendButton.setOnSaveClickListener {
                val text = messageInput.text
                toggleSendLoading(true)
                hideKeyboard()
                sendClick?.invoke(text.toString())
            }
            translateButton.setOnSaveClickListener {
                val text = messageInput.text
                if (text.isNotBlank()) {
                    toggleTranslateLoading(true)
                    translateClick?.invoke(text.toString())
                }
            }
        }
    }

    fun clearInput() {
        binding.messageInput.setText("")
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
        binding.messageInput.setSelection(text?.length.orZero)
        toggleTranslateLoading(false)
    }
}