package com.bestDate.db.entity

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class LocationDB(
    var iso_code: String? = null,
    var country: String? = null,
    var city: String? = null
): Parcelable