package com.bestDate.view.match

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewMatchesDisabledBinding

class MatchesDisabledView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewMatchesDisabledBinding =
        ViewMatchesDisabledBinding.inflate(LayoutInflater.from(context), this)

    var clickAction: (() -> Unit)? = null

    init {
        binding.toSettingsBox.setOnSaveClickListener {
            clickAction?.invoke()
        }
    }
}