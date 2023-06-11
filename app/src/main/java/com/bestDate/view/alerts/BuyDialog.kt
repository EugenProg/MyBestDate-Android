package com.bestDate.view.alerts

import android.view.Gravity
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import com.bestDate.R
import com.bestDate.data.extension.getDialog
import com.bestDate.data.extension.hideWithScaleAnimation
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.extension.showWithScaleAnimation
import com.bestDate.databinding.DialogBuyBinding

fun FragmentActivity.showBuyDialog(
    type: BuyDialogType,
    coinsAmount: String,
    buyForCoinsAction: (() -> Unit),
    watchVideoAction: (() -> Unit),
    buySubscriptionAction: (() -> Unit)
) {
    val binding = DialogBuyBinding.inflate(layoutInflater)

    val dialog = getDialog(binding.root, position = Gravity.CENTER)

    with(binding) {
        box.showWithScaleAnimation {
            title.text = getString(type.title)

            coinsText.text = getString(R.string.buy_for_100_coins, coinsAmount)
            coinsDescription.text = getString(type.description)
            watchDescription.text = getString(type.description)
        }

        watchButton.setOnSaveClickListener {

        }

        buyForCoinsBtn.setOnSaveClickListener {

        }

        buyButton.setOnSaveClickListener {

        }

        closeBtn.setOnSaveClickListener {
            dialog.hideWithScaleAnimation(box, this@showBuyDialog)
        }
    }
}

enum class BuyDialogType(@StringRes val title: Int, @StringRes val description: Int) {
    MESSAGE(R.string.you_have_reached_your_message_sending_limit, R.string.for_sending_one_message),
    INVITATION(
        R.string.you_have_reached_your_invitation_sending_limit,
        R.string.for_sending_one_invitation
    )
}