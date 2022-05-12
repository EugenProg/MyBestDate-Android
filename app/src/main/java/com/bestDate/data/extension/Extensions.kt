package com.bestDate.data.extension

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.core.view.updatePadding
import com.bestDate.R
import java.text.SimpleDateFormat
import java.util.*

fun EditText.textIsChanged(textIsChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

        override fun afterTextChanged(s: Editable?) {
            textIsChanged.invoke(s.toString())
        }
    })
}

fun Context.vibratePhone() {
    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(100)
    }
}

fun View.animateError() {
    this.startAnimation(
        AnimationUtils.loadAnimation(context, R.anim.show_shake)
    )
}

fun View.setPaddingBottom(paddingBottom: Int) {
    this.updatePadding(bottom = paddingBottom.toPx())
}
/**
 * An extension to convert numbers from dp to px
 * */
fun Int.toPx() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics).toInt()

@SuppressLint("SimpleDateFormat")
fun Date.toStringFormat(): String {
    val formatter = SimpleDateFormat("dd MMMM yyyy")
    return formatter.format(this)
}