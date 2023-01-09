package com.bestDate.view.questionnaire

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bestDate.databinding.ViewSurprisingBinding

class SurprisingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: ViewSurprisingBinding =
        ViewSurprisingBinding.inflate(LayoutInflater.from(context), this)
    private var isVisibleSurprise: Boolean = true

    fun showSurprise() {
        if (!isVisibleSurprise) toggle()
    }

    fun hideSurprise() {
        if (isVisibleSurprise) toggle()
    }

    private fun toggle() {
        with(binding) {
            ok.isVisible = isVisibleSurprise
            surprise.isVisible = !isVisibleSurprise
            isVisibleSurprise = !isVisibleSurprise
        }
    }
}