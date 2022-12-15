package com.bestDate.view.settings

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bestDate.R
import com.bestDate.data.extension.setAttrs
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewSettingsButtonBinding

class SettingsButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewSettingsButtonBinding = ViewSettingsButtonBinding.inflate(
        LayoutInflater.from(context), this)
    var onClick: (() -> Unit)? = null

    init {
        setAttrs(attrs, R.styleable.SettingsButtonView) {
            binding.button.text = it.getString(R.styleable.SettingsButtonView_settings_button_title)
        }

        binding.button.setOnSaveClickListener {
            onClick?.invoke()
        }
    }

    var title: String
        get() = binding.button.text.toString()
        set(value) {
            binding.button.text = value
        }


}