package com.bestDate.presentation.passRecovery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.databinding.FragmentPassRecoverySetNewBinding
import com.bestDate.presentation.base.BaseVMFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PassRecoverySetNewFragment :
    BaseVMFragment<FragmentPassRecoverySetNewBinding, PassRecoveryViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPassRecoverySetNewBinding =
        { inflater, parent, attach ->
            FragmentPassRecoverySetNewBinding.inflate(
                inflater,
                parent,
                attach
            )
        }

    override val viewModelClass: Class<PassRecoveryViewModel> = PassRecoveryViewModel::class.java

    override val statusBarLight = true

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            backButton.onClick = { navController.popBackStack() }

            saveButton.onClick = {
                validate()
            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.loadingLiveData) {
            binding.saveButton.toggleActionEnabled(it)
        }
        observe(viewModel.recoveryLiveData) {
            chooseRoute()
        }
        observe(viewModel.errorLiveData) {
            showMessage(it.exception.message)
        }
    }

    private fun chooseRoute() {
        when {
            viewModel.user.value?.hasNoPhotos() == true && viewModel.getSkipImageCount() < 3 -> {
                navController.navigate(
                    PassRecoverySetNewFragmentDirections.actionGlobalProfilePhotoEditingFragment()
                )
            }
            viewModel.user.value?.questionnaireEmpty() == true &&
                    viewModel.getSkipQuestionnaireCount() < 3 -> {
                navController.navigate(
                    PassRecoverySetNewFragmentDirections.actionGlobalQuestionnaireFragment()
                )
            }
            else -> {
                navController.navigate(
                    PassRecoverySetNewFragmentDirections.actionGlobalMainNavGraph()
                )
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
        if (binding.passInput.text.isBlank() ||
            binding.passInput.text.length < 6) binding.passInput.setError()
        else {
            PassRecoveryDataHolder.password = binding.passInput.text
            viewModel.confirmPassRecovery(getString(R.string.app_locale))
        }
    }
}