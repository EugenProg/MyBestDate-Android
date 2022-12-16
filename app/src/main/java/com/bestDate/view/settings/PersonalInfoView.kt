package com.bestDate.view.settings

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.bestDate.R
import com.bestDate.data.extension.show
import com.bestDate.data.extension.toServerFormat
import com.bestDate.data.extension.toStringFormat
import com.bestDate.data.model.UpdateUserRequest
import com.bestDate.databinding.ViewPersonalInfoBinding
import com.bestDate.db.entity.UserDB
import com.bestDate.presentation.registration.GenderType
import com.bestDate.view.CalendarView
import com.bestDate.view.bottomSheet.genderSheet.GenderSheet
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class PersonalInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewPersonalInfoBinding =
        ViewPersonalInfoBinding.inflate(LayoutInflater.from(context), this)

    var saveUserData: ((UpdateUserRequest) -> Unit)? = null
    var sendEmailCode: ((String) -> Unit)? = null
    var saveUserEmail: ((email: String, code: String) -> Unit)? = null
    var sendPhoneCode: ((String) -> Unit)? = null
    var saveUserPhone: ((phone: String, code: String) -> Unit)? = null
    var allChangesAreSaved: (() -> Unit)? = null

    private var fragmentManager: FragmentManager? = null
    private var genderSheet: GenderSheet = GenderSheet()
    private var datePicker: MaterialDatePicker<Long>? = null
    private var birthdate: Date? = null
    private var gender: GenderType? = null

    private var user: UserDB? = null
    private var userIsChanged: Boolean = false
    private var emailIsChanged: Boolean = false
    private var phoneIsChanged: Boolean = false

    init {
        with(binding) {
            saveButton.onClick = {
                when {
                    userIsChanged -> validateUserSave()
                    emailIsChanged -> validateEmailSave()
                    phoneIsChanged -> validatePhoneSave()
                }
            }
            nameInput.onTextChangeListener = {
                if (user != null) validateButtonActive()
            }
            genderInput.onTextChangeListener = {
                if (user != null) validateButtonActive()
            }
            birthInput.onTextChangeListener = {
                if (user != null) validateButtonActive()
            }
            emailInput.onTextChangeListener = {
                if (user != null) validateButtonActive()
            }
            phoneInput.onTextChangeListener = {
                if (user != null) validateButtonActive()
            }
            emailOtp.onTextChangeListener = {
                if (it.length >= 6) {
                    saveUserEmail?.invoke(emailInput.getText(), it)
                }
            }
            phoneOtp.onTextChangeListener = {
                if (it.length >= 6) {
                    saveUserPhone?.invoke(phoneInput.getText(), it)
                }
            }
            genderInput.onClick = {
                fragmentManager?.let { genderSheet.show(it, genderSheet.tag) }
            }
            genderSheet.itemClick = {
                gender = it
                genderInput.setText(context.getString(it.line))
            }
            birthInput.onClick = {
                fragmentManager?.let { datePicker?.show(it) }
            }
        }
    }

    fun setUser(user: UserDB?, fm: FragmentManager) {
        fragmentManager = fm
        gender = user?.getGenderType()
        birthdate = user?.getBirthDate()
        with(binding) {
            nameInput.setText(user?.name)
            genderInput.setText(gender?.let { context.getString(it.line) })
            birthInput.setText(birthdate?.toStringFormat())
            if (!emailIsChanged) emailInput.setText(user?.email)
            if (!phoneIsChanged) phoneInput.setText(user?.phone)
        }
        this.user = user
        validateButtonActive()

        datePicker = CalendarView().getDateSelectCalender(
            context.getString(R.string.birth_date), birthdate, birthDateSelect()
        )
    }

    fun showEmailOtp() {
        binding.emailOtp.isVisible = true
    }

    fun showPhoneOtp() {
        binding.phoneOtp.isVisible = true
    }

    fun userDataIsSaved() {
        validateButtonActive()
        when{
            emailIsChanged -> validateEmailSave()
            phoneIsChanged -> validatePhoneSave()
            else -> allChangesAreSaved?.invoke()
        }
    }

    fun emailIsSaved() {
        validateButtonActive()
        binding.emailOtp.isVisible = false
        if (phoneIsChanged) validatePhoneSave()
        else allChangesAreSaved?.invoke()
    }

    fun phoneIsSaved() {
        validateButtonActive()
        binding.phoneOtp.isVisible = false
        binding.saveButton.toggleActionEnabled(false)
        allChangesAreSaved?.invoke()
    }

    private fun validateUserSave() {
        with(binding) {
            when {
                nameInput.getText().isBlank() -> nameInput.setError()
                genderInput.getText().isBlank() -> genderInput.setError()
                birthInput.getText().isBlank() -> birthInput.setError()
                else -> saveUserData?.invoke(
                    UpdateUserRequest(
                        name = nameInput.getText(),
                        gender = gender?.gender ?: user?.gender,
                        birthday = birthdate?.toServerFormat(),
                        look_for = gender?.aim ?: user?.look_for
                    )
                )
            }
        }
    }

    private fun validateEmailSave() {
        if (binding.emailInput.getText().isBlank()) binding.emailInput.setError()
        else sendEmailCode?.invoke(binding.emailInput.getText())
    }

    private fun validatePhoneSave() {
        if (binding.phoneInput.getText().isBlank()) binding.phoneInput.setError()
        else sendPhoneCode?.invoke(binding.phoneInput.getText())
    }

    private fun birthDateSelect(): (Date) -> Unit {
        return {
            birthdate = it
            binding.birthInput.setText(it.toStringFormat())
        }
    }

    private fun validateButtonActive() {
        userIsChanged = user?.name != binding.nameInput.getText() ||
                user?.getLocalizeGender()?.let { context.getString(it) } != binding.genderInput.getText() ||
                user?.getFormattedBirthday() != binding.birthInput.getText()

        emailIsChanged = user?.email.orEmpty() != binding.emailInput.getText()
        phoneIsChanged = user?.phone.orEmpty() != binding.phoneInput.getText()

        binding.saveButton.setActive(userIsChanged || emailIsChanged || phoneIsChanged)
    }
}