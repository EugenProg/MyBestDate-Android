package com.bestDate.view.questionnaire

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bestDate.R
import com.bestDate.databinding.ViewRangeBarQuestionnaireBinding
import com.google.gson.Gson
import java.lang.Exception

class RangeBarQuestionnaireView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewRangeBarQuestionnaireBinding
    var rangeIsChanged: ((String) -> Unit)? = null

    init {
        val view = View.inflate(context, R.layout.view_range_bar_questionnaire, this)
        binding = ViewRangeBarQuestionnaireBinding.bind(view)

        binding.bar.rangeChange = {
            rangeIsChanged?.invoke(range)
        }
    }

    var hint: String
        get() = binding.placeholder.text.toString()
        set(value) {
            binding.placeholder.text = value
        }

    var startProgress: Int
        get() = binding.bar.startProgress
        set(value) {
            binding.bar.startProgress = value
        }

    var endProgress: Int
        get() = binding.bar.endProgress
        set(value) {
            binding.bar.endProgress = value
        }

    var minProgress: Int
        get() = binding.bar.minProgress
        set(value) {
            binding.bar.minProgress = value

        }

    var maxProgress: Int
        get() = binding.bar.maxProgress
        set(value) {
            binding.bar.maxProgress = value
        }

    var range: String
        get() = binding.bar.range
        set(value) {
            binding.bar.range = value
        }
}

