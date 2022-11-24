package com.bestDate.view.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bestDate.databinding.ViewToolbarProfileBinding

class ToolbarProfile @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: ViewToolbarProfileBinding =
        ViewToolbarProfileBinding.inflate(LayoutInflater.from(context), this)

    var title: String = ""
        set(value) {
            binding.pageTitleTextView.text = value
            field = value
        }

}