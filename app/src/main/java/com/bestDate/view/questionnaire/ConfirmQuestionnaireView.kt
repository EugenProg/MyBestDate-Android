package com.bestDate.view.questionnaire

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewConfirmationQuestionnaireBinding

class ConfirmQuestionnaireView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewConfirmationQuestionnaireBinding =
        ViewConfirmationQuestionnaireBinding.inflate(LayoutInflater.from(context), this)
    private var itemIsConfirmed: Boolean = false
    var itemClick: (() -> Unit)? = null

    init {
        binding.root.setOnSaveClickListener { itemClick?.invoke() }
    }

    var title: String
        get() = binding.title.toString()
        set(value) {
            binding.title.text = value
        }

    var description: String?
        get() = binding.description.toString()
        set(value) {
            binding.description.text = value
            binding.description.isVisible = !value.isNullOrBlank()
        }

    var isConfirm: Boolean
        get() = itemIsConfirmed
        set(value) {
            itemIsConfirmed = value
            binding.confirmedImage.isVerified = isConfirm
        }
}