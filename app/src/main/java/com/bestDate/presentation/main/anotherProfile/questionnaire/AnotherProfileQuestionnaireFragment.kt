package com.bestDate.presentation.main.anotherProfile.questionnaire

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bestDate.R
import com.bestDate.base.BaseFragment
import com.bestDate.data.extension.orZero
import com.bestDate.databinding.FragmentAnotherProfileQuestionnaireBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnotherProfileQuestionnaireFragment: BaseFragment<FragmentAnotherProfileQuestionnaireBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAnotherProfileQuestionnaireBinding =
        { inflater, parent, attach -> FragmentAnotherProfileQuestionnaireBinding.inflate(inflater, parent, attach) }

    override val statusBarColor = R.color.bg_main
    private val args by navArgs<AnotherProfileQuestionnaireFragmentArgs>()

    override fun onInit() {
        super.onInit()
        val user = args.user
        val questionnaire = user?.questionnaire

        with(binding) {
            aboutMe.setInfo(questionnaire?.about_me)
            myHeight.setInfo(getString(R.string.cm_unit, questionnaire?.height.orZero.toString()))
            myWeight.setInfo(getString(R.string.kg_unit, questionnaire?.weight.orZero.toString()))
            eyeColor.setInfo(questionnaire?.eye_color)
            hairColor.setInfo(questionnaire?.hair_color)
            hairLength.setInfo(questionnaire?.hair_length)

            maritalStatus.setInfo(questionnaire?.marital_status)
            havingKids.setInfo(questionnaire?.kids)
            placeOfResidence.setInfo(questionnaire?.nationality)
            education.setInfo(questionnaire?.education)
            occupationalStatus.setInfo(questionnaire?.occupation)

            hobby.setInfo(questionnaire?.hobby?.joinToString(", "))
            typesOfSports.setInfo(questionnaire?.sport?.joinToString(", "))
            eveningTime.setInfo(questionnaire?.evening_time)

            socials.setSocials(questionnaire?.socials)
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.toolbar.backClick = {
            goBack()
        }
    }
}