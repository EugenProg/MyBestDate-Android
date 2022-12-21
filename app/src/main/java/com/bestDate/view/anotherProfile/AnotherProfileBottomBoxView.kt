package com.bestDate.view.anotherProfile

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.fadeInsert
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewAnotherProfileBottomBoxBinding

class AnotherProfileBottomBoxView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewAnotherProfileBottomBoxBinding =
        ViewAnotherProfileBottomBoxBinding.inflate(LayoutInflater.from(context), this)
    var chatClick: (() -> Unit)? = null
    var cardClick: (() -> Unit)? = null
    var likeClick: (() -> Unit)? = null

    var isLiked: Boolean = false
        set(value) {
            field = value
            binding.heart.fadeInsert {
                binding.heart.setImageResource(if (value) R.drawable.ic_nav_like_pink else R.drawable.ic_nav_like_blue)
            }
        }

    init {
        with(binding) {
            chatBox.setOnSaveClickListener {
                chatClick?.invoke()
            }
            cardBox.setOnSaveClickListener {
                cardClick?.invoke()
            }
            likeBox.setOnSaveClickListener {
                animationView.isVisible = true
                animationView.playAnimation()
                likeClick?.invoke()
                postDelayed({
                    animationView.isVisible = false
                    animationView.pauseAnimation()
                }, animationView.duration)
            }
        }
    }
}