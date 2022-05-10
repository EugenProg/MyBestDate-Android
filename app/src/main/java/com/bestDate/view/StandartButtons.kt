package com.bestDate.view

import android.content.Context
import android.util.AttributeSet
import com.bestDate.R

class WhiteButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): BaseButton(context, attrs, defStyleAttr,
    buttonColor = R.color.white,
    textColor = R.color.bg_main,
    progressColor = R.color.bg_main)

class BlackButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): BaseButton(context, attrs, defStyleAttr,
    buttonColor = R.color.bg_main,
    textColor = R.color.white,
    progressColor = R.color.white_90)
