package com.bestDate.view.alerts

import android.view.Gravity
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import com.bestDate.R
import com.bestDate.data.extension.getDialog
import com.bestDate.data.extension.hideWithScaleAnimation
import com.bestDate.data.extension.orZero
import com.bestDate.data.extension.safetyToInt
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.extension.showWithScaleAnimation
import com.bestDate.data.utils.ads.RewardedAdUtil
import com.bestDate.data.utils.subscription.SubscriptionUtil
import com.bestDate.databinding.DialogBuyBinding
import com.bestDate.presentation.main.UserUseCase
import javax.inject.Inject

class BanningDialog @Inject constructor(
    private var subscriptionUtil: SubscriptionUtil,
    private var userUseCase: UserUseCase
) {
    var buySubscriptionAction: (() -> Unit)? = null
    var buyForCoinsAction: ((BuyDialogType) -> Unit)? = null
    var sendAction: (() -> Unit)? = null
    var closeAction: (() -> Unit)? = null

    private lateinit var rewardedAdUtil: RewardedAdUtil

    fun show(activity: FragmentActivity, type: BuyDialogType) {
        setUpRewardAdUtil(activity)
        val coinsBalance = userUseCase.coinsCount.value?.safetyToInt()
        val coinsPrice =
            if (type == BuyDialogType.MESSAGE) subscriptionUtil.getAdditionMessagePrice()
            else subscriptionUtil.getAdditionInvitationPrice()
        activity.showBuyDialog(
            type = type,
            coinsAmount = coinsPrice.toString(),
            buySubscriptionAction = {
                buySubscriptionAction?.invoke()
            }, watchVideoAction = {
                rewardedAdUtil.start()
            }, buyForCoinsAction = {
                if (coinsBalance.orZero >= coinsPrice) {
                    activity.showBuyForCoinsDialog(
                        type,
                        coinsBalance.toString(),
                        coinsPrice.toString()
                    ) {
                        buyForCoinsAction?.invoke(type)
                    }
                } else {
                    activity.showNotEnoughCoinsDialog(
                        coinsBalance.toString(),
                        coinsPrice.toString()
                    ) {
                        buySubscriptionAction?.invoke()
                    }
                }
            }
        )
    }

    private fun setUpRewardAdUtil(activity: FragmentActivity) {
        rewardedAdUtil = RewardedAdUtil(activity)

        rewardedAdUtil.earnedReward = {
            sendAction?.invoke()
        }
        rewardedAdUtil.failed = {
            closeAction?.invoke()
        }
    }
}

fun FragmentActivity.showBuyDialog(
    type: BuyDialogType,
    coinsAmount: String,
    buyForCoinsAction: () -> Unit,
    watchVideoAction: () -> Unit,
    buySubscriptionAction: () -> Unit
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
            dialog.hideWithScaleAnimation(box, this@showBuyDialog) {
                watchVideoAction.invoke()
            }
        }

        buyForCoinsBtn.setOnSaveClickListener {
            dialog.hideWithScaleAnimation(box, this@showBuyDialog) {
                buyForCoinsAction.invoke()
            }
        }

        buyButton.setOnSaveClickListener {
            dialog.hideWithScaleAnimation(box, this@showBuyDialog) {
                buySubscriptionAction.invoke()
            }
        }

        closeBtn.setOnSaveClickListener {
            dialog.hideWithScaleAnimation(box, this@showBuyDialog)
        }
    }
}

enum class BuyDialogType(
    @StringRes val title: Int,
    @StringRes val description: Int,
    var buyName: String
) {
    MESSAGE(
        R.string.you_have_reached_your_message_sending_limit,
        R.string.for_sending_one_message,
        "message"
    ),
    INVITATION(
        R.string.you_have_reached_your_invitation_sending_limit,
        R.string.for_sending_one_invitation,
        "invitation"
    )
}