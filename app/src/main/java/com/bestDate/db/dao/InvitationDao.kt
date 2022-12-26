package com.bestDate.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.bestDate.db.entity.Invitation
import kotlinx.coroutines.flow.Flow

@Dao
interface InvitationDao {
    @Query("SELECT * FROM Invitation")
    fun getInvitations(): Flow<MutableList<Invitation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(invitations: MutableList<Invitation>)

    @Transaction
    fun save(invitationList: MutableList<Invitation>) {
        delete()
        insert(invitationList)
    }

    @Query("DELETE FROM Invitation")
    fun delete()
}