package com.bestDate.data.utils

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader

class ImageFetcherUtil(val context: Context) : LoaderManager.LoaderCallbacks<Cursor> {
    var imagesLoaded: ((images: MutableList<String>) -> Unit)? = null

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val selection: String? = null
        val selectionArgs = arrayOf<String>()
        val sortOrder = MediaStore.Images.Media.DATE_ADDED

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
        val images: MutableList<String> = mutableListOf()
        data?.let {
            val columnIndexData = it.getColumnIndexOrThrow(MediaColumns.DATA)

            while (it.moveToNext()) {
                images.add(it.getString(columnIndexData))
            }
        }
        images.reverse()
        imagesLoaded?.invoke(images)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {}
}