package com.bestDate.presentation.base.fillRequredData

import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.*
import com.bestDate.data.model.UpdateUserRequest
import com.bestDate.databinding.FragmentFillRegistrationOauthDataBinding
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.presentation.registration.GenderType
import com.bestDate.view.CalendarView
import com.bestDate.view.bottomSheet.genderSheet.GenderSheet
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
abstract class FillRegistrationOAuthDataFragment :
    BaseVMFragment<FragmentFillRegistrationOauthDataBinding, FillRegistrationOAuthDataViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentFillRegistrationOauthDataBinding =
        { inflater, parent, attach ->
            FragmentFillRegistrationOauthDataBinding.inflate(
                inflater,
                parent,
                attach
            )
        }
    override val viewModelClass: Class<FillRegistrationOAuthDataViewModel> =
        FillRegistrationOAuthDataViewModel::class.java

    override val statusBarColor = R.color.bg_main
    private var genderSheet: GenderSheet = GenderSheet()
    private lateinit var datePicker: MaterialDatePicker<Long>
    private lateinit var selectedGender: GenderType
    private var selectedBirthDate: Date = getEighteenYearDate()

    abstract fun getName(): String
    abstract fun getGender(): GenderType
    abstract fun getBirthdate(): String?
    abstract fun navigateAction()

    override fun onInit() {
        super.onInit()
        selectedGender = getGender()
        getBirthdate()?.let {
            selectedBirthDate = it.getDate()
        }

        with(binding) {
            nameInput.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
            nameInput.text = getName()
            genderInput.text = getString(selectedGender.line)
            birthInput.text = selectedBirthDate.toStringFormat()
        }
        datePicker = CalendarView().getDateSelectCalender(
            getString(R.string.birth_date),
            selectedBirthDate,
            birthDateSelect()
        )
    }

    private fun birthDateSelect(): (Date) -> Unit {
        return {
            selectedBirthDate = it
            binding.birthInput.text = it.toStringFormat()
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            nextButton.onSafeClick = {
                if (!genderInput.isVisible) hideKeyboardAction()
                else validate()
            }
            genderInput.onClick = {
                genderSheet.show(childFragmentManager, genderSheet.tag)
            }
            birthInput.onClick = {
                datePicker.show(childFragmentManager)
            }
            genderSheet.itemClick = {
                selectedGender = it
                genderInput.text = getString(it.line)
            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.saveSuccessLiveData) {
            navigateAction()
        }
        observe(viewModel.loadingMode) {
            binding.nextButton.toggleActionEnabled(it)
        }
        observe(viewModel.errorLiveData) {
            showMessage(it.exception.message)
        }
    }

    private fun validate() {
        with(binding) {
            when {
                nameInput.text.isBlank() -> nameInput.setError()
                genderInput.text.isBlank() -> genderInput.setError()
                birthInput.text.isBlank() -> birthInput.setError()
                else -> {
                    viewModel.saveUserData(getUserData())
                }
            }
        }
    }

    private fun getUserData() =
        UpdateUserRequest(
            name = binding.nameInput.text,
            gender = selectedGender.gender,
            birthday = selectedBirthDate.toServerFormat(),
            look_for = selectedGender.aim
        )

    override var customBackNavigation: Boolean = true

    override fun onCustomBackNavigation() {}
}