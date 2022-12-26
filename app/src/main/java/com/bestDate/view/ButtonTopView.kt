package com.bestDate.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewButtonTopBinding

class ButtonTopView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewButtonTopBinding =
        ViewButtonTopBinding.inflate(LayoutInflater.from(context), this)

    var onClick: (() -> Unit)? = null
        set(value) {
            binding.root.setOnSaveClickListener { onClick?.invoke() }
            field = value
        }
}