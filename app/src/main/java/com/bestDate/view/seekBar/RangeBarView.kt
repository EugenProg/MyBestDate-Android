package com.bestDate.view.seekBar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bestDate.R
import com.bestDate.databinding.ViewRangeBarBinding
import com.bestDate.databinding.ViewRangeBarThumbBinding
import com.google.gson.Gson
import java.lang.Exception

class RangeBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewRangeBarBinding
    var rangeChange: (() -> Unit)? = null

    init {
        val view = View.inflate(context, R.layout.view_range_bar, this)
        binding = ViewRangeBarBinding.bind(view)

        binding.bar.values = listOf(25f, 45f)

        binding.bar.addOnChangeListener { slider, _, _ ->
            rangeChange?.invoke()
            slider.setCustomThumbDrawablesForValues(
                getThumb(slider.values[0].toInt()),
                getThumb(slider.values[1].toInt())
            )
        }
    }

    var startProgress: Int
        get() = binding.bar.values[0].toInt()
        set(value) {
            binding.bar.values = mutableListOf(value.toFloat(), binding.bar.values[1])
        }

    var endProgress: Int
        get() = binding.bar.values[1].toInt()
        set(value) {
            binding.bar.values = mutableListOf(binding.bar.values[0], value.toFloat())
        }

    var minProgress: Int
        get() = binding.bar.valueFrom.toInt()
        set(value) {
            if (binding.bar.valueTo < value) binding.bar.valueTo = (value + 10).toFloat()
            binding.bar.valueFrom = value.toFloat()

        }

    var maxProgress: Int
        get() = binding.bar.valueTo.toInt()
        set(value) {
            binding.bar.valueTo = value.toFloat()
        }

    var range: String
        get() = createRangeString()
        set(value) {
            parseRangeString(value)
        }

    private fun getThumb(progress: Int): Drawable {
        val thumbViewBinding = ViewRangeBarThumbBinding.inflate(LayoutInflater.from(context))

        thumbViewBinding.progressNumber.text = progress.toString()
        with(thumbViewBinding.root) {
            measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
            val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            layout(0, 0, measuredWidth, measuredHeight)
            draw(canvas)

            return BitmapDrawable(resources, bitmap)
        }
    }


    private fun createRangeString(): String {
        val gson = Gson()
        return gson.toJson(Range(startProgress, endProgress))
    }

    private fun parseRangeString(range: String) {
        if (!range.matches(Regex("\\{\\s*\"min\":\\s*\\d+,\\s*\"max\":\\s*\\d+\\s*\\}"))) return
        try {
            val gson = Gson()
            val parsedRange = gson.fromJson(range, Range::class.java)
            binding.bar.values = mutableListOf(parsedRange.min.toFloat(), parsedRange.max.toFloat())
        } catch (e: Exception) {

        }
    }

    private data class Range(
        var min: Int,
        var max: Int
    )
}