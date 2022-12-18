package com.bestDate.db.dao

import androidx.room.*
import com.bestDate.db.entity.UserSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSettingsDao {
    @Query("SELECT * FROM UserSettings LIMIT 1")
    fun getUserSettingsFlow(): Flow<UserSettings>

    @Query("SELECT * FROM UserSettings LIMIT 1")
    fun getUserSettings(): UserSettings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(settings: UserSettings)

    @Transaction
    fun validate(settings: UserSettings) {
        val userSettings = getUserSettings()
        if (userSettings != null && settings.user_id != userSettings.user_id) {
            delete()
        }
        save(settings)
    }

    @Query("DELETE FROM UserSettings")
    fun delete()
}