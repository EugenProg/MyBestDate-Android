package com.bestDate.presentation.main.userProfile.personalData.changePassword

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.databinding.FragmentChangePasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment :
    BaseVMFragment<FragmentChangePasswordBinding, ChangePasswordViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentChangePasswordBinding =
        { inflater, parent, attach ->
            FragmentChangePasswordBinding.inflate(
                inflater,
                parent,
                attach
            )
        }
    override val viewModelClass: Class<ChangePasswordViewModel> =
        ChangePasswordViewModel::class.java
    override val statusBarColor = R.color.bg_main

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.toolbar.backClick = {
            goBack()
        }
        binding.passChangeButton.onClick = {
            validate()
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.successLiveData.observe(viewLifecycleOwner) {
            showMessage(R.string.save_successfully)
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            showMessage(it.exception.message)
        }
    }

    private fun validate() {
        with(binding) {
            when {
                oldPassInput.getText()
                    .isBlank() || oldPassInput.getText().length < 6 -> oldPassInput.setError()
                newPassInput.getText()
                    .isBlank() || newPassInput.getText().length < 6 -> newPassInput.setError()
                newPassInput.getText() !=
                        passConfirmationInput.getText() -> passConfirmationInput.setError()
                else -> viewModel.changePassword(oldPassInput.getText(), newPassInput.getText())
            }
        }
    }
}