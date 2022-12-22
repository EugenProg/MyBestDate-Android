package com.bestDate.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewSelectorBinding

class SelectorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewSelectorBinding =
        ViewSelectorBinding.inflate(LayoutInflater.from(context), this)

    var onClick: (() -> Unit)? = null

    init {
        binding.root.setOnSaveClickListener {
            onClick?.invoke()
        }
    }

    var isActive: Boolean = false
        set(value) {
            binding.isActiveImageView.isVisible = value
            binding.textView.setTextColor(
                ContextCompat.getColor(
                    context,
                    if (value) R.color.white else R.color.white_50
                )
            )
            field = value
        }

    var label: String = ""
        set(value) {
            binding.textView.text = value
            field = value
        }
}