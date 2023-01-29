package com.bestDate.presentation.main.anotherProfile.questionnaire

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.extension.orZero
import com.bestDate.databinding.FragmentAnotherProfileQuestionnaireBinding
import com.bestDate.db.entity.QuestionnaireDB
import com.bestDate.db.entity.UserDB
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.presentation.base.questionnaire.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class AnotherProfileQuestionnaireFragment:
    BaseVMFragment<FragmentAnotherProfileQuestionnaireBinding, AnotherProfileQuestionnaireViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAnotherProfileQuestionnaireBinding =
        { inflater, parent, attach -> FragmentAnotherProfileQuestionnaireBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<AnotherProfileQuestionnaireViewModel> =
        AnotherProfileQuestionnaireViewModel::class.java

    override val statusBarColor = R.color.bg_main
    private var myUser: UserDB? = null

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.translateLiveData) {
            binding.aboutMe.setInfo(it)
            binding.aboutMe.setTranslatable(false)
        }
        observe(viewModel.user) {
            setQuestionnaireInfo(it.questionnaire)
            setTranslateVisibility(it)
        }
        observe(viewModel.myUser) {
            myUser = it
            setTranslateVisibility(viewModel.user.value)
        }
    }

    private fun setTranslateVisibility(selectedUser: UserDB?) {
        binding.aboutMe.setTranslatable(
            !selectedUser?.questionnaire?.about_me.isNullOrBlank() &&
                    selectedUser?.language != myUser?.language
        )
    }

    private fun setQuestionnaireInfo(questionnaire: QuestionnaireDB?) {
        with(binding) {
            aboutMe.setInfo(questionnaire?.about_me)
            myHeight.setInfo(getString(R.string.cm_unit, questionnaire?.height.orZero.toString()))
            myWeight.setInfo(getString(R.string.kg_unit, questionnaire?.weight.orZero.toString()))
            eyeColor.setInfo(EyeColorType().getName(requireContext(), questionnaire?.eye_color))
            hairColor.setInfo(HairColorType().getName(requireContext(), questionnaire?.hair_color))
            hairLength.setInfo(HairLengthType().getName(requireContext(), questionnaire?.hair_length))

            maritalStatus.setInfo(MaritalStatus().getName(requireContext(), questionnaire?.marital_status))
            havingKids.setInfo(KidsCount().getName(requireContext(), questionnaire?.kids))
            placeOfResidence.setInfo(NationalityType().getName(requireContext(), questionnaire?.nationality))
            education.setInfo(EducationStatus().getName(requireContext(), questionnaire?.education))
            occupationalStatus.setInfo(OccupationalStatus().getName(requireContext(), questionnaire?.occupation))

            hobby.setInfo(HobbyType().getNameLine(requireContext(), questionnaire?.hobby))
            typesOfSports.setInfo(SportTypes().getNameLine(requireContext(), questionnaire?.sport))
            eveningTime.setInfo(EveningTimeType().getName(requireContext(), questionnaire?.evening_time))

            socials.setSocials(questionnaire?.socials)
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.toolbar.backClick = {
            goBack()
        }
        binding.aboutMe.translateClick = {
            viewModel.translate()
        }
    }
}