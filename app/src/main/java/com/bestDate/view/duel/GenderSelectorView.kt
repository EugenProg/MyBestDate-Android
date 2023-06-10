package com.bestDate.view.duel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bestDate.databinding.ViewGenderSelectorBinding
import com.bestDate.presentation.registration.Gender

class GenderSelectorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: ViewGenderSelectorBinding =
        ViewGenderSelectorBinding.inflate(LayoutInflater.from(context), this)

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