package com.bestDate.data.extension

import java.io.File

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
                    this.extension.lowercase() == "png")