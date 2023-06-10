package com.bestDate.view.duel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewMyDuelButtonBinding

class MyDuelsButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewMyDuelButtonBinding =
        ViewMyDuelButtonBinding.inflate(LayoutInflater.from(context), this)

    var click: (() -> Unit)? = null

    init {
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