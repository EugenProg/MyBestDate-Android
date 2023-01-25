package com.bestDate.presentation.registration

import androidx.annotation.StringRes
import com.bestDate.R
import com.bestDate.data.extension.formatToPhoneNumber
import com.bestDate.data.extension.toServerFormat
import com.bestDate.data.model.RegistrationRequest
import java.util.*

object RegistrationHolder {
    fun getRegistrationData(): RegistrationRequest {
        val email = if (type == RegistrationType.EMAIL) login else null
        val phone = if (type == RegistrationType.PHONE) login.formatToPhoneNumber() else null
        return RegistrationRequest(
            email = email,
            phone = phone,
            name = name,
            password = password,
            password_confirmation = password,
            gender = gender?.gender.orEmpty(),
            birthday = birthdate?.toServerFormat().orEmpty(),
            look_for = gender?.aim ?: mutableListOf()
        )
    }

    var type: RegistrationType = RegistrationType.EMAIL
    var login: String = ""
    var password: String = ""
    var name: String = ""
    var birthdate: Date? = null
    var gender: GenderType? = null
}

enum class RegistrationType {
    EMAIL, PHONE
}

enum class GenderType(@StringRes val line: Int, val gender: String, val aim: MutableList<String>) {
    MAN_LOOKING_MAN(R.string.man_looking_for_a_man, "male", mutableListOf("male")),
    MAN_LOOKING_WOMAN(R.string.man_looking_for_a_woman, "male", mutableListOf("female")),
    WOMAN_LOOKING_MAN(R.string.woman_looking_for_a_man, "female", mutableListOf("male")),
    WOMAN_LOOKING_WOMAN(R.string.woman_looking_for_a_woman, "female", mutableListOf("female"))
}

enum class Gender(val label: Int, val serverName: String) {
    MAN(R.string.man, "male"),
    WOMAN(R.string.woman, "female")
}