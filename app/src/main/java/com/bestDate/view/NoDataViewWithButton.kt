package com.bestDate.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.setAttrs
import com.bestDate.databinding.ViewNoDataWithButtonBinding

class NoDataViewWithButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewNoDataWithButtonBinding = ViewNoDataWithButtonBinding.inflate(
        LayoutInflater.from(context), this
    )

    var onClick: (() -> Unit)? = null

    init {
        setAttrs(attrs, R.styleable.NoDataViewWithButton) {
            binding.title.text = it.getString(R.styleable.NoDataViewWithButton_no_data_text_title)
            binding.desc.text = it.getString(R.styleable.NoDataViewWithButton_no_data_text_desc)
            binding.directionsText.text = it.getString(R.styleable.NoDataViewWithButton_no_data_text_direction)
            val showLoader = it.getBoolean(R.styleable.ToolbarView_toolbar_action_visible, true)
            binding.loader.isVisible = showLoader
            binding.title.isVisible = !showLoader
        }

        binding.topButton.onClick = {
            onClick?.invoke()
        }
    }

    var noData: Boolean = false
        set(value) {
            binding.root.isVisible = value
            field = value
        }

    fun toggleLoading(show: Boolean) {
        binding.root.isVisible = show || noData
        binding.loader.isVisible = show
        binding.info.isVisible = !show
    }
}