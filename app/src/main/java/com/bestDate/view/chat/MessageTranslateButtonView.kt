package com.bestDate.view.chat

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.Message
import com.bestDate.databinding.ViewMessageTranslateButtonBinding

class MessageTranslateButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewMessageTranslateButtonBinding =
        ViewMessageTranslateButtonBinding.inflate(LayoutInflater.from(context), this)

    private var status: Message.TranslateStatus = Message.TranslateStatus.UN_ACTIVE

    var translateClick: (() -> Unit)? = null
    var returnClick: (() -> Unit)? = null

    init {
        binding.root.setOnSaveClickListener {
            if (status == Message.TranslateStatus.UN_ACTIVE) {
                setStatus(Message.TranslateStatus.LOADING)
                translateClick?.invoke()
            }
            if (status == Message.TranslateStatus.TRANSLATED) {
                setStatus(Message.TranslateStatus.UN_ACTIVE)
                returnClick?.invoke()
            }
        }
    }

    fun setStatus(status: Message.TranslateStatus) {
        this.status = status
        with(binding) {
            progress.isVisible = status == Message.TranslateStatus.LOADING

            translateImage.isVisible = status != Message.TranslateStatus.LOADING
            translateImage.setImageResource(
                if (status == Message.TranslateStatus.UN_ACTIVE) R.drawable.ic_translate
                else R.drawable.ic_translate_blue
            )
        }
    }
}