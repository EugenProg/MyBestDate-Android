package com.bestDate.view.chat

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ViewChatInvitationBinding

class ChatInvitationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewChatInvitationBinding =
        ViewChatInvitationBinding.inflate(LayoutInflater.from(context), this)
    var goToClick: (() -> Unit)? = null

    init {
        binding.invitationButton.setOnSaveClickListener {
            goToClick?.invoke()
        }
    }

    fun setVisibility(user: ShortUserData?, hasMessages: Boolean) {
        when {
            user?.isBot() == true -> binding.root.isVisible = false
            user?.allow_chat == false -> showOnlyCardsView()
            !hasMessages -> showNoMessagesView()
            else -> binding.root.isVisible = false
        }
    }

    private fun showNoMessagesView() {
        with(binding) {
            noMessagesView.isVisible = true
            onlyCardsTitle.isVisible = false
            onlyCardsText.isVisible = false
        }
    }

    private fun showOnlyCardsView() {
        with(binding) {
            noMessagesView.isVisible = false
            onlyCardsTitle.isVisible = true
            onlyCardsText.isVisible = true
        }
    }
}