package com.bestDate.view.button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bestDate.R
import com.bestDate.data.extension.setAttrs
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewDirectionLineButtonBinding

class DirectionLineButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewDirectionLineButtonBinding = ViewDirectionLineButtonBinding.inflate(
        LayoutInflater.from(context), this
    )

    var click: (() -> Unit)? = null

    init {
        setAttrs(attrs, R.styleable.DirectionLineButtonView) {
            binding.buttonBackground.setBackgroundResource(
                it.getResourceId(
                    R.styleable.DirectionLineButtonView_button_background,
                    R.drawable.buttons_box
                )
            )
            binding.icon.setImageResource(
                it.getResourceId(
                    R.styleable.DirectionLineButtonView_button_image,
                    R.drawable.ic_profile
                )
            )
            binding.title.text = it.getString(R.styleable.DirectionLineButtonView_button_name)
        }

        binding.root.setOnSaveClickListener {
            click?.invoke()
        }
    }
}