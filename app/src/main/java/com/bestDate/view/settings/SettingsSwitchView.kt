package com.bestDate.view.settings

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.setAttrs
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewSettingsSwitchBinding

class SettingsSwitchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewSettingsSwitchBinding =
        ViewSettingsSwitchBinding.inflate(LayoutInflater.from(context), this)
    private var progressMode: Boolean = false

    var checkAction: ((checked: Boolean) -> Unit)? = null

    init {
        setAttrs(attrs, R.styleable.SettingsSwitchView) {
            binding.icon.setImageResource(it.getResourceId(
                R.styleable.SettingsSwitchView_settings_switch_icon, R.drawable.ic_settings_message))
            binding.title.text = it.getString(R.styleable.SettingsSwitchView_settings_switch_title)
        }

        with(binding) {
            root.setOnClickListener {
                if (!progressMode) {
                    switchCompat.isChecked = !switchCompat.isChecked
                    toggleSwitchStatus(binding.switchCompat.isChecked)
                }
            }
        }
    }

    var title: String?
    get() = binding.title.text.toString()
    set(value) {
        binding.title.text = value
    }

    @DrawableRes var icon: Int? = R.drawable.ic_settings_message
    set(value) {
        value?.let {
            binding.icon.setImageResource(it)
        }
        field = value
    }

    fun toggleActionEnabled(enable: Boolean) {
        progressMode = enable
        binding.switchCompat.isVisible = !enable
        binding.progress.isVisible = enable
    }

    fun setChecked(isChecked: Boolean) {
        binding.switchCompat.isChecked = isChecked
    }

    private fun toggleSwitchStatus(active: Boolean) {
        with(binding) {
            status.text = context.getString(if (active) R.string.active else R.string.deactive)
            val color = ContextCompat.getColor(context,
                if (active) R.color.bg_light_blue else R.color.bg_pink)
            status.setTextColor(color)
            switchCompat.trackTintList = ColorStateList.valueOf(color)
            status.alpha = 0.9f
        }
    }
}