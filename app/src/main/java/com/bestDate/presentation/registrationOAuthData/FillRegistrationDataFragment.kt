package com.bestDate.presentation.registrationOAuthData

import androidx.navigation.fragment.navArgs
import com.bestDate.presentation.base.fillRequredData.FillRegistrationOAuthDataFragment
import com.bestDate.presentation.registration.GenderType

class FillRegistrationDataFragment : FillRegistrationOAuthDataFragment() {
    private val args: FillRegistrationDataFragmentArgs by navArgs()

    override fun getName(): String = args.name.orEmpty()

    override fun getGender(): GenderType = args.gender

    override fun getBirthdate(): String? = args.birthDate

    override fun navigateAction() {
        navController.navigate(
            FillRegistrationDataFragmentDirections.actionFillRegistrationAouthDataToPhotoEditor()
        )
    }
}