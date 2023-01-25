package com.bestDate.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bestDate.databinding.ViewManWomanSelectorBinding
import com.bestDate.presentation.registration.Gender

class ManWomanSelectorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewManWomanSelectorBinding =
        ViewManWomanSelectorBinding.inflate(LayoutInflater.from(context), this)

    init {
        with(binding) {
            manSelector.gender = Gender.MAN
            womanSelector.gender = Gender.WOMAN

            manSelector.onClick = {
                binding.womanSelector.isActive = false
                onClick?.invoke(it)

            }
            womanSelector.onClick = {
                binding.manSelector.isActive = false
                onClick?.invoke(it)
            }
        }
    }

    fun setGender(gender: Gender) {
        with(binding) {
            manSelector.isActive = gender == Gender.MAN
            womanSelector.isActive = gender == Gender.WOMAN
        }
    }

    var onClick: ((Gender) -> Unit)? = null
}