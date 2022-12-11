package com.bestDate.view.button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewSignOutButtonBinding

class SignOutButtonView  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewSignOutButtonBinding =
        ViewSignOutButtonBinding.inflate(LayoutInflater.from(context), this)
    var onClick: (() -> Unit)? = null

    init {
        binding.root.setOnSaveClickListener {
            onClick?.invoke()
        }
    }

    fun toggleActionEnabled(enable: Boolean) {
        if (enable) {
            binding.icon.visibility = View.INVISIBLE
            binding.title.visibility = View.INVISIBLE
            binding.progress.visibility = View.VISIBLE
        } else {
            binding.icon.visibility = View.VISIBLE
            binding.title.visibility = View.VISIBLE
            binding.progress.visibility = View.GONE
        }
    }
}