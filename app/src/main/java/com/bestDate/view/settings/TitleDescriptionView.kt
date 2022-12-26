package com.bestDate.view.settings

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bestDate.R
import com.bestDate.data.extension.setAttrs
import com.bestDate.databinding.ViewTitleDescriptionBinding

class TitleDescriptionView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewTitleDescriptionBinding = ViewTitleDescriptionBinding.inflate(
        LayoutInflater.from(context), this)

    init {
        setAttrs(attrs, R.styleable.TitleDescriptionView) {
            binding.title.text = it.getString(R.styleable.TitleDescriptionView_title_description_title)
            binding.description.text = it.getString(R.styleable.TitleDescriptionView_title_description_description)
        }
    }

    var title: String?
    get() = binding.title.text.toString()
    set(value) {
        binding.title.text = value
    }

    var description: String?
    get() = binding.description.text.toString()
    set(value) {
        binding.description.text = value
    }
}