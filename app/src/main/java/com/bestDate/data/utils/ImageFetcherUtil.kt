package com.bestDate.data.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader

class ImageFetcherUtil(val context: Context) : LoaderManager.LoaderCallbacks<Cursor> {
    var imagesLoaded: ((images: MutableList<Uri>) -> Unit)? = null

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaColumns.DATA, MediaStore.Images.Media._ID)
        val selection: String? = null
        val selectionArgs = arrayOf<String>()
        val sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC"

        return CursorLoader(
            context,
            uri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        val images: MutableList<Uri> = mutableListOf()
        data?.let {
            while (it.moveToNext()) {
                val imageId = it.getInt(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                images.add(
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    imageId.toLong()
                    )
                )
            }
        }
        imagesLoaded?.invoke(images)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {}
}