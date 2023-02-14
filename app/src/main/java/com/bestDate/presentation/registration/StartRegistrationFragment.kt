package com.bestDate.presentation.registration

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.openAsLink
import com.bestDate.data.extension.setPaddingBottom
import com.bestDate.data.extension.toStringFormat
import com.bestDate.data.utils.ViewUtils
import com.bestDate.databinding.FragmentStartRegistrationBinding
import com.bestDate.data.extension.show
import com.bestDate.presentation.base.BaseAuthFragment
import com.bestDate.view.CalendarView
import com.bestDate.view.bottomSheet.genderSheet.GenderSheet
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class StartRegistrationFragment : BaseAuthFragment<FragmentStartRegistrationBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentStartRegistrationBinding =
        { inflater, parent, attach -> FragmentStartRegistrationBinding.inflate(inflater, parent, attach) }

    override val navBarColor = R.color.main_dark
    override val statusBarLight = true

    private var genderSheet: GenderSheet = GenderSheet()
    private lateinit var datePicker: MaterialDatePicker<Long>

    override fun onInit() {
        super.onInit()
        with(binding) {
            nameInput.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
            nameInput.text = RegistrationHolder.name

            RegistrationHolder.gender?.line?.let { genderInput.text = getString(it) }
            birthInput.text = RegistrationHolder.birthdate?.toStringFormat().orEmpty()
        }

        datePicker = CalendarView().getDateSelectCalender(
            getString(R.string.birth_date), RegistrationHolder.birthdate, birthDateSelect()
        )
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            backButton.onClick = {
                navController.popBackStack()
            }
            nextButton.onSafeClick = {
                if (!genderInput.isVisible) hideKeyboardAction()
                else validate()
            }
            socialContainer.googleClick = {
                loginByGoogle()
            }
            socialContainer.facebookClick = {
                loginWithFacebook()
            }
            loginButton.setOnClickListener {
                navController.navigate(StartRegistrationFragmentDirections
                    .actionStartRegistrationFragmentToAuthFragment())
            }
            genderInput.onClick = {
                genderSheet.show(childFragmentManager, genderSheet.tag)
            }
            birthInput.onClick = {
                datePicker.show(childFragmentManager)
            }
            genderSheet.itemClick = {
                RegistrationHolder.gender = it
                genderInput.text = getString(it.line)
            }
            privacyPolicyCheckBox.textClick = {
                "https://dev-api.bestdate.info/privacy-policy".openAsLink(requireContext())
            }
            termsOfUseCheckBox.textClick = {
                "https://dev-api.bestdate.info/use-agreement".openAsLink(requireContext())
            }
        }
    }

    private fun birthDateSelect(): (Date) -> Unit {
        return {
            RegistrationHolder.birthdate = it
            binding.birthInput.text = it.toStringFormat()
        }
    }

    private fun validate() {
        with(binding) {
            when {
                nameInput.text.isBlank() -> nameInput.setError()
                genderInput.text.isBlank() -> genderInput.setError()
                birthInput.text.isBlank() -> birthInput.setError()
                !termsOfUseCheckBox.checked -> termsOfUseCheckBox.setError()
                !privacyPolicyCheckBox.checked -> privacyPolicyCheckBox.setError()
                else -> {
                    RegistrationHolder.name = nameInput.text
                    navController.navigate(StartRegistrationFragmentDirections
                        .actionStartRegistrationFragmentToContinueRegistrationFragment())
                }
            }
        }
    }

    override fun scrollAction() {
        super.scrollAction()
        with(binding) {
            ViewUtils().hideViews(socialContainer, loginButton, genderInput, birthInput,
                termsOfUseCheckBox, privacyPolicyCheckBox)
            inputContainer.setPaddingBottom(0)
            scroll.fullScroll(View.FOCUS_DOWN)
            if (nameInput.hasFocus) nameInput.setFocus()
        }
    }

    override fun hideAction() {
        super.hideAction()
        with(binding) {
            ViewUtils().showViews(socialContainer, loginButton, genderInput, birthInput,
                termsOfUseCheckBox, privacyPolicyCheckBox)
            inputContainer.setPaddingBottom(90)
        }
    }

    override fun navigateToMain() {
        navController.navigate(StartRegistrationFragmentDirections.actionStartRegistrationToMain())
    }

    override fun navigateToFillData(name: String?, birthDate: String?, genderType: GenderType) {
        navController.navigate(StartRegistrationFragmentDirections
            .actionStartRegistrationToFillRegistrationAouthData(name, birthDate, genderType))
    }

    override fun navigateToPhoto() {
        navController.navigate(StartRegistrationFragmentDirections
            .actionStartRegistrationToPhotoEditor())
    }

    override fun navigateToQuestionnaire() {
        navController.navigate(StartRegistrationFragmentDirections
            .actionStartRegistrationToQuestionnaire())
    }
}