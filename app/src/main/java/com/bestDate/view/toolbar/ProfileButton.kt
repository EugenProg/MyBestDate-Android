package com.bestDate.view.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewProfileButtonBinding
import com.bumptech.glide.Glide

class ProfileButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: ViewProfileButtonBinding =
        ViewProfileButtonBinding.inflate(LayoutInflater.from(context), this)

    var photo: String? = ""
        set(value) {
            Glide.with(binding.root.context)
                .load(value)
                .circleCrop()
                .into(binding.profileImageView)
            field = value
        }

    var onProfileClick: (() -> Unit)? = null
        set(value) {
            binding.profileImageView.setOnSaveClickListener {
                value?.invoke()
            }
            field = value
        }
}