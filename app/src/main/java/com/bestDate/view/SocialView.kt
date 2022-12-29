package com.bestDate.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bestDate.databinding.ViewSocialBinding

class SocialView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewSocialBinding =
        ViewSocialBinding.inflate(LayoutInflater.from(context), this)

    var facebookClick: (() -> Unit)? = null
    var googleClick: (() -> Unit)? = null

    init {
        binding.facebook.setOnClickListener {
            facebookClick?.invoke()
        }
        binding.google.setOnClickListener {
            googleClick?.invoke()
        }
    }
}