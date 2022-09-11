package com.bestDate.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson

class StringConverter {
    @TypeConverter
    fun fromString(list: MutableList<String>?): String {
        return if (list != null) Gson().toJson(list) else ""
    }

    @TypeConverter
    fun toList(value: String?): MutableList<String> {
        return if (value.isNullOrEmpty()) ArrayList()
        else Gson().fromJson(value, Array<String>::class.java).toMutableList()
    }
}