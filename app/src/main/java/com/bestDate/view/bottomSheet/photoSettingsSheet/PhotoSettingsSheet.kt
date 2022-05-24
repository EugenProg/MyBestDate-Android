package com.bestDate.view.bottomSheet.photoSettingsSheet

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.Image
import com.bestDate.databinding.SheetPhotoSetingsBinding
import com.bestDate.view.base.BaseBottomSheet
import com.bumptech.glide.Glide

class PhotoSettingsSheet: BaseBottomSheet<SheetPhotoSetingsBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetPhotoSetingsBinding =
        { inflater, parent, attach -> SheetPhotoSetingsBinding.inflate(inflater, parent, attach) }

    var selectedImage: Image? = null

    var setMainClick: ((Image) -> Unit)? = null
    var deleteClick: ((Image) -> Unit)? = null
    var editClick: ((Image) -> Unit)? = null

    override fun onInit() {
        super.onInit()
        with(binding) {
            sympathySwitch.hint = getString(R.string.take_part_in_a_contest)
            sympathySwitch.text = getString(R.string.mutual_sympathy)

            topFiftySwitch.hint = getString(R.string.take_part_in_a_contest)
            topFiftySwitch.text = getString(R.string.top_50)

            profilePhotoSwitch.hint = getString(R.string.set_as_a_profile_photo)
            profilePhotoSwitch.text = getString(R.string.main_picture)

            safeButton.title = getString(R.string.save_changes)

            Glide.with(requireContext())
                .load(selectedImage?.bitmap ?: selectedImage?.uri)
                .into(binding.image)

            topFiftySwitch.checked = selectedImage?.topFifty ?: false
            sympathySwitch.checked = selectedImage?.mutualSympathy ?: false
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            sympathySwitch.onInfoClick = { showMessage(getString(R.string.mutual_sympathy)) }
            topFiftySwitch.onInfoClick = { showMessage(getString(R.string.top_50)) }

            safeButton.onSafeClick = {
                selectedImage?.let { setMainClick?.invoke(it) }
            }

            editButton.setOnSaveClickListener { selectedImage?.let { im -> editClick?.invoke(im) } }
            deleteButton.setOnSaveClickListener { selectedImage?.let { im-> deleteClick?.invoke(im) } }
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        selectedImage?.mutualSympathy = binding.sympathySwitch.checked
        selectedImage?.topFifty = binding.topFiftySwitch.checked
    }
}