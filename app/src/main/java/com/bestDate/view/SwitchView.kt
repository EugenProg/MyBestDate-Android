package com.bestDate.view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewStandardInfoBinding
import com.bestDate.databinding.ViewSwitchBinding

class SwitchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: ViewSwitchBinding
    var onInfoClick: (() -> Unit)? = null
    var onChecked: ((checked: Boolean) -> Unit)? = null

    init {
        val view = View.inflate(context, R.layout.view_switch, this)
        binding = ViewSwitchBinding.bind(view)

        binding.info.setOnSaveClickListener { onInfoClick?.invoke() }

        binding.switchCompat.isChecked = false
        binding.switchCompat.setOnCheckedChangeListener { _, checked ->
            setTrackColor(checked)
            onChecked?.invoke(checked)
        }
    }

    var hint: String
        get() = binding.placeholder.text.toString()
        set(value) {
            binding.placeholder.text = value
        }

    var text: String
        get() = binding.name.text.toString()
        set(value) {
            binding.name.text = value
        }

    private fun setTrackColor(checked: Boolean) {
        val color = ContextCompat.getColor(context,
            if (checked) R.color.bg_light_blue else R.color.bg_pink)
        binding.switchCompat.trackTintList = ColorStateList.valueOf(color)
        binding.root.setBackgroundResource(
            if (checked) R.drawable.blue_input_shape else R.drawable.default_input_shape)
    }
}