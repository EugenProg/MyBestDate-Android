package com.bestDate.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bestDate.R
import com.bestDate.databinding.SocialViewBinding

class SocialView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: SocialViewBinding

    var facebookClick: (() -> Unit)? = null
    var appleClick: (() -> Unit)? = null
    var googleClick: (() -> Unit)? = null

    init {
        View.inflate(context, R.layout.social_view, this)
        binding = SocialViewBinding.bind(this)

        binding.apple.setOnClickListener {
            appleClick?.invoke()
        }
        binding.facebook.setOnClickListener {
            facebookClick?.invoke()
        }
        binding.google.setOnClickListener {
            googleClick?.invoke()
        }
    }
}