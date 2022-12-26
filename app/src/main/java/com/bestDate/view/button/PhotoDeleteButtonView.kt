package com.bestDate.view.button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewPhotoDeleteButtonBinding

class PhotoDeleteButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewPhotoDeleteButtonBinding =
        ViewPhotoDeleteButtonBinding.inflate(LayoutInflater.from(context), this)

    var onSafeClick: (() -> Unit)? = null

    init {
        binding.root.setOnSaveClickListener {
            onSafeClick?.invoke()
        }
    }

    fun toggleActionEnabled(enable: Boolean) {
        if (enable) {
            binding.image.visibility = View.INVISIBLE
            binding.root.isEnabled = false
            binding.progress.visibility = View.VISIBLE
        } else {
            binding.image.visibility = View.VISIBLE
            binding.root.isEnabled = true
            binding.progress.visibility = View.INVISIBLE
        }
    }
}