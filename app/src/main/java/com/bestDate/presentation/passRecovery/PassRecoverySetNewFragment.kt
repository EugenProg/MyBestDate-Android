package com.bestDate.presentation.passRecovery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.FragmentPassRecoverySetNewBinding
import com.bestDate.presentation.auth.AuthFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PassRecoverySetNewFragment : BaseVMFragment<FragmentPassRecoverySetNewBinding, PassRecoveryViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPassRecoverySetNewBinding =
        { inflater, parent, attach -> FragmentPassRecoverySetNewBinding.inflate(inflater, parent, attach)}

    override val viewModelClass: Class<PassRecoveryViewModel> = PassRecoveryViewModel::class.java

    override val statusBarLight = true

    override fun onInit() {
        super.onInit()
        with(binding) {
            passInput.isPasswordField = true
            passInput.hint = getString(R.string.enter_a_new_password)

            saveButton.title = getString(R.string.install_and_login)
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            backButton.setOnSaveClickListener { navController.popBackStack() }

            saveButton.onClick = {
                validate()

            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.saveButton.toggleActionEnabled(it)
        }
        viewModel.recoveryLiveData.observe(viewLifecycleOwner) {
            chooseRoute()
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            showMessage(it.exception.message)
        }
    }

    private fun chooseRoute() {
        when {
            viewModel.user.value?.hasNoPhotos() == true -> {
                navController.navigate(PassRecoverySetNewFragmentDirections.actionGlobalProfilePhotoEditingFragment())
            }
            viewModel.user.value?.questionnaireEmpty() == true -> {
                navController.navigate(PassRecoverySetNewFragmentDirections.actionGlobalQuestionnaireFragment())
            }
            else -> {
                navController.navigate(PassRecoverySetNewFragmentDirections.actionGlobalMainNavGraph())
            }
        }
    }

    override fun scrollAction() {
        super.scrollAction()
        with(binding) {
            scroll.fullScroll(View.FOCUS_DOWN)
            passInput.setFocus()
        }
    }

    private fun validate() {
        if (binding.passInput.text.isBlank() || binding.passInput.text.length < 6) binding.passInput.setError()
        else {
            PassRecoveryDataHolder.password = binding.passInput.text
            viewModel.confirmPassRecovery()
        }
    }
}