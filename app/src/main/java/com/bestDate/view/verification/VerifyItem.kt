package com.bestDate.view.verification

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bestDate.R
import com.bestDate.databinding.ViewVerifyBinding

class VerifyItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewVerifyBinding =
        ViewVerifyBinding.inflate(LayoutInflater.from(context), this)

    var isVerified: Boolean = false
        set(value) {
            binding.verifyImageView.setImageResource(if (value) R.drawable.ic_verify_active else R.drawable.ic_verify)
            field = value
        }

}