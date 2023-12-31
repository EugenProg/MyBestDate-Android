package com.bestDate.db.entity

import android.os.Parcelable
import androidx.room.Entity
import com.bestDate.data.extension.orZero
import com.bestDate.data.utils.Logger
import com.bestDate.view.seekBar.RangeBarView
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class QuestionnaireDB(
    var purpose: String? = null,
    var expectations: String? = null,
    var height: Int? = null,
    var weight: Int? = null,
    var eye_color: String? = null,
    var hair_color: String? = null,
    var hair_length: String? = null,
    var marital_status: String? = null,
    var kids: String? = null,
    var education: String? = null,
    var occupation: String? = null,
    var nationality: String? = null,
    var search_country: String? = null,
    var search_city: String? = null,
    var about_me: String? = null,
    var search_age_min: Int? = null,
    var search_age_max: Int? = null,
    var socials: MutableList<String>? = mutableListOf(),
    var hobby: MutableList<String>? = mutableListOf(),
    var sport: MutableList<String>? = mutableListOf(),
    var evening_time: String? = null
): Parcelable {
    fun getLocation(): String {
        if (search_country == null && search_city == null) return ""

        var location = if (search_country != null) search_country else ""
        location += if (location?.isNotBlank() == true) ", ${search_city.orEmpty()}" else search_city.orEmpty()
        return location.orEmpty()
    }

    fun getAgeRange(): String {
        val gson = Gson()
        return gson.toJson(
            RangeBarView.Range(
                min = search_age_min ?: 27,
                max = search_age_max ?: 81
            )
        )
    }

    fun setLocation(answer: String?) {
        val locationArray = answer?.split(", ")
        search_country = if (locationArray.isNullOrEmpty()) null else locationArray[0]
        search_city = if (locationArray?.size.orZero > 1) locationArray?.get(1) else null
    }

    fun setAgeRange(answer: String?) {
        if (answer != null) {
            try {
                val range = Gson().fromJson(answer, RangeBarView.Range::class.java)
                search_age_max = range.max
                search_age_min = range.min
            } catch (e: Exception) {
                Logger.print("Parsing error: ${e.message}")
            }
        }
    }

    fun isEmpty(): Boolean {
        for (f in javaClass.declaredFields) {
            f.isAccessible = true
            if (f.name == "socials" || f.name == "hobby" || f.name == "sport" || f.name == "CREATOR") continue
            if (f[this] != null) return false
        }
        if (!socials.isNullOrEmpty() || !hobby.isNullOrEmpty() || !sport.isNullOrEmpty()) return false
        return true
    }

    fun isFull(): Boolean {
        for (f in javaClass.declaredFields) {
            f.isAccessible = true
            if (f.name == "socials" || f.name == "hobby" || f.name == "sport" || f.name == "CREATOR") continue
            if (f[this] == null) return false
        }
        if (socials.isNullOrEmpty() || hobby.isNullOrEmpty() || sport.isNullOrEmpty()) return false
        return true
    }
}
