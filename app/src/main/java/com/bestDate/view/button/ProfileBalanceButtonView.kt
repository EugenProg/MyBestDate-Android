package com.bestDate.view.button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.text.isDigitsOnly
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewProfileBalanceButtonBinding

class ProfileBalanceButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewProfileBalanceButtonBinding =
        ViewProfileBalanceButtonBinding.inflate(LayoutInflater.from(context), this)

    var click: (() -> Unit)? = null

    init {
        binding.root.setOnSaveClickListener {
            click?.invoke()
        }
    }

    var coinsCount: Int
    get() {
        val text = binding.coinCountView.text.toString()
        return if (text.isDigitsOnly()) text.toInt() else 0
    }
    set(value) {
        binding.coinCountView.text = value.toString()
    }
}