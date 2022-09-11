package com.bestDate.db.dao

import androidx.room.*
import com.bestDate.db.entity.UserDB
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM UserDB LIMIT 1")
    fun getUserFlow(): Flow<UserDB?>

    @Query("SELECT * FROM UserDB LIMIT 1")
    fun getUser(): UserDB?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(userDB: UserDB)

    @Transaction
    fun validate(userDB: UserDB) {
        val user = getUser()
        if (user != null && userDB.id != user.id) {
            delete()
        }
        save(userDB)
    }

    @Query("DELETE FROM UserDB")
    fun delete()
}