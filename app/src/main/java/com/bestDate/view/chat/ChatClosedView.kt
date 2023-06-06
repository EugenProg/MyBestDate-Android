package com.bestDate.view.chat

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewChatClosedBinding

class ChatClosedView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewChatClosedBinding =
        ViewChatClosedBinding.inflate(LayoutInflater.from(context), this)

    var click: (() -> Unit)? = null

    init {
        binding.toSettingsBox.setOnSaveClickListener {
            click?.invoke()
        }
    }
}