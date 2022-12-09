package com.bestDate.presentation.start

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.data.utils.Logger
import com.bestDate.databinding.FragmentStartBinding
import com.bestDate.presentation.auth.AuthFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartFragment : BaseVMFragment<FragmentStartBinding, StartViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentStartBinding =
        { inflater, parent, attach -> FragmentStartBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<StartViewModel> = StartViewModel::class.java

    override fun onInit() {
        super.onInit()
        when {
            viewModel.isFirstEnter() -> {
                navController.navigate(StartFragmentDirections.actionStartToOnboardStart())
            }
            !viewModel.isRefreshTokenValid() -> {
                navController.navigate(StartFragmentDirections.actionStartToAuth())
            }
            else -> {
                viewModel.refreshToken()
            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.refreshSuccessLiveData.observe(viewLifecycleOwner) {
            viewModel.refreshUser()
        }
        viewModel.errorLive.observe(viewLifecycleOwner) {
            Logger.print("refresh exception: ${it.exception.message}")
            navController.navigate(StartFragmentDirections.actionStartToAuth())
        }
        viewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                val language = getString(R.string.app_language)
                if (language != it.language) {
                    viewModel.changeLanguage(language)
                } else {
                    chooseRoute()
                }
            }
        }
        viewModel.updateLanguageSuccessLiveData.observe(viewLifecycleOwner) {
            chooseRoute()
        }
    }

    private fun chooseRoute() {
        when {
            viewModel.user.value?.hasNoPhotos() == true -> {
                navController.navigate(AuthFragmentDirections.actionAuthFragmentToProfilePhotoEditingFragment())
            }
            viewModel.user.value?.questionnaireEmpty() == true -> {
                navController.navigate(AuthFragmentDirections.actionAuthFragmentToQuestionnaireFragment())
            }
            else -> {
                navController.navigate(AuthFragmentDirections.actionAuthFragmenToMain())
            }
        }
    }

}