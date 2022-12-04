package com.bestDate.view.anotherProfile

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewAnotherProfileBottomBoxBinding

class AnotherProfileBottomBoxView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewAnotherProfileBottomBoxBinding =
        ViewAnotherProfileBottomBoxBinding.inflate(LayoutInflater.from(context), this)
    var chatClick: (() -> Unit)? = null
    var cardClick: (() -> Unit)? = null
    var likeClick: (() -> Unit)? = null

    init {
        with(binding) {
            chatBox.setOnSaveClickListener {
                chatClick?.invoke()
            }
            cardBox.setOnSaveClickListener {
                cardClick?.invoke()
            }
            likeBox.setOnSaveClickListener {
                likeClick?.invoke()
            }
        }
    }
}