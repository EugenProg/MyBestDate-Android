package com.bestDate.view.anotherProfile

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bestDate.R
import com.bestDate.databinding.ViewImageLinePreBinding

class ImageLinePreView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: ViewImageLinePreBinding =
        ViewImageLinePreBinding.inflate(LayoutInflater.from(context), this)

    fun setImagesCount(count: Int) {
        binding.three.visibility = if (count >= 3) View.VISIBLE else View.INVISIBLE
        binding.two.visibility = if (count >= 2) View.VISIBLE else View.INVISIBLE
    }
}