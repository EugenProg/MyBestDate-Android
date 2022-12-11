package com.bestDate.view.button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.setAttrs
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewUserDataButtonBinding

class UserDataButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewUserDataButtonBinding = ViewUserDataButtonBinding.inflate(
        LayoutInflater.from(context), this
    )

    var click: (() -> Unit)? = null

    init {
        setAttrs(attrs, R.styleable.UserDataButtonView) {
            binding.title.text = it.getString(R.styleable.UserDataButtonView_button_title)
            binding.icon.setImageResource(
                it.getResourceId(
                    R.styleable.UserDataButtonView_button_icon,
                    R.drawable.ic_my_duel
                )
            )
        }

        binding.root.setOnSaveClickListener {
            click?.invoke()
        }
    }

    var badgeOn: Boolean
        get() = binding.badge.isVisible
        set(value) {
            binding.badge.isVisible = value
        }
}