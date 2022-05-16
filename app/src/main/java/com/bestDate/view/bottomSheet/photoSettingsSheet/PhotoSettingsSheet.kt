package com.bestDate.view.bottomSheet.photoSettingsSheet

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.SheetPhotoSetingsBinding
import com.bestDate.view.base.BaseBottomSheet
import com.bumptech.glide.Glide

class PhotoSettingsSheet: BaseBottomSheet<SheetPhotoSetingsBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetPhotoSetingsBinding =
        { inflater, parent, attach -> SheetPhotoSetingsBinding.inflate(inflater, parent, attach) }

    var selectedImage: Uri? = null

    var saveClick: ((Uri) -> Unit)? = null
    var deleteClick: ((Uri) -> Unit)? = null
    var editClick: ((Uri) -> Unit)? = null

    override fun onInit() {
        super.onInit()
        with(binding) {
            sympathySwitch.hint = getString(R.string.take_part_in_a_contest)
            sympathySwitch.text = getString(R.string.mutual_sympathy)

            topFiftySwitch.hint = getString(R.string.take_part_in_a_contest)
            topFiftySwitch.text = getString(R.string.top_50)

            safeButton.title = getString(R.string.set_the_main_photo)

            Glide.with(requireContext()).load(selectedImage).into(binding.image)
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            sympathySwitch.onInfoClick = { showMessage(getString(R.string.mutual_sympathy)) }
            sympathySwitch.onChecked = {

            }

            topFiftySwitch.onInfoClick = { showMessage(getString(R.string.top_50)) }
            topFiftySwitch.onChecked = {

            }

            safeButton.onClick = {
                selectedImage?.let { saveClick?.invoke(it) }
            }

            editButton.setOnSaveClickListener { selectedImage?.let { im -> editClick?.invoke(im) } }
            deleteButton.setOnSaveClickListener { selectedImage?.let { im-> deleteClick?.invoke(im) } }
        }
    }
}