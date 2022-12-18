package com.bestDate.presentation.main.userProfile.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.data.model.SettingsType
import com.bestDate.databinding.FragmentSettingsBinding
import com.bestDate.view.alerts.LoaderDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment: BaseVMFragment<FragmentSettingsBinding, SettingsViewModel>() {
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

            }
            changeLanguageButton.onClick = {

            }
            deleteProfileButton.onClick = {

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
        viewModel.user.observe(viewLifecycleOwner) {
            binding.changeLanguageButton.buttonTitle = it?.language
        }
        viewModel.userSettings.observe(viewLifecycleOwner) {
            it?.let {
                binding.blockingMessagesSwitch.setChecked(it.block_messages)
                binding.matchParticipationSwitch.setChecked(it.matchParticipation)
                binding.notificationSettings.setNotificationSettings(it.notifications)
            }
        }
        viewModel.loadingMode.observe(viewLifecycleOwner) {
            if (it) loader.startLoading()
            else loader.stopLoading()
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            loader.stopLoading()
            showMessage(it.exception.message)
        }
    }
}