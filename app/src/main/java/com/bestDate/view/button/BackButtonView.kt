package com.bestDate.view.button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.bestDate.R
import com.bestDate.data.extension.setAttrs
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.BackButtonViewBinding

class BackButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {

    private val binding = BackButtonViewBinding.inflate(LayoutInflater.from(context), this)
    var onClick: (() -> Unit)? = null

    init {
        setAttrs(attrs, R.styleable.BackButtonView) {
            val color = it.getColor(R.styleable.BackButtonView_back_button_color,
                ContextCompat.getColor(context, R.color.white))
            setColor(color)
        }

        binding.root.setOnSaveClickListener {
            onClick?.invoke()
        }
    }

    fun setColor(@ColorInt color: Int) {
        binding.text.setTextColor(color)
        binding.arrow.setColorFilter(color)
    }
}