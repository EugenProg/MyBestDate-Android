package com.bestDate.view.questionnaire

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.databinding.ViewSurprisingBinding

class SurprisingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: ViewSurprisingBinding
    private var isVisibleSurprise: Boolean = true

    init {
        val view = View.inflate(context, R.layout.view_surprising, this)
        binding = ViewSurprisingBinding.bind(view)
    }

    fun showSurprise() {
        if (!isVisibleSurprise) toggle()
    }

    fun hideSurprise() {
        if (isVisibleSurprise) toggle()
    }

    fun toggle() {
        with(binding) {
            ok.isVisible = isVisibleSurprise
            surprise.isVisible = !isVisibleSurprise
            isVisibleSurprise = !isVisibleSurprise
        }
    }
}