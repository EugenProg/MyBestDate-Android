package com.bestDate.presentation.main.search

import androidx.navigation.fragment.navArgs
import com.bestDate.presentation.base.fillRequredData.FillRegistrationOAuthDataFragment
import com.bestDate.presentation.registration.GenderType

class MainFillDataFragment: FillRegistrationOAuthDataFragment() {
    private val args: MainFillDataFragmentArgs by navArgs()

    override fun getName(): String = args.name.orEmpty()

    override fun getGender(): GenderType = args.gender

    override fun getBirthdate(): String? = args.birthDate

    override fun navigateAction() {
        navController.popBackStack()
    }
}