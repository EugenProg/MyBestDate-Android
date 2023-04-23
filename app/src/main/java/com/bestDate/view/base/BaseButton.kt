package com.bestDate.view.base

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bestDate.R
import com.bestDate.data.extension.setAttrs
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewButtonBinding

abstract class BaseButton (context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
    private val buttonColor: Int, private val textColor: Int):
    ConstraintLayout(context, attrs, defStyleAttr) {

    protected var binding: ViewButtonBinding = ViewButtonBinding
        .inflate(LayoutInflater.from(context), this)
    private var buttonTitle: String = ""
    var onClick: (() -> Unit)? = null
    var onSafeClick: (() -> Unit)? = null

    init {
        setAttrs(attrs, R.styleable.BaseButton) {
            title = it.getString(R.styleable.BaseButton_title).orEmpty()
        }

        setButtonColor(context)

        binding.button.setOnSaveClickListener {
            onSafeClick?.invoke()
            onClick?.invoke()
        }
    }

    private fun setButtonColor(context: Context) {
        binding.button.setBackgroundColor(ContextCompat.getColor(context, buttonColor))
        binding.button.setTextColor(ContextCompat.getColor(context, textColor))
    }

    var title: String
        get() = binding.button.text.toString()
        set(value) {
            buttonTitle = value
            binding.button.text = value
        }

    fun toggleActionEnabled(enable: Boolean) {
        if (enable) {
            binding.button.text = ""
            binding.button.isEnabled = false
            binding.progress.visibility = View.VISIBLE
        } else {
            binding.button.text = buttonTitle
            binding.button.isEnabled = true
            binding.progress.visibility = View.INVISIBLE
        }
    }

    var loaderVisibility: Boolean = true
        set(value) {
        field = value
        binding.progress.visibility = if (value) View.VISIBLE else View.INVISIBLE
    }
}