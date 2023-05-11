package com.bestDate.view.bottomSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.databinding.SheetBuySubscriptionBinding
import com.bestDate.view.base.BaseBottomSheet

class BuySubscriptionBottomSheet : BaseBottomSheet<SheetBuySubscriptionBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetBuySubscriptionBinding =
        { inflater, parent, attach ->
            SheetBuySubscriptionBinding.inflate(
                inflater,
                parent,
                attach
            )
        }

    var buyClick: (() -> Unit)? = null

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.buyButton.onClick = {
            buyClick?.invoke()
        }
    }

    override fun getTheme(): Int = R.style.DarkBottomSheetDialogTheme
}