package com.bestDate.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bestDate.R
import com.bestDate.databinding.WhiteButtonViewBinding

class WhiteButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: WhiteButtonViewBinding
    var onClick: (() -> Unit)? = null

    init {
        View.inflate(context, R.layout.white_button_view, this)
        binding = WhiteButtonViewBinding.bind(this)

        binding.button.setOnClickListener {
            onClick?.invoke()
        }
    }

    var title: String
    get() = binding.button.text.toString()
    set(value) {
        binding.button.text = value
    }

    fun setActionEnabled() {

    }
}