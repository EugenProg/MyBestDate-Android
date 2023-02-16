package com.bestDate.view.bottomSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.SheetShareMainProfileBinding
import com.bestDate.view.base.BaseBottomSheet

class ShareMainProfileBottomSheet : BaseBottomSheet<SheetShareMainProfileBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetShareMainProfileBinding =
        { inflater, parent, attach ->
            SheetShareMainProfileBinding.inflate(
                inflater,
                parent,
                attach
            )
        }

    var shareClick: (() -> Unit)? = null

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            shareBox.setOnSaveClickListener {
                dismiss()
                shareClick?.invoke()
            }
        }
    }
}