package com.bestDate.db.entity

import android.annotation.SuppressLint
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.bestDate.R
import com.bestDate.data.model.ProfileImage
import com.bestDate.db.converters.PhotoConverter
import com.bestDate.db.converters.StringConverter
import java.text.SimpleDateFormat

@Entity
data class UserDB(
    @PrimaryKey val id: Int,
    var name: String? = null,
    var email: String? = null,
    var email_verification: Boolean? = null,
    var phone: String? = null,
    var phone_verification: Boolean? = null,
    var gender: String? = null,
    @TypeConverters(StringConverter::class)
    var look_for: MutableList<String>? = null,
    var language: String? = null,
    var birthday: String? = null,
    var is_online: Boolean? = null,
    var last_online_at: String? = null,
    var new_likes: Int? = null,
    var new_guests: Int? = null,
    var distance: Double? = null,
    @TypeConverters(PhotoConverter::class)
    var photos: MutableList<ProfileImage>? = null,
    @Embedded
    var location: LocationDB? = null,
    var block_messages: Boolean? = null,
    var blocked: Boolean? = null,
    var blocked_me: Boolean? = null,
    @Embedded
    var questionnaire: QuestionnaireDB? = null
) {
    fun getLocalizeGender(): Int {
        return when {
            gender == "male" && look_for?.contains("male") == true -> R.string.man_looking_for_a_man
            gender == "male" && look_for?.contains("female") == true -> R.string.man_looking_for_a_woman
            gender == "female" && look_for?.contains("female") == true -> R.string.woman_looking_for_a_woman
            gender == "female" && look_for?.contains("male") == true -> R.string.woman_looking_for_a_man
            else -> R.string.man_looking_for_a_woman
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getFormattedBirthday(): String? {
        val incomingFormatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val dateFormatter = SimpleDateFormat("dd MMMM yyyy")

        return incomingFormatter.parse(birthday.orEmpty())?.let { dateFormatter.format(it) }
    }
}
