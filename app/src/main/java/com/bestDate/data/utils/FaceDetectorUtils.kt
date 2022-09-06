package com.bestDate.data.utils

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector

class FaceDetectorUtils {
    var detector: FaceDetector? = null

    var completion: ((facesCount: Int) -> Unit)? = null

    init {
        detector = FaceDetection.getClient()
    }

    fun detectFaces(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        detector?.process(image)
            ?.addOnSuccessListener {
                completion?.invoke(it.size)
            }
            ?.addOnFailureListener {
                completion?.invoke(0)
            }
    }
}