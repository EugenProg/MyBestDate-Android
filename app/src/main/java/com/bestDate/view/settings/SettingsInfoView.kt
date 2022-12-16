package com.bestDate.view.settings

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bestDate.R
import com.bestDate.data.extension.*
import com.bestDate.databinding.ViewSettingsInfoBinding

class SettingsInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewSettingsInfoBinding =
        ViewSettingsInfoBinding.inflate(LayoutInflater.from(context), this)
    var onClick: (() -> Unit)? = null
    var onTextChangeListener: ((String?) -> Unit)? = null

    init {
        setAttrs(attrs, R.styleable.SettingsInfoView) {
            binding.hint.text = it.getString(R.styleable.SettingsInfoView_settings_info_hint)
            binding.icon.setImageResource(
                it.getResourceId(
                    R.styleable.SettingsInfoView_settings_info_icon,
                    R.drawable.ic_settings_user
                )
            )
        }

        binding.root.setOnSaveClickListener {
            onClick?.invoke()
        }
    }

    fun setText(text: String?) {
        binding.input.text = text
        onTextChangeListener?.invoke(text)
        setDefault()
    }

    fun getText(): String {
        return binding.input.text.toString()
    }

    fun setError() {
        with(binding) {
            hint.setTextColor(ContextCompat.getColor(context, R.color.red))
            input.setTextColor(ContextCompat.getColor(context, R.color.red))
            icon.setColorFilter(ContextCompat.getColor(context, R.color.red))
        }
        this.animateError()
        context.vibratePhone()
    }

    private fun setDefault() {
        with(binding) {
            hint.setTextColor(ContextCompat.getColor(context, R.color.white_50))
            input.setTextColor(ContextCompat.getColor(context, R.color.white_80))
            icon.setColorFilter(ContextCompat.getColor(context, R.color.white_80))
        }
    }
}