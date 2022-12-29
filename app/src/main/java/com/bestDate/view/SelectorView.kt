package com.bestDate.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewSelectorBinding
import com.bestDate.presentation.registration.Gender

class SelectorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewSelectorBinding =
        ViewSelectorBinding.inflate(LayoutInflater.from(context), this)

    var onClick: (() -> Unit)? = null

    init {
        binding.root.setOnSaveClickListener {
            isActive = true
            onClick?.invoke()
        }
    }

    var isActive: Boolean = false
        set(value) {
            if (value) {
                binding.isActiveImageView.visibility = View.VISIBLE
            } else {
                binding.isActiveImageView.visibility = View.INVISIBLE
            }
            binding.textView.setTextColor(
                ContextCompat.getColor(
                    context,
                    if (value) R.color.white else R.color.white_50
                )
            )
            field = value
        }

    var isMan: Boolean = false
        set(value) {
            binding.textView.text =
                binding.root.context.getString(if (value) Gender.MAN.label else Gender.WOMAN.label)
            field = value
        }
}