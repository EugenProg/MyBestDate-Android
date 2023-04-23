package com.bestDate.view.button

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.bestDate.R
import com.bestDate.view.base.BaseButton

class WhiteButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): BaseButton(context, attrs, defStyleAttr,
    buttonColor = R.color.white,
    textColor = R.color.bg_main) {

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.button.setTextColor(ContextCompat.getColor(context,
            if (enabled) R.color.bg_main else R.color.main_10)
        )
        binding.button.isEnabled = enabled
    }
}

class BlackButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): BaseButton(context, attrs, defStyleAttr,
    buttonColor = R.color.bg_main,
    textColor = R.color.white) {

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.button.setTextColor(ContextCompat.getColor(context,
            if (enabled) R.color.white else R.color.white_10)
        )
        binding.button.isEnabled = enabled
    }
}

class BlueButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): BaseButton(context, attrs, defStyleAttr,
    buttonColor = R.color.bg_light_blue,
    textColor = R.color.bg_main) {

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.button.setTextColor(ContextCompat.getColor(context,
            if (enabled) R.color.bg_main else R.color.main_10)
        )
        binding.button.isEnabled = enabled
    }
}

class PinkButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): BaseButton(context, attrs, defStyleAttr,
    buttonColor = R.color.bg_pink,
    textColor = R.color.bg_main) {

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.button.setTextColor(ContextCompat.getColor(context,
            if (enabled) R.color.bg_main else R.color.main_10)
        )
        binding.button.isEnabled = enabled
    }
}
