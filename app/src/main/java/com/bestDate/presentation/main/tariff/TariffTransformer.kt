package com.bestDate.presentation.main.tariff

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2.PageTransformer
import com.bestDate.data.extension.toPx

class TariffTransformer: PageTransformer {
    private val horizontalOffset = (-90).toPx()
    private val topOffset = (-20f).toPx()
    private val rotationDegree = 10

    override fun transformPage(page: View, position: Float) {
        page.translationX = horizontalOffset * position
        page.rotationY = rotationDegree * position
        when {
            position <= 0 -> {
                page.pivotX = page.width.toFloat()
                page.translationY = topOffset * position
            }
            else -> {
                page.pivotX = 0f
            }
        }
    }
}

class HorizontalMarginDecoration(margin: Int): RecyclerView.ItemDecoration() {
    private val horizontalMargin = margin.toPx()
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.left = horizontalMargin
        outRect.right = horizontalMargin
    }
}