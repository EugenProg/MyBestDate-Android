package com.bestDate.view.match

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.extension.toPx
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ViewMatchImageBinding
import com.bumptech.glide.Glide

class MatchImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewMatchImageBinding =
        ViewMatchImageBinding.inflate(LayoutInflater.from(context), this)
    private var user: ShortUserData? = null

    var closeAction: ((View?) -> Unit)? = null
    var matchAction: ((View?, Int?) -> Unit)? = null
    var clickAction: ((ShortUserData?) -> Unit)? = null
    private var clickRange = (-10f).toPx()..10f.toPx()

    init {
        with(binding) {
            closeBtn.setOnSaveClickListener {
                closeAnimation()
            }
            matchBtn.setOnSaveClickListener {
                matchAnimation()
            }
            setMoveListener()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setMoveListener() {
        var dx = 0.0f
        var dy = 0.0f
        binding.root.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    dx = view.x - motionEvent.rawX
                    dy = view.y - motionEvent.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    view.x = motionEvent.rawX + dx
                    val yPosition = motionEvent.rawY + dy
                    view.y = when {
                        yPosition < (-20f).toPx() -> (-20f).toPx()
                        yPosition > 150f.toPx() -> 150f.toPx()
                        else -> yPosition
                    }
                }
                MotionEvent.ACTION_UP -> {
                    val xDistance = motionEvent.rawX + dx
                    val yDistance = motionEvent.rawY + dy
                    when {
                        xDistance > 50f.toPx() -> matchAnimation()
                        xDistance < (-50f).toPx() -> closeAnimation()
                        xDistance in clickRange &&
                                yDistance in clickRange -> clickAction?.invoke(user)
                        else -> {
                            toStartPosition()
                        }
                    }
                }
                else -> {
                    toStartPosition()
                }
            }
            return@setOnTouchListener true
        }
    }

    private fun toStartPosition() {
        binding.root.animate()
            .translationX(0.0f)
            .translationY(0.0f)
            .setDuration(300)
            .start()
    }

    private fun matchAnimation() {
        binding.root.animate()
            .translationX(binding.root.width.toFloat() * 1.2f)
            .translationY(50f.toPx())
            .rotation(15f)
            .setDuration(300)
            .start()

        postDelayed({
            matchAction?.invoke(this, user?.id)
        }, 300)
    }

    private fun closeAnimation() {
        binding.root.animate()
            .translationX(-binding.root.width.toFloat() * 1.2f)
            .translationY(50f.toPx())
            .rotation(-15f)
            .setDuration(300)
            .start()

        postDelayed({
            closeAction?.invoke(this)
        }, 300)
    }

    fun setUser(user: ShortUserData?) {
        this.user = user
        user?.getMainPhoto()?.let {
            Glide.with(context)
                .load(it.thumb_url)
                .into(binding.avatarThumb)

            Glide.with(context)
                .load(it.full_url)
                .into(binding.avatar)
        }
    }
}