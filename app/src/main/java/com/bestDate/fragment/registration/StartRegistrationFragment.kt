package com.bestDate.fragment.registration

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.setPaddingBottom
import com.bestDate.data.extension.toStringFormat
import com.bestDate.data.locale.RegistrationDataHolder
import com.bestDate.data.utils.ViewUtils
import com.bestDate.databinding.FragmentStartRegistrationBinding
import com.bestDate.fragment.BaseFragment
import com.bestDate.view.CalendarView
import com.bestDate.view.bottomSheet.genderSheet.GenderSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class StartRegistrationFragment : BaseFragment<FragmentStartRegistrationBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentStartRegistrationBinding =
        { inflater, parent, attach -> FragmentStartRegistrationBinding.inflate(inflater, parent, attach) }

    override val navBarColor = R.color.main_dark
    override val statusBarLight = true

    private var genderSheetDialog: GenderSheetDialog = GenderSheetDialog()
    private lateinit var datePicker: MaterialDatePicker<Long>

    override fun onInit() {
        super.onInit()
        with(binding) {
            nameInput.hint = getString(R.string.name)
            nameInput.icon = R.drawable.ic_user
            nameInput.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS

            genderInput.hint = getString(R.string.gender)
            genderInput.icon = R.drawable.ic_gender

            birthInput.hint = getString(R.string.birth_date)
            birthInput.icon = R.drawable.ic_calendar

            nextButton.title = getString(R.string.next)

            nameInput.text = RegistrationDataHolder.username.orEmpty()
            birthInput.text = RegistrationDataHolder.birthdate?.toStringFormat().orEmpty()
            genderInput.text = RegistrationDataHolder.gender.orEmpty()
        }

        datePicker = CalendarView().getDateSelectCalender(
            getString(R.string.birth_date),
            RegistrationDataHolder.birthdate,
            birthDateSelect()
        )
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            backButton.setOnClickListener {
                navController.popBackStack()
            }
            nextButton.onClick = {
                if (!genderInput.isVisible) hideKeyboardAction()
                else validate()
            }
            loginButton.setOnClickListener {
                navController.navigate(StartRegistrationFragmentDirections
                    .actionStartRegistrationFragmentToAuthFragment())
            }
            genderInput.onClick = {
                genderSheetDialog.show(childFragmentManager, genderSheetDialog.tag)
            }
            birthInput.onClick = {
                datePicker.show(childFragmentManager, datePicker.tag)
            }
            genderSheetDialog.itemClick = {
                RegistrationDataHolder.gender = it
                genderInput.text = it
            }
        }
    }

    private fun birthDateSelect(): (Date) -> Unit {
        return {
            RegistrationDataHolder.birthdate = it
            binding.birthInput.text = it.toStringFormat()
        }
    }

    private fun validate() {
        with(binding) {
            when {
                nameInput.text.isBlank() -> nameInput.setError()
                genderInput.text.isBlank() -> genderInput.setError()
                birthInput.text.isBlank() -> birthInput.setError()
                else -> {
                    RegistrationDataHolder.username = nameInput.text
                    navController.navigate(StartRegistrationFragmentDirections
                        .actionStartRegistrationFragmentToContinueRegistrationFragment())
                }
            }
        }
    }

    override fun scrollAction() {
        super.scrollAction()
        with(binding) {
            ViewUtils().hideViews(socialContainer, loginButton, genderInput, birthInput)
            inputContainer.setPaddingBottom(0)
            scroll.fullScroll(View.FOCUS_DOWN)
            if (nameInput.hasFocus) nameInput.setFocus()
        }
    }

    override fun hideAction() {
        super.hideAction()
        with(binding) {
            ViewUtils().showViews(socialContainer, loginButton, genderInput, birthInput)
            inputContainer.setPaddingBottom(90)
        }
    }
}