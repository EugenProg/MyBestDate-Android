package com.bestDate.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bestDate.R
import com.bestDate.data.extension.setAttrs
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewToolbarBinding

class ToolbarView  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {

    private val binding = ViewToolbarBinding.inflate(LayoutInflater.from(context), this)
    var backClick: (() -> Unit)? = null
    var additionalClick: (() -> Unit)? = null

    init {
        setAttrs(attrs, R.styleable.ToolbarView) {
            binding.title.text = it.getString(R.styleable.ToolbarView_toolbar_title)
            binding.additionalAction.visibility =
                if (it.getBoolean(R.styleable.ToolbarView_toolbar_action_visible, false)) VISIBLE
                else INVISIBLE
        }

        binding.back.onClick = {
            backClick?.invoke()
        }

        binding.additionalAction.setOnSaveClickListener {
            additionalClick?.invoke()
        }
    }
}