package com.bestDate.view.alerts

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.Gravity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.bestDate.R
import com.bestDate.data.extension.getDialog
import com.bestDate.data.extension.hideWithScaleAnimation
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.extension.showWithScaleAnimation
import com.bestDate.databinding.DialogBuyForCoinsBinding
import com.bestDate.databinding.DialogNotEnoughCoinsBinding

fun FragmentActivity.showBuyForCoinsDialog(
    type: BuyDialogType,
    balance: String,
    amount: String,
    buyAction: () -> Unit
) {
    val binding = DialogBuyForCoinsBinding.inflate(layoutInflater)

    val dialog = getDialog(binding.root, position = Gravity.CENTER)

    with(binding) {
        box.showWithScaleAnimation {
            balanceView.text = balance
            priceView.text = createCoinsPriceString(this@showBuyForCoinsDialog, amount)

            coinsDescription.text = getString(type.description)
        }

        buyForCoinsBtn.setOnSaveClickListener {
            buyAction.invoke()
        }

        closeBtn.setOnSaveClickListener {
            dialog.hideWithScaleAnimation(box, this@showBuyForCoinsDialog)
        }
    }
}

fun FragmentActivity.showNotEnoughCoinsDialog(
    balance: String,
    amount: String,
    buyAction: () -> Unit
) {
    val binding = DialogNotEnoughCoinsBinding.inflate(layoutInflater)

    val dialog = getDialog(binding.root, position = Gravity.CENTER)

    with(binding) {
        box.showWithScaleAnimation {
            balanceView.text = balance
            priceView.text = createCoinsPriceString(this@showNotEnoughCoinsDialog, amount)
        }

        buyButton.setOnSaveClickListener {
            buyAction.invoke()
        }

        closeBtn.setOnSaveClickListener {
            dialog.hideWithScaleAnimation(box, this@showNotEnoughCoinsDialog)
        }
    }
}

private fun createCoinsPriceString(context: Context, price: String): SpannableString {
    val color = ContextCompat.getColor(context, R.color.white)
    val endIndex = price.length + 3
    val span = SpannableString("- $price BD Coin")
    span.setSpan(ForegroundColorSpan(color), 0, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    span.setSpan(RelativeSizeSpan(2f), 0, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    return span
}