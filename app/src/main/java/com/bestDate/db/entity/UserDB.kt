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
import com.bestDate.data.model.ShortUserData
import com.bestDate.db.converters.PhotoConverter
import com.bestDate.db.converters.StringConverter
import com.bestDate.presentation.main.search.GenderFilter
import com.bestDate.presentation.registration.Gender
import com.bestDate.presentation.registration.GenderType
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
    var questionnaire: QuestionnaireDB? = null,
    var sent_messages_today: Int? = null,
    var sent_invitations_today: Int? = null
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

    fun getGenderFilter(): GenderFilter {
        return if (look_for?.contains("male") == true) GenderFilter.MEN
                else GenderFilter.WOMEN
    }

    @SuppressLint("SimpleDateFormat")
    fun getFormattedBirthday(): String? {
        val incomingFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateFormatter = SimpleDateFormat("dd MMMM yyyy")

        return birthday?.let { bd ->
            incomingFormatter.parse(bd)?.let { dateFormatter.format(it) }
        }
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

    fun getGenderType(): GenderType {
        GenderType.values().forEach {
            if (it.gender == gender && it.aim.firstOrNull() == look_for?.firstOrNull()) return it
        }
        return GenderType.WOMAN_LOOKING_MAN
    }

    fun getDuelGender(): Gender {
        return if (look_for?.first() == Gender.MAN.serverName) Gender.MAN
        else Gender.WOMAN
    }

    fun getBirthDate(): Date {
        return birthday?.getDateWithTimeOffset() ?: Date()
    }

    fun hasNoPhotos() = photos.isNullOrEmpty()
    fun questionnaireEmpty() = questionnaire?.isEmpty()
    fun questionnaireFull() = questionnaire?.isFull()

    fun getShortUser(oldUserData: ShortUserData?): ShortUserData {
        return ShortUserData(
            id = id,
            name = name,
            gender = gender,
            language = language,
            birthday = birthday,
            full_questionnaire = questionnaireFull(),
            role = oldUserData?.role ?: "user",
            blocked = blocked,
            blocked_me = blocked_me,
            block_messages = block_messages,
            allow_chat = oldUserData?.allow_chat ?: true,
            is_online = is_online,
            last_online_at = last_online_at,
            distance = distance,
            main_photo = getMainPhoto(),
            location = location,
            photos_count = photos?.size ?: 3
        )
    }
}
