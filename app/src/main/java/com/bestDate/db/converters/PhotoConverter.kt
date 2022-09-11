package com.bestDate.db.converters

import androidx.room.TypeConverter
import com.bestDate.data.model.ProfileImage
import com.google.gson.Gson

class PhotoConverter {
    @TypeConverter
    fun fromPhotoString(list: MutableList<ProfileImage>?): String {
        return if (list != null) Gson().toJson(list) else ""
    }

    @TypeConverter
    fun toPhotoList(value: String?): MutableList<ProfileImage> {
        return if (value.isNullOrEmpty()) ArrayList()
        else Gson().fromJson(value, Array<ProfileImage>::class.java).toMutableList()
    }
}