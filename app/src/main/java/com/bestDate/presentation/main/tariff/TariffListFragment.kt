package com.bestDate.presentation.main.tariff

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.utils.subscription.SubscriptionManager
import com.bestDate.databinding.FragmentTariffListBinding
import com.bestDate.presentation.base.BaseFragment
import com.bestDate.presentation.main.userProfile.invitationList.adapters.ViewPagerAdapter

class TariffListFragment: BaseFragment<FragmentTariffListBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTariffListBinding =
        { inflater, parent, attach -> FragmentTariffListBinding.inflate(inflater, parent, attach) }

    override val statusBarColor = R.color.bg_main
    private lateinit var subscriptionManager: SubscriptionManager

    override fun onInit() {
        super.onInit()

        subscriptionManager = SubscriptionManager(requireContext())
        val pagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle, getItemsList())

        with(binding) {
            tariffListView.offscreenPageLimit = 1
            tariffListView.adapter = pagerAdapter
            tariffListView.setPageTransformer(TariffTransformer())
            tariffListView.addItemDecoration(HorizontalMarginDecoration(6))

            tariffListView.setCurrentItem(1, false)
        }
    }

    private fun getItemsList(): MutableList<Fragment> {
        val items: MutableList<Fragment> = mutableListOf()

        TariffType.values().forEach {

            items.add(
                TariffFragment(it, subscriptionManager.getProductDetailsById(it.id)) {
                    it?.let {
                        subscriptionManager.launchBilling(requireActivity(), it)
                    }
                }
            )
        }

        return items
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.closeBtn.setOnSaveClickListener {
            goBack()
        }
    }
}