package com.bestDate.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ViewResultProfileBinding

class ResultProfileView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewResultProfileBinding =
        ViewResultProfileBinding.inflate(LayoutInflater.from(context), this)

    var profile: ShortUserData = ShortUserData()
        set(value) {
            binding.barView.percent = 70.0
            field = value
        }
}