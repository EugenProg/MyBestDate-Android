package com.bestDate.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewDuelElementBinding

class DuelElementView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewDuelElementBinding =
        ViewDuelElementBinding.inflate(LayoutInflater.from(context), this)
    var likeClick: (() -> Unit)? = null

    init {
        binding.viewLike.setOnSaveClickListener {
            like()
        }
    }

    fun like() {
        playHeartsAnim()
        likeClick?.invoke()
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