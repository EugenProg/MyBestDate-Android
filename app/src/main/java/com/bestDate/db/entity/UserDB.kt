package com.bestDate.db.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.bestDate.R
import com.bestDate.data.extension.getDateWithTimeOffset
import com.bestDate.data.extension.getDiffYears
import com.bestDate.data.model.ProfileImage
import com.bestDate.db.converters.PhotoConverter
import com.bestDate.db.converters.StringConverter
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Entity
@Parcelize
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
    var created_at: String? = null,
    var coins: String? = null,
    var is_online: Boolean? = null,
    var last_online_at: String? = null,
    var new_likes: Int? = null,
    var new_guests: Int? = null,
    var new_messages: Int? = null,
    var new_invitations: Int? = null,
    var new_duels: Int? = null,
    var new_matches: Int? = null,
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
) : Parcelable {
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

    fun getMainPhoto(): ProfileImage {
        return photos?.firstOrNull { it.main == true } ?: ProfileImage().getDefaultPhoto()
    }

    fun getMainPhotoThumbUrl(): String? {
        return getMainPhoto().thumb_url
    }

    fun getAge(): String {
        val yourDate = birthday?.let { it.getDateWithTimeOffset() } ?: return "18"
        return getDiffYears(yourDate, Date()).toString()
    }

    fun getUserLocation(): String {
        return "${location?.country.orEmpty()}, ${location?.city.orEmpty()}"
    }

    fun hasNoPhotos() = photos.isNullOrEmpty()
    fun questionnaireEmpty() = questionnaire?.isEmpty()
    fun questionnaireFull() = questionnaire?.isFull()
}
