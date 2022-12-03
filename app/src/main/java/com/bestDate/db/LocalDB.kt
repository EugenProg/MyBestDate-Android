package com.bestDate.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bestDate.db.converters.PhotoConverter
import com.bestDate.db.converters.StringConverter
import com.bestDate.db.dao.UserDao
import com.bestDate.db.entity.UserDB

@Database(
    version = 2,
    entities = [
        UserDB::class
    ]
)
@TypeConverters(StringConverter::class, PhotoConverter::class)
abstract class LocalDB: RoomDatabase() {
    abstract fun userDao(): UserDao
}