package com.bestDate.view.bottomNav

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.extension.toPx
import com.bestDate.databinding.ViewBottomNavButtonBinding

class CustomBottomNavButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewBottomNavButtonBinding =
        ViewBottomNavButtonBinding.inflate(LayoutInflater.from(context), this)

    var onClick: (() -> Unit)? = null
    var onNavigationChange: (() -> Unit)? = null

    var isActive: Boolean = false
        set(value) {
            binding.activeImageView.isVisible = value
            binding.labelTextView.isVisible = !value
            if (value) {
                binding.iconImageView.setImageResource(iconActive)
            } else {
                binding.iconImageView.setImageResource(icon)
            }
            field = value
        }

    var icon: Int = 0
        set(value) {
            if (!isActive) {
                setImage(value)
            }
            field = value
        }

    var iconActive: Int = 0
        set(value) {
            if (isActive) {
                setImage(value)
            }
            field = value
        }

    var label: String = ""
        set(value) {
            binding.labelTextView.text = value
            field = value
        }

    var hasBadge: Boolean = false
        set(value) {
            binding.viewBadge.isVisible = value
            field = value
        }

    init {
        binding.root.setOnSaveClickListener {
            if (!isActive) {
                onClick?.invoke()
                onNavigationChange?.invoke()
            }
        }
    }

    private fun setImage(value: Int) {
        binding.iconImageView.setImageResource(value)
        binding.iconImageView.layoutParams.width =
            if (value == R.drawable.ic_matches) 28.toPx() else 20.toPx()
        binding.iconImageView.layoutParams.height =
            if (value == R.drawable.ic_matches) 28.toPx() else 20.toPx()

        if (value == R.drawable.ic_matches) {
            (binding.vgButton.layoutParams as LayoutParams).topMargin = -(4.toPx())
        }
    }
}