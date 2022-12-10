package com.bestDate.view.bottomSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.databinding.SheetNotCorrectPhotoBinding
import com.bestDate.view.base.BaseBottomSheet

class NotCorrectPhotoSheet: BaseBottomSheet<SheetNotCorrectPhotoBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetNotCorrectPhotoBinding =
        { inflater, parent, attach -> SheetNotCorrectPhotoBinding.inflate(inflater, parent, attach) }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.clearButton.onClick = {
            this.dismiss()
        }
    }
}