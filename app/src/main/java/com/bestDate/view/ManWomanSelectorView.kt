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
    var lookFor: String? = ""
        set(value) {
            binding.manSelector.isActive =
                value == binding.root.context.getString(Gender.MAN.gender)
            binding.womanSelector.isActive =
                value == binding.root.context.getString(Gender.WOMAN.gender)
            field = value
        }

    var onClick: ((Gender) -> Unit)? = null
        set(value) {
            binding.manSelector.isMan = true
            binding.womanSelector.isMan = false

            binding.manSelector.onClick = {
                binding.womanSelector.isActive = false
                value?.invoke(Gender.MAN)

            }
            binding.womanSelector.onClick = {
                binding.manSelector.isActive = false
                value?.invoke(Gender.WOMAN)
            }
            field = value
        }
}