package com.bestDate.db

import android.content.Context
import androidx.room.Room
import com.bestDate.db.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomDbModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LocalDB {
        return Room
            .databaseBuilder(context, LocalDB::class.java, "BestDate")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(db: LocalDB): UserDao { return db.userDao() }
}