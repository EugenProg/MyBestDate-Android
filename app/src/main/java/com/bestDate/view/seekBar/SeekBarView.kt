package com.bestDate.view.seekBar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.bestDate.data.extension.onChangeListener
import com.bestDate.databinding.ViewSeekBarBinding
import com.bestDate.databinding.ViewSeekBarThumbBinding

class SeekBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewSeekBarBinding =
        ViewSeekBarBinding.inflate(LayoutInflater.from(context), this)

    init {
        binding.bar.onChangeListener(progressChanged = onSeekBarChanged())
    }

    var progress: Int
        get() = binding.bar.progress
        set(value) {
            binding.bar.progress = value
        }

    var minProgress: Int
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) binding.bar.min else 0
        set(value) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (binding.bar.max < value) binding.bar.max = value + 10
                binding.bar.min = value
            }
        }

    var maxProgress: Int
        get() = binding.bar.max
        set(value) {
            binding.bar.max = value
        }

    private fun onSeekBarChanged(): ((SeekBar?, Int, Boolean) -> Unit) {
        return { bar, progress, _ ->
            bar?.thumb = getThumb(progress)
        }
    }

    private fun getThumb(progress: Int): Drawable {
        val thumbViewBinding = ViewSeekBarThumbBinding.inflate(LayoutInflater.from(context))

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
}