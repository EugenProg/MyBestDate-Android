package com.bestDate.view.bottomSheet.anotherProfileAdditional

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.SheetAnotherProfileAdditionalyBinding
import com.bestDate.view.base.BaseBottomSheet

class AnotherProfileAdditionalBottomSheet(var isBlocked: Boolean) :
    BaseBottomSheet<SheetAnotherProfileAdditionalyBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetAnotherProfileAdditionalyBinding =
        { inflater, parent, attach ->
            SheetAnotherProfileAdditionalyBinding.inflate(
                inflater,
                parent,
                attach
            )
        }

    var shareClick: (() -> Unit)? = null
    var blockClick: (() -> Unit)? = null

    override fun onInit() {
        super.onInit()
        binding.blockTitle.text = getString(if (isBlocked) R.string.unblock_profile else R.string.block_profile)
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            shareBox.setOnSaveClickListener {
                dismiss()
                shareClick?.invoke()
            }
            blockBox.setOnSaveClickListener {
                dismiss()
                blockClick?.invoke()
            }
        }
    }
}