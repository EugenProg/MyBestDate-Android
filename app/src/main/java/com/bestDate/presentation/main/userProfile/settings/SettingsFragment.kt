package com.bestDate.presentation.main.userProfile.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment: BaseVMFragment<FragmentSettingsBinding, SettingsViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingsBinding =
        { inflater, parent, attach -> FragmentSettingsBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<SettingsViewModel> = SettingsViewModel::class.java

    override val statusBarColor = R.color.bg_main

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
        }
    }
}