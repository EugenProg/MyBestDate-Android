package com.bestDate.db.entity

import androidx.room.Entity

@Entity
data class LocationDB(
    var iso_code: String? = null,
    var country: String? = null,
    var city: String? = null
)