package com.bestDate.view.chat

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.bestDate.R
import com.bestDate.data.extension.getVisitPeriod
import com.bestDate.databinding.ViewChatStatusBinding

class ChatStatusView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewChatStatusBinding =
        ViewChatStatusBinding.inflate(LayoutInflater.from(context), this)
    private var statusType = ChatStatusType.LAST_ONLINE

    fun setStatus(statusType: ChatStatusType, lastOnline: String? = null) {
        this.statusType = statusType
        binding.statusText.setTextColor(ContextCompat.getColor(context, statusType.color))
        setStatusText(lastOnline)
    }

    private fun setStatusText(lastOnline: String?) {
        binding.statusText.text =
            when(statusType) {
                ChatStatusType.ONLINE -> context.getString(R.string.online)
                ChatStatusType.TYPING -> context.getString(R.string.typing)
                ChatStatusType.LAST_ONLINE ->
                    context.getString(R.string.last_visit, lastOnline.getVisitPeriod(context))
            }
    }
}

enum class ChatStatusType(@ColorRes val color: Int) {
    ONLINE(R.color.green),
    TYPING(R.color.green),
    LAST_ONLINE(R.color.white_80)
}