package com.bestDate.db.entity

import androidx.room.Entity

@Entity
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
)
