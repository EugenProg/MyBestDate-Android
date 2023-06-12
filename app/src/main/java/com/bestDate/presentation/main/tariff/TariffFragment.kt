package com.bestDate.presentation.main.tariff

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.android.billingclient.api.ProductDetails
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.FragmentTariffBinding
import com.bestDate.presentation.base.BaseFragment

class TariffFragment(
    private var type: TariffType,
    private val product: ProductDetails?,
    private val subscribe: (ProductDetails?) -> Unit
) : BaseFragment<FragmentTariffBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTariffBinding =
        { inflater, parent, attach -> FragmentTariffBinding.inflate(inflater, parent, attach) }

    override val statusBarColor = R.color.bg_main

    @SuppressLint("SetTextI18n")
    override fun onInit() {
        super.onInit()
        with(binding) {
            title.text = getString(type.title)
            title.setTextColor(ContextCompat.getColor(requireContext(), type.color))

            popular.isVisible = type.popular

            val price = product?.subscriptionOfferDetails?.firstOrNull()
                ?.pricingPhases?.pricingPhaseList?.firstOrNull()?.formattedPrice
            amountView.text = price
            periodView.text = getString(type.period)
            adsSwitch.setImageResource(type.switch)
            cardsSwitch.setImageResource(type.switch)
            messageSwitch.setImageResource(type.switch)

            button.setBackgroundColor(ContextCompat.getColor(requireContext(), type.color))

            button.setOnSaveClickListener {
                subscribe.invoke(product)
            }
        }
    }
}

enum class TariffType(
    @StringRes val title: Int, var popular: Boolean, var id: String,
    @StringRes var period: Int, @ColorRes var color: Int, @DrawableRes var switch: Int
) {
    MONTHLY(
        R.string.montly_plan, true, "monthly_plan", R.string.per_montly,
        R.color.bg_light_blue, R.drawable.ic_switch_blue
    ),
    THREE_MONTHS(
        R.string.three_months_plan, false, "three_months_plan", R.string.per_three_monthly,
        R.color.bg_pink, R.drawable.ic_switch_pink
    ),
    SIX_MONTHS(
        R.string.six_months_plan, false, "six_months_plan", R.string.per_six_monthly,
        R.color.white, R.drawable.ic_switch_white
    )
}