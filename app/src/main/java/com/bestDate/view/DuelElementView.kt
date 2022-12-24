package com.bestDate.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewDuelElementBinding
import com.bumptech.glide.Glide

class DuelElementView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewDuelElementBinding =
        ViewDuelElementBinding.inflate(LayoutInflater.from(context), this)
    var likeClick: ((Int) -> Unit)? = null

    var image: String? = ""
        set(value) {
            Glide.with(binding.root.context)
                .load(value)
                .placeholder(R.drawable.ic_default_photo)
                .into(binding.profileImageView)
            field = value
        }

    var photoId: Int = 0

    init {
        binding.viewLike.setOnSaveClickListener {
            playHeartsAnim()
            likeClick?.invoke(photoId)
        }
    }

    private fun playHeartsAnim() {
        with(binding) {
            // if (!isLiked) {
            animationView.isVisible = true
            animationView.playAnimation()
            postDelayed({
                animationView.isVisible = false
                animationView.pauseAnimation()
            }, animationView.duration)
            //}
        }
    }
}