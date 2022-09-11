package com.bestDate.data.extension

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.takusemba.cropme.CropLayout
import com.takusemba.cropme.OnCropListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max

fun EditText.textIsChanged(textIsChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

        override fun afterTextChanged(s: Editable?) {
            textIsChanged.invoke(s.toString())
        }
    })
}

/**
 * An extension to create a observer from a editText
 * */
@ExperimentalCoroutinesApi
fun EditText.textInputAsFlow() = callbackFlow {
    val watcher: TextWatcher = doOnTextChanged { text: CharSequence?, _, _, _ ->
        this.trySend(text).isSuccess
    }
    awaitClose { this@textInputAsFlow.removeTextChangedListener(watcher) }
}

fun RequestBuilder<Drawable>.imageIsSet(
    owner: LifecycleOwner,
    imageIsSet: () -> Unit): RequestBuilder<Drawable> {
    return this.addListener(object : RequestListener<Drawable>{
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            owner.postDelayed({ imageIsSet.invoke() }, 10)
            return false
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

@SuppressLint("SimpleDateFormat")
fun Date.toServerFormat(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(this)
}

val Int?.orZero: Int
get() = this ?: 0


fun Uri?.getBitmap(context: Context): Bitmap? {
    if (this == null) return null
    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(context.contentResolver, this)
        ImageDecoder.decodeBitmap(source)
    } else {
        MediaStore.Images.Media.getBitmap(context.contentResolver, this)
    }

    return bitmap.copy(Bitmap.Config.ARGB_8888, true)
}

fun Bitmap.scale(size: Double? = 2048.0): Bitmap {
    val readySize = size ?: 0.0

    if (max(this.height, this.width) < readySize * 1.5) return this

    val nh = (this.height * (readySize / this.width)).toInt()
    return Bitmap.createScaledBitmap(this, readySize.toInt(), nh, true)
}

fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, stream)

    return stream.toByteArray()
}

fun CropLayout.cropListener(success: ((Bitmap) -> Unit)? = null,
                            failure: ((Exception) -> Unit)? = null) {
    this.addOnCropListener(object : OnCropListener {
        override fun onFailure(e: Exception) {
            failure?.invoke(e)
        }

        override fun onSuccess(bitmap: Bitmap) {
            success?.invoke(bitmap)
        }
    })
}