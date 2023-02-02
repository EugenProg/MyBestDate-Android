package com.bestDate.presentation.start

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.extension.postDelayed
import com.bestDate.data.utils.Logger
import com.bestDate.databinding.FragmentStartBinding
import com.bestDate.presentation.base.BaseVMFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartFragment : BaseVMFragment<FragmentStartBinding, StartViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentStartBinding =
        { inflater, parent, attach -> FragmentStartBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<StartViewModel> = StartViewModel::class.java

    private var tokenIsRefreshed: Boolean = false
    override val statusBarColor = R.color.bg_main
    override val navBarColor = R.color.bg_main

    override fun onInit() {
        super.onInit()
        when {
            viewModel.isFirstEnter() -> {
                postDelayed({
                    navController.navigate(StartFragmentDirections.actionStartToOnboardStart())
                }, 1600)
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
        observe(viewModel.refreshSuccessLiveData) {
            tokenIsRefreshed = true
            viewModel.refreshUser()
        }
        observe(viewModel.errorLiveData) {
            Logger.print("refresh exception: ${it.exception.message}")
            navController.navigate(StartFragmentDirections.actionStartToAuth())
        }
        observe(viewModel.user) {
            if (it != null && tokenIsRefreshed) {
                val language = getString(R.string.app_locale)
                if (language != it.language) {
                    viewModel.changeLanguage(language)
                } else {
                    chooseRoute()
                }
            }
        }
        observe(viewModel.updateLanguageSuccessLiveData) {
            chooseRoute()
        }
    }

    private fun chooseRoute() {
        when {
            viewModel.user.value?.hasNoPhotos() == true -> {
                navController.navigate(StartFragmentDirections.actionStartToProfilePhotoEditing())
            }
            viewModel.user.value?.questionnaireEmpty() == true -> {
                navController.navigate(StartFragmentDirections.actionStartToQuestionnaire())
            }
            else -> {
                viewModel.startPusher()
                navController.navigate(StartFragmentDirections.actionStartToMainGraph())
            }
        }
    }
}