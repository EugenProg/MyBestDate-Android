package com.bestDate.view.base

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewButtonBinding

abstract class BaseButton (context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
    private val buttonColor: Int, private val textColor: Int, private val progressColor: Int):
    ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: ViewButtonBinding
    private var buttonTitle: String = ""
    var onClick: (() -> Unit)? = null

    init {
        val view = View.inflate(context, R.layout.view_button, this)
        binding = ViewButtonBinding.bind(view)

        setButtonColor(context)

        binding.button.setOnSaveClickListener {
            onClick?.invoke()
        }
    }

    private fun setButtonColor(context: Context) {
        binding.button.setBackgroundColor(ContextCompat.getColor(context, buttonColor))
        binding.button.setTextColor(ContextCompat.getColor(context, textColor))
        binding.progress.indeterminateDrawable.colorFilter =
            PorterDuffColorFilter(
                ContextCompat.getColor(context, progressColor),
                PorterDuff.Mode.MULTIPLY
            )
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
}