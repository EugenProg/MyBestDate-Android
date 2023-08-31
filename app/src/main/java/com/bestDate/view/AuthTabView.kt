package com.bestDate.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.bestDate.R
import com.bestDate.data.extension.setColorAnimated
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewAuthTabBinding

class AuthTabView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewAuthTabBinding =
        ViewAuthTabBinding.inflate(LayoutInflater.from(context), this)
    private var currentTab: AuthTabType = AuthTabType.EMAIL

    var itemChanged: ((AuthTabType) -> Unit)? = null

    init {
        with(binding) {
            setEmailTab()

            emailTab.setOnSaveClickListener {
                if (currentTab != AuthTabType.EMAIL) {
                    currentTab = AuthTabType.EMAIL
                    setEmailTab()
                }
            }
            phoneTab.setOnSaveClickListener {
                if (currentTab != AuthTabType.PHONE) {
                    currentTab = AuthTabType.PHONE
                    setPhoneTab()
                }
            }
        }
    }

    private fun setEmailTab() {
        with(binding) {
            emailTab.setColorAnimated(R.color.bg_main)
            phoneTab.setColorAnimated(R.color.white)

            shape.animate()
                .translationX(0f)
                .setDuration(300)
                .start()
        }
    }

    private fun setPhoneTab() {
        with(binding) {
            emailTab.setColorAnimated(R.color.white)
            phoneTab.setColorAnimated(R.color.bg_main)

            shape.animate()
                .translationX(shape.width.toFloat())
                .setDuration(300)
                .start()
        }
    }
}

enum class AuthTabType {
    EMAIL, PHONE
}