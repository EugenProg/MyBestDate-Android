package com.bestDate.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Invitation(
    @PrimaryKey val id: Int,
    val name: String? = null
)
