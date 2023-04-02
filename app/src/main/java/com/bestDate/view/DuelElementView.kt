package com.bestDate.view

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewDuelElementBinding
import com.bumptech.glide.Glide

class DuelElementView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewDuelElementBinding =
        ViewDuelElementBinding.inflate(LayoutInflater.from(context), this)
    var likeClick: (() -> Unit)? = null
    private var isLiked = false

    init {
        binding.viewLike.setOnSaveClickListener {
            playHeartsAnim()
            isLiked = true
            likeClick?.invoke()
        }
    }

    fun setBitmap(image: Bitmap?) {
        isLiked = false
        Glide.with(context)
            .load(image)
            .into(binding.profileImageView)
    }

    private fun playHeartsAnim() {
        with(binding) {
            if (!isLiked) {
                animationView.visibility = View.VISIBLE
                animationView.playAnimation()
                postDelayed({
                    animationView.visibility = View.INVISIBLE
                    animationView.pauseAnimation()
                }, animationView.duration)
            }
        }
    }
}