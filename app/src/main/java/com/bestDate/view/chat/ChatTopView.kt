package com.bestDate.view.chat

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ViewChatTopBinding

class ChatTopView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewChatTopBinding = 
        ViewChatTopBinding.inflate(LayoutInflater.from(context), this)
    var goToClick: (() -> Unit)? = null

    init {
        binding.root.isVisible = false
        binding.goToButton.setOnSaveClickListener {
            goToClick?.invoke()
        }
    }
    
    fun setVisibility(user: ShortUserData?, hasMessages: Boolean?) {
        binding.root.isVisible = hasMessages == true && user?.isBot() != true
    }
}