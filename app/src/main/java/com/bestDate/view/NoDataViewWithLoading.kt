package com.bestDate.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.setAttrs
import com.bestDate.databinding.ViewNoDataWithLoadingBinding

class NoDataViewWithLoading @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewNoDataWithLoadingBinding = ViewNoDataWithLoadingBinding.inflate(
        LayoutInflater.from(context), this)

    init {
        setAttrs(attrs, R.styleable.NoDataViewWithLoading) {
            binding.title.text = it.getString(R.styleable.NoDataViewWithLoading_no_data_text)
            val showLoader = it.getBoolean(R.styleable.ToolbarView_toolbar_action_visible, true)
            binding.loader.isVisible = showLoader
            binding.title.isVisible = !showLoader
        }
    }

    fun setTitle(title: String) {
        binding.title.text = title
    }

    var noData: Boolean = false
    set(value) {
        binding.root.isVisible = value
        field = value
    }

    fun toggleLoading(show: Boolean) {
        binding.root.isVisible = show || noData
        binding.loader.isVisible = show
        binding.title.isVisible = !show
    }
}