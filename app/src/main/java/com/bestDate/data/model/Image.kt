package com.bestDate.data.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    var bitmap: Bitmap? = null,
    val uri: String? = null,
    var isMain: Boolean = false,
    var mutualSympathy: Boolean = false,
    var topFifty: Boolean = false
): Parcelable {
    fun getImageCopy(): Image {
        return Image(bitmap, uri, isMain, mutualSympathy, topFifty)
    }
}
