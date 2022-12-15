package com.bestDate.view.settings

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bestDate.databinding.ViewPersonalInfoBinding

class PersonalInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewPersonalInfoBinding =
        ViewPersonalInfoBinding.inflate(LayoutInflater.from(context), this)


}