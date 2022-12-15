package com.bestDate.view.button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewInvitationListNavigationBoxBinding

class InvitationListNavigationBoxView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewInvitationListNavigationBoxBinding =
        ViewInvitationListNavigationBoxBinding.inflate(LayoutInflater.from(context), this)
    var onClick: ((InvitationActions) -> Unit)? = null

    private var notActiveColor = ContextCompat.getColor(context, R.color.white_50)
    private var activeColor = ContextCompat.getColor(context, R.color.white)

    init {
        setNewBtn()
        binding.newBtn.setOnSaveClickListener {
            setNewBtn()
            onClick?.invoke(InvitationActions.NEW)
        }
        binding.answeredBtn.setOnSaveClickListener {
            setAnsweredBtn()
            onClick?.invoke(InvitationActions.ANSWERED)
        }
        binding.sentBtn.setOnSaveClickListener {
            setSentBtn()
            onClick?.invoke(InvitationActions.SENT)
        }
    }

    fun setNewBtn() {
        with(binding) {
            newBtn.setTextColor(activeColor)
            answeredBtn.setTextColor(notActiveColor)
            sentBtn.setTextColor(notActiveColor)
        }
    }

    fun setAnsweredBtn() {
        with(binding) {
            newBtn.setTextColor(notActiveColor)
            answeredBtn.setTextColor(activeColor)
            sentBtn.setTextColor(notActiveColor)
        }
    }

    fun setSentBtn() {
        with(binding) {
            newBtn.setTextColor(notActiveColor)
            answeredBtn.setTextColor(notActiveColor)
            sentBtn.setTextColor(activeColor)
        }
    }
}

enum class InvitationActions {
    NEW, ANSWERED, SENT
}