package com.bestDate.view.bottomSheet.photoSettingsSheet

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bestDate.R
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.ProfileImage
import com.bestDate.databinding.SheetPhotoSetingsBinding
import com.bestDate.presentation.profilePhotoEditor.ProfilePhotoViewModel
import com.bestDate.view.base.BaseBottomSheet
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoSettingsSheet: BaseBottomSheet<SheetPhotoSetingsBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetPhotoSetingsBinding =
        { inflater, parent, attach -> SheetPhotoSetingsBinding.inflate(inflater, parent, attach) }
    private val viewModel: ProfilePhotoViewModel by viewModels()

    private var selectedImage: ProfileImage? = null

    override fun onInit() {
        super.onInit()
        with(binding) {
            profilePhotoSwitch.isVisible = selectedImage?.main == false

            topFiftySwitch.hint = getString(R.string.take_part_in_a_contest)
            topFiftySwitch.text = getString(R.string.top_50).uppercase()

            profilePhotoSwitch.hint = getString(R.string.set_as_a_profile_photo)
            profilePhotoSwitch.text = getString(R.string.main_picture)

            safeButton.title = getString(R.string.save_changes)
            setUserData()
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            topFiftySwitch.onInfoClick = { showMessage(getString(R.string.top_50).uppercase()) }

            safeButton.onSafeClick = {
                viewModel.updatePhotoStatus(
                    selectedImage?.id.orZero,
                    profilePhotoSwitch.checked,
                    topFiftySwitch.checked
                )
            }

            deleteButton.onSafeClick = {
                viewModel.deletePhoto(selectedImage?.id.orZero)
            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.updateSettingsLiveData.observe(viewLifecycleOwner) {
            dismiss()
        }
        viewModel.updateLoadingLiveData.observe(viewLifecycleOwner) {
            binding.safeButton.toggleActionEnabled(it)
        }
        viewModel.deleteLiveData.observe(viewLifecycleOwner) {
            dismiss()
        }
        viewModel.deleteLoadingLiveData.observe(viewLifecycleOwner) {
            binding.deleteButton.toggleActionEnabled(it)
        }
        viewModel.errorLive.observe(viewLifecycleOwner) {
            binding.safeButton.toggleActionEnabled(false)
            binding.deleteButton.toggleActionEnabled(false)
            showMessage(it.exception.message)
        }
    }

    fun setSelectedImage(selectedImage: ProfileImage) {
        this.selectedImage = selectedImage.copy()
    }

    private fun setUserData() {
        Glide.with(requireContext())
            .load(selectedImage?.thumb_url)
            .into(binding.image)

        binding.topFiftySwitch.checked = selectedImage?.top ?: false
        binding.profilePhotoSwitch.checked = selectedImage?.main ?: false
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        selectedImage?.top = binding.topFiftySwitch.checked
    }
}