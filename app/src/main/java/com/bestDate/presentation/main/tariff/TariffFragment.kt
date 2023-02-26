package com.bestDate.presentation.main.tariff

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.databinding.FragmentTariffBinding
import com.bestDate.presentation.base.BaseFragment

class TariffFragment(private var type: TariffType) : BaseFragment<FragmentTariffBinding>() {
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

            amountView.text = "€ ${type.amount}"
            periodView.text = getString(type.period)
            cardsSwitch.setImageResource(type.switch)
            messageSwitch.setImageResource(type.switch)

            button.setBackgroundColor(ContextCompat.getColor(requireContext(), type.color))
        }
    }
}

enum class TariffType(
    @StringRes val title: Int, var popular: Boolean, var amount: Double,
    @StringRes var period: Int, @ColorRes var color: Int, @DrawableRes var switch: Int
) {
    MONTHLY(
        R.string.montly_plan, true, 5.99, R.string.per_montly,
        R.color.bg_light_blue, R.drawable.ic_switch_blue
    ),
    THREE_MONTHS(
        R.string.three_months_plan, false, 14.99, R.string.per_three_monthly,
        R.color.bg_pink, R.drawable.ic_switch_pink
    ),
    SIX_MONTHS(
        R.string.six_months_plan, false, 26.99, R.string.per_six_monthly,
        R.color.white, R.drawable.ic_switch_white
    )
}