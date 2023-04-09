package com.bestDate.data.extension

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.bestDate.data.utils.Logger
import java.io.File
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

fun File.getImages(): MutableList<File> {
    val files: MutableList<File> = ArrayList()
    if (this.exists() && this.listFiles() != null) {
        for (entry in this.listFiles()) {
            if (entry.isImage) {
                files.add(entry)
            } else if (entry.isDirectory) {
                files.addAll(entry.getImages())
            }
        }
    }
    return files
}

val File.isImage: Boolean
    get() = this.isFile &&
            (this.extension.lowercase() == "jpg" ||
                    this.extension.lowercase() == "jpeg" ||
                    this.extension.lowercase() == "png") &&
            this.canRead()

fun URL.toBitmap(errorCallBack: (() -> Unit)? = null): Bitmap? {
    return try {
        val connection = this.openConnection() as HttpsURLConnection

        BitmapFactory.decodeStream(connection.inputStream)
    } catch (e: IOException) {
        e.printStackTrace()
        Logger.print("Error ${e.message} by loading the file $this")
        errorCallBack?.invoke()
        null
    }
}