package com.bestDate.view.questionnaire

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewConfirmationQuestionnaireBinding

class ConfirmQuestionnaireView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewConfirmationQuestionnaireBinding
    private var itemIsConfirmed: Boolean = false
    var itemClick: (() -> Unit)? = null

    init {
        val view = View.inflate(context, R.layout.view_confirmation_questionnaire, this)
        binding = ViewConfirmationQuestionnaireBinding.bind(view)

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
        setConfirmationColor(value)
    }

    private fun setConfirmationColor(confirm: Boolean) {
        val color = ContextCompat.getColor(context,
            if (confirm) R.color.bg_pink else R.color.main_20_without_opacity
        )
        binding.confirmedImage.setColorFilter(color)
        binding.confirmedText.isVisible = confirm
    }
}