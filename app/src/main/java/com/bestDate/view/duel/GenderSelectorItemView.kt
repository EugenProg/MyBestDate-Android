package com.bestDate.view.duel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewGenderSelectorItemBinding
import com.bestDate.presentation.registration.Gender

class GenderSelectorItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewGenderSelectorItemBinding =
        ViewGenderSelectorItemBinding.inflate(LayoutInflater.from(context), this)

    var onClick: ((Gender) -> Unit)? = null

    init {
        binding.root.setOnSaveClickListener {
            if (!isActive) {
                isActive = true
                onClick?.invoke(gender)
            }
        }
    }

    var gender: Gender = Gender.WOMAN
        set(value) {
            binding.selector.text = value.smallLabel
            field = value
        }

    var isActive: Boolean = false
        set(value) {
            if (value) {
                binding.selector.setBackgroundResource(gender.smallBack)
            } else {
                binding.selector.setBackgroundResource(R.drawable.gender_default)
            }
            binding.selector.setTextColor(
                ContextCompat.getColor(
                    context,
                    if (value) R.color.white else R.color.white_30
                )
            )
            field = value
        }
}