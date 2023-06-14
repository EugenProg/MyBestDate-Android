package com.bestDate.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bestDate.R
import com.bestDate.data.extension.toSubscriptionDate
import com.bestDate.databinding.ViewSubscriptionInfoBinding

class SubscriptionInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewSubscriptionInfoBinding =
        ViewSubscriptionInfoBinding.inflate(LayoutInflater.from(context), this)

    @SuppressLint("SetTextI18n")
    fun setEndDate(endDate: String) {
        binding.description.text = "${context.getString(R.string.active)} • ${endDate.toSubscriptionDate()}"
    }
}