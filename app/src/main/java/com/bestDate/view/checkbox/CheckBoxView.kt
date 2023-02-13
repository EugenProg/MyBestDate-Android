package com.bestDate.view.checkbox

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.animateError
import com.bestDate.data.extension.setAttrs
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.extension.vibratePhone
import com.bestDate.databinding.ViewCheckBoxBinding

class CheckBoxView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewCheckBoxBinding =
        ViewCheckBoxBinding.inflate(LayoutInflater.from(context), this)
    private val uncheckedColor = ContextCompat.getColor(context, R.color.white_70)
    private val checkedColor = ContextCompat.getColor(context, R.color.white)
    private var linkText: String? = ""

    var textClick: (() -> Unit)? = null

    init {
        setAttrs(attrs, R.styleable.CheckBoxView) {
            linkText = it.getString(R.styleable.CheckBoxView_check_box_link)
            setSpannableText(uncheckedColor)
        }

        binding.checkBox.setOnClickListener {
            checked = !checked
        }

        binding.title.setOnSaveClickListener {
            textClick?.invoke()
        }
    }

    var checked: Boolean = false
    set(value) {
        field = value
        toggleCheckable()
    }

    fun setError() {
        with(binding) {
            checkBox.setBackgroundResource(R.drawable.check_box_error_frame)
            setSpannableText(ContextCompat.getColor(context, R.color.red))
        }
        this.animateError()
        context.vibratePhone()
    }

    private fun toggleCheckable() {
        if (checked) check()
        else uncheck()
    }

    private fun check() {
        with(binding) {
            checkBox.setBackgroundResource(R.drawable.check_box_frame)
            setSpannableText(checkedColor)
            isCheckedView.isVisible = true
        }
    }

    private fun uncheck() {
        with(binding) {
            checkBox.setBackgroundResource(R.drawable.check_box_frame)
            setSpannableText(uncheckedColor)
            isCheckedView.isVisible = false
        }
    }

    private fun setSpannableText(@ColorInt color: Int) {
        val agreeText = context.getString(R.string.i_agree_and_accept)
        linkText?.let {
            val startIndex = agreeText.length + 1
            val endIndex = startIndex + it.length
            val span = SpannableString("$agreeText $it")
            span.setSpan(UnderlineSpan(), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            span.setSpan(StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            span.setSpan(ForegroundColorSpan(color), 0, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.title.text = span
        }
    }
}