package com.bestDate.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class UserSettings(
    @PrimaryKey var user_id: Int,
    var block_messages: Boolean,
    @SerializedName("matches")
    var matchParticipation: Boolean,
    var invisible: Boolean,
    @Embedded
    var notifications: NotificationSettings
)

data class NotificationSettings(
    var likes: Boolean,
    var matches: Boolean,
    var invitations: Boolean,
    var messages: Boolean,
    var guests: Boolean
)