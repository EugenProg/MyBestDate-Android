package com.bestDate.data.extension

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
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
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bestDate.R
import com.bestDate.data.model.BaseResponse
import com.bestDate.presentation.main.chats.ChatListAdapter
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.takusemba.cropme.CropLayout
import com.takusemba.cropme.OnCropListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.ResponseBody
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max

fun EditText.textIsChanged(textIsChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

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
    imageIsSet: () -> Unit
): RequestBuilder<Drawable> {
    return this.addListener(object : RequestListener<Drawable> {
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
    Resources.getSystem().displayMetrics
).toInt()

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

fun CropLayout.cropListener(
    success: ((Bitmap) -> Unit)? = null,
    failure: ((Exception) -> Unit)? = null
) {
    this.addOnCropListener(object : OnCropListener {
        override fun onFailure(e: Exception) {
            failure?.invoke(e)
        }

        override fun onSuccess(bitmap: Bitmap) {
            success?.invoke(bitmap)
        }
    })
}

fun Context.openWebAddress(address: String?) {
    if (address.isNullOrEmpty() || !address.contains("http")) return
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(address)))
}

fun ResponseBody?.getErrorMessage(): String {
    return try {
        Gson().fromJson(this?.charStream(), BaseResponse::class.java).message
    } catch (e: Exception) {
        e.message.orEmpty()
    }
}

fun RecyclerView.swipeDeleteListener(deleteAction: ((Int) -> Unit)) {
    ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START) {
        val currentScrollOffset = 0

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (viewHolder is ChatListAdapter.ChatListItemViewHolder) {
                context.vibratePhone()
                deleteAction.invoke(viewHolder.adapterPosition)
            }
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            if (viewHolder is ChatListAdapter.ChatListItemViewHolder &&
                actionState == ItemTouchHelper.ACTION_STATE_SWIPE
            ) {
                with(viewHolder.itemView) {
                    val paint = Paint()
                    paint.color = ContextCompat.getColor(context, R.color.red)
                    val back = RectF(
                        right.toFloat() - 81.toPx(),
                        top.toFloat(),
                        right.toFloat(),
                        bottom.toFloat()
                    )
                    c.drawRect(back, paint)

                    val scrollOffset = currentScrollOffset + (-dX).toInt()
                    if (scrollOffset < 80.toPx()) {
                        super.onChildDraw(
                            c,
                            recyclerView,
                            viewHolder,
                            -scrollOffset.toFloat(),
                            dY,
                            actionState,
                            isCurrentlyActive
                        )
                    }

                    if (scrollOffset > 50.toPx()) {
                        val icon =
                            BitmapFactory.decodeResource(resources, R.drawable.ic_chat_list_delete)
                        val iconRect =
                            RectF(
                                right.toFloat() - 50.toPx(),
                                top.toFloat() + 28.toPx(),
                                right.toFloat() - 30.toPx(),
                                bottom.toFloat() - 30.toPx()
                            )
                        c.drawBitmap(icon, null, iconRect, paint)
                    }
                }
            }
        }
    }).attachToRecyclerView(this)
}