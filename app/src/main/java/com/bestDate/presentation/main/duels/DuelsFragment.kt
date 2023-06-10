package com.bestDate.presentation.main.duels

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bestDate.BuildConfig
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.BackScreenType
import com.bestDate.databinding.FragmentDuelsBinding
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.view.DuelElementView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DuelsFragment : BaseVMFragment<FragmentDuelsBinding, DuelsViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDuelsBinding =
        { inflater, parent, attach -> FragmentDuelsBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<DuelsViewModel> = DuelsViewModel::class.java

    override val statusBarColor = R.color.bg_main
    private lateinit var adView: AdView
    private var initialLayoutComplete = false

    override fun onInit() {
        super.onInit()
        binding.resultView.isVisible = false
        setUpAdMobs()
        viewModel.setUserGender()
    }

    private fun setUpAdMobs() {
        var bannerId = getString(R.string.ad_mob_duel_banner_id)
        if (BuildConfig.DEBUG) {
            val configurations = RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("25D641C228E3A8A73765F7B968E1F7AE"))
                .build()
            MobileAds.setRequestConfiguration(configurations)

            bannerId = "ca-app-pub-3940256099942544/6300978111"
        }

        adView = AdView(requireContext())
        binding.adContainer.addView(adView)

        binding.adContainer.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true
                adView.adUnitId = bannerId
                adView.setAdSize(adSize)

                adView.loadAd(
                    AdRequest.Builder()
                        .build()
                )
            }
        }
    }

    private val adSize: AdSize
        get() {
            val windowMetrics = requireActivity().windowManager.currentWindowMetrics
            val bounds = windowMetrics.bounds

            var adWidthPixels = binding.adContainer.width.toFloat()

            if (adWidthPixels == 0f) {
                adWidthPixels = bounds.width().toFloat()
            }

            val density = resources.displayMetrics.density
            val adWidth = (adWidthPixels / density).toInt()

            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(requireContext(), adWidth)
        }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.myDuelsButton.click = {
            navController.navigate(DuelsFragmentDirections.actionDuelsToMyTopDuels())
        }
        binding.topButton.onClick = {
            navController.navigate(
                DuelsFragmentDirections.actionDuelsToTop(viewModel.gender)
            )
        }
        binding.noDataView.onClick = {
            navController.navigate(
                DuelsFragmentDirections.actionDuelsToTop(viewModel.gender)
            )
        }
        binding.resultView.openProfile = {
            navController.navigate(
                DuelsFragmentDirections.actionGlobalTopToAnotherProfile(it, BackScreenType.DUELS)
            )
        }
    }

    private fun setUpFilterButtons() {
        binding.locationFilterButton.label = getString(R.string.universe)
        binding.locationFilterButton.isActive = true
        binding.selectorView.setGender(viewModel.gender)
        binding.selectorView.onClick = {
            viewModel.gender = it
            viewModel.getDuels()
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.genderIsSetLiveData) {
            setUpFilterButtons()
        }
        observe(viewModel.duelImages) {
            binding.duelView.isVisible = it?.isEmpty() != true
            binding.noDataView.isVisible = it?.isEmpty() == true
            binding.noDataView.noData = it?.isEmpty() == true
            if (!it.isNullOrEmpty()) {
                setUpElement(binding.firstDuelElementView, it.first(), it.last())
                setUpElement(binding.secondDuelElementView, it.last(), it.first())
            }
        }
        observe(viewModel.hasNewDuels) {
            binding.myDuelsButton.badgeOn = it
        }
        observe(viewModel.duelResults) {
            binding.resultView.isVisible = it?.isNotEmpty() == true
            binding.resultView.duelProfiles = it
        }
        observe(viewModel.loadingLiveData) {
            if (viewModel.duelImages.value.isNullOrEmpty()) binding.noDataView.toggleLoading(it)
        }
        observe(viewModel.coins) {
            binding.coinView.amountCoins.text = it
        }
        observe(viewModel.errorLiveData) {
            binding.noDataView.toggleLoading(false)
            showMessage(it.exception.message)
        }
    }

    private fun setUpElement(
        duelElementView: DuelElementView,
        profileImage: Pair<Bitmap?, Int?>,
        anotherProfileImage: Pair<Bitmap?, Int?>
    ) {
        duelElementView.apply {
            setBitmap(profileImage.first)
            likeClick = {
                viewModel.postVote(profileImage.second.orZero, anotherProfileImage.second.orZero)
            }
        }
    }

    override fun networkIsUpdated() {
        super.networkIsUpdated()
        viewModel.getDuels()
    }
}