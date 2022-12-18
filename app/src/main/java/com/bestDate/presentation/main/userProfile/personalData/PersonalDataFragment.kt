package com.bestDate.presentation.main.userProfile.personalData

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.databinding.FragmentPersonalDataBinding
import com.bestDate.view.alerts.LoaderDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalDataFragment : BaseVMFragment<FragmentPersonalDataBinding, PersonalDataViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPersonalDataBinding =
        { inflater, parent, attach ->
            FragmentPersonalDataBinding.inflate(
                inflater,
                parent,
                attach
            )
        }
    override val viewModelClass: Class<PersonalDataViewModel> = PersonalDataViewModel::class.java

    override val statusBarColor = R.color.bg_main

    private lateinit var loader: LoaderDialog

    override fun onInit() {
        super.onInit()
        loader = LoaderDialog(requireActivity())
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            toolbar.backClick = {
                goBack()
            }
            personalInfo.saveUserData = {
                loader.startLoading()
                viewModel.updateUserInfo(it)
            }
            personalInfo.sendEmailCode = {
                loader.startLoading()
                viewModel.sendEmailCode(it)
            }
            personalInfo.sendPhoneCode = {
                loader.startLoading()
                viewModel.sendPhoneCode(it)
            }
            personalInfo.saveUserEmail = { email, code ->
                loader.startLoading()
                viewModel.saveUserEmail(email, code)
            }
            personalInfo.saveUserPhone = { phone, code ->
                loader.startLoading()
                viewModel.saveUserPhone(phone, code)
            }
            personalInfo.allChangesAreSaved = {
                loader.stopLoading()
                showMessage(R.string.save_successfully)
            }
            changePassButton.onClick = {
                navController.navigate(
                    PersonalDataFragmentDirections.actionPersonalDataToChangePassword()
                )
            }
            searchLocationButton.onClick = {

            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.user.observe(viewLifecycleOwner) {
            binding.personalInfo.setUser(it, childFragmentManager)
            binding.location.text = it?.getUserLocation()
        }
        viewModel.userSaveSuccessLiveData.observe(viewLifecycleOwner) {
            binding.personalInfo.userDataIsSaved()
        }
        viewModel.emailCodeSuccessLiveData.observe(viewLifecycleOwner) {
            loader.stopLoading()
            binding.personalInfo.showEmailOtp()
        }
        viewModel.emailSaveSuccessLiveData.observe(viewLifecycleOwner) {
            binding.personalInfo.emailIsSaved()
        }
        viewModel.phoneCodeSuccessLiveData.observe(viewLifecycleOwner) {
            loader.stopLoading()
            binding.personalInfo.showPhoneOtp()
        }
        viewModel.phoneSaveSuccessLiveData.observe(viewLifecycleOwner) {
            loader.stopLoading()
            binding.personalInfo.phoneIsSaved()
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            loader.stopLoading()
            showMessage(it.exception.message)
        }
    }
}