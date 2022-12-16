package com.bestDate.view.settings

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewSaveSettingsButtonBinding

class SaveSettingsButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewSaveSettingsButtonBinding =
        ViewSaveSettingsButtonBinding.inflate(LayoutInflater.from(context), this)

    private var isActive: Boolean = true
    var onClick: (() -> Unit)? = null

    init {
        setActive(false)

        binding.button.setOnSaveClickListener {
            if (isActive) onClick?.invoke()
        }
    }

    fun setActive(active: Boolean) {
        isActive = active
        binding.button.setTextColor(
            ContextCompat.getColor(
                context,
                if (active) R.color.white else R.color.bg_main
            )
        )
        binding.button.setBackgroundColor(
            ContextCompat.getColor(
                context,
                if (active) R.color.bg_pink else R.color.white_20
            )
        )
    }

    fun toggleActionEnabled(enable: Boolean) {
        if (enable) {
            binding.button.text = ""
            binding.button.isEnabled = false
            binding.progress.visibility = View.VISIBLE
        } else {
            binding.button.text = context.getString(R.string.save_personal_data)
            binding.button.isEnabled = true
            binding.progress.visibility = View.INVISIBLE
        }
    }
}