package com.bestDate.presentation.main.userProfile.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.extension.show
import com.bestDate.data.model.SettingsType
import com.bestDate.databinding.FragmentSettingsBinding
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.view.alerts.LoaderDialog
import com.bestDate.view.alerts.showDeleteDialog
import com.bestDate.view.bottomSheet.languageSheet.LanguageSelectSheet
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseVMFragment<FragmentSettingsBinding, SettingsViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingsBinding =
        { inflater, parent, attach -> FragmentSettingsBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<SettingsViewModel> = SettingsViewModel::class.java

    override val statusBarColor = R.color.bg_main
    private lateinit var loader: LoaderDialog

    override fun onInit() {
        super.onInit()
        viewModel.refreshUserSettings()
        loader = LoaderDialog(requireActivity())
    }


    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            toolbar.backClick = {
                goBack()
            }
            blockedListButton.onClick = {
                navController.navigate(SettingsFragmentDirections.actionSettingsToBlockedUsers())
            }
            changeLanguageButton.onClick = {
                showLanguageSelectSheet()
            }
            deleteProfileButton.onClick = {
                requireActivity().showDeleteDialog(
                    getString(R.string.all_your_data_will_be_deleted)
                ) {
                    loader.startLoading()
                    viewModel.deleteUserProfile()
                }
            }

            notificationSettings.checkAction = { checked, settingsType ->
                viewModel.updateUserSettings(settingsType, checked)
            }
            blockingMessagesSwitch.checkAction = {
                viewModel.updateUserSettings(SettingsType.MESSAGES, it)
            }
            matchParticipationSwitch.checkAction = {
                viewModel.updateUserSettings(SettingsType.MATCHES, it)
            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.userSettings) {
            binding.blockingMessagesSwitch.setChecked(it.block_messages)
            binding.matchParticipationSwitch.setChecked(it.matchParticipation)
            binding.notificationSettings.setNotificationSettings(it.notifications)
        }
        observe(viewModel.loadingMode) {
            if (it && viewModel.userSettings.value == null) loader.startLoading()
            else loader.stopLoading()
        }
        observe(viewModel.errorLiveData) {
            loader.stopLoading()
            showMessage(it.exception.message)
        }
        observe(viewModel.deleteSuccessLiveData) {
            loader.stopLoading()
            navController.navigate(SettingsFragmentDirections.actionGlobalAuthFragment())
        }
        observe(viewModel.languageSaveLiveData) {
            loader.stopLoading()
            Lingver.getInstance().setLocale(requireContext(), it.settingsName)
            navController.navigate(SettingsFragmentDirections.actionRefresh())
        }
    }

    private fun showLanguageSelectSheet() {
        val languageSelectSheet = LanguageSelectSheet()
        languageSelectSheet.itemClick = {
            loader.startLoading()
            viewModel.saveLanguage(it)
        }

        languageSelectSheet.show(childFragmentManager)
    }
}