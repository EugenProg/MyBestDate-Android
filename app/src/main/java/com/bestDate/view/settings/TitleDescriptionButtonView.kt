package com.bestDate.view.settings

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bestDate.R
import com.bestDate.data.extension.setAttrs
import com.bestDate.databinding.ViewTitleDescriptionButtonBinding

class TitleDescriptionButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewTitleDescriptionButtonBinding =
        ViewTitleDescriptionButtonBinding.inflate(LayoutInflater.from(context), this)

    var onClick: (() -> Unit)? = null

    init {
        setAttrs(attrs, R.styleable.TitleDescriptionButtonView) {
            binding.titleDescription.title = it.getString(R.styleable.TitleDescriptionButtonView_t_d_b_title)
            binding.titleDescription.description = it.getString(R.styleable.TitleDescriptionButtonView_t_d_b_description)
            binding.button.title = it.getString(R.styleable.TitleDescriptionButtonView_t_d_b_button_title)
        }

        binding.button.onClick = {
            onClick?.invoke()
        }
    }

    var buttonTitle: String?
    get() = binding.button.title
    set(value) {
        binding.button.title = value
    }
}