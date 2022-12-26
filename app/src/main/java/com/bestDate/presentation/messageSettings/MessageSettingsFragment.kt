package com.bestDate.presentation.messageSettings

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.FragmentMessageSettingsBinding
import com.bestDate.presentation.base.BaseVMFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageSettingsFragment:
    BaseVMFragment<FragmentMessageSettingsBinding, MessageSettingsViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMessageSettingsBinding =
        { inflater, parent, attach -> FragmentMessageSettingsBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<MessageSettingsViewModel> = MessageSettingsViewModel::class.java

    override val statusBarColor: Int = R.color.bg_main

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            closeBtn.setOnSaveClickListener {
                viewModel.setNotFirstEnter()
                navController.navigate(MessageSettingsFragmentDirections
                    .actionMessagesSettingsToMainNavGraph())
            }
            saveButton.onClick = {
                viewModel.save(switchView.isChecked)
            }
            switchView.checkAction = {
                switchFrame.setBackgroundResource(
                    if (it) R.drawable.blue_input_shape else R.drawable.default_input_shape
                )
            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.userSettings.observe(viewLifecycleOwner) {
            binding.switchView.setChecked(it?.block_messages)
        }
        viewModel.saveLiveData.observe(viewLifecycleOwner) {
            viewModel.setNotFirstEnter()
            navController.navigate(MessageSettingsFragmentDirections
                .actionMessagesSettingsToMainNavGraph())
        }
        viewModel.loadingMode.observe(viewLifecycleOwner) {
            binding.saveButton.toggleActionEnabled(it)
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            showMessage(it.exception.message)
        }
    }
}