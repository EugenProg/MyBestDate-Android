package com.bestDate.view.filter

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.extension.setTextViewDrawableColor
import com.bestDate.databinding.ViewDecorationFilterButtonBinding
import com.bestDate.presentation.registration.Gender

class DecoratedFilterButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewDecorationFilterButtonBinding =
        ViewDecorationFilterButtonBinding.inflate(LayoutInflater.from(context), this)

    var onClick: (() -> Unit)? = null
    var country: String = context.getString(R.string.universe)

    var gender = Gender.WOMAN
        set(value) {
            val isWoman = value == Gender.WOMAN
            val textColor = if (isWoman) R.color.white else R.color.black
            val textInt = if (isWoman) R.string.miss_country else R.string.mister_country
            val decorationInt = if (isWoman) R.drawable.decor_button else R.drawable.decor_button_blue
            val backGroundColor = if (isWoman)  R.color.bg_pink else  R.color.decoration_blue

            binding.textView.setTextColor(ContextCompat.getColor(context, textColor))
            binding.textView.setTextViewDrawableColor(textColor)
            binding.textView.text =
                binding.root.context.getString(textInt, country)
            binding.decorationView.setBackgroundResource(decorationInt)
            binding.bgView.setBackgroundColor(ContextCompat.getColor(binding.root.context, backGroundColor))

            field = value
        }

    init {
        binding.root.setOnSaveClickListener {
            onClick?.invoke()
        }
        binding.decorationView.isVisible = true
    }
}