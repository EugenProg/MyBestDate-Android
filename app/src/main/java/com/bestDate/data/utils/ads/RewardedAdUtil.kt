package com.bestDate.data.utils.ads

import android.app.Activity
import com.bestDate.BuildConfig
import com.bestDate.R
import com.bestDate.data.utils.Logger
import com.bestDate.view.alerts.LoaderDialog
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class RewardedAdUtil(private val activity: Activity) {
    private var rewardedAd: RewardedAd? = null
    private val loadingDialog: LoaderDialog = LoaderDialog(activity)

    var earnedReward: (() -> Unit)? = null
    var failed: (() -> Unit)? = null

    fun start() {
        loadingDialog.startLoading()
        val bannerId = setUpConfiguration()
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(activity, bannerId, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Logger.print(adError.toString())
                rewardedAd = null
                loadingDialog.stopLoading()
                failed?.invoke()
            }

            override fun onAdLoaded(ad: RewardedAd) {
                Logger.print("Ad was loaded.")
                rewardedAd = ad
                loadingDialog.stopLoading()
                initEarnedRewardListener()
            }
        })
    }

    private fun initEarnedRewardListener() {
        rewardedAd?.let { ad ->
            ad.show(activity) {
                Logger.print("User earned the reward\namount: ${it.amount}, type: ${it.type}")
                loadingDialog.stopLoading()
                earnedReward?.invoke()
            }
        } ?: kotlin.run {
            loadingDialog.stopLoading()
            failed?.invoke()
            Logger.print("The rewarded ad wasn't ready yet.")
        }
    }

    private fun setUpConfiguration(): String {
        var bannerId = activity.getString(R.string.ad_mob_rewarded_id)
        if (BuildConfig.DEBUG) {
            val configurations = RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("25D641C228E3A8A73765F7B968E1F7AE"))
                .build()
            MobileAds.setRequestConfiguration(configurations)

            bannerId = "ca-app-pub-3940256099942544/5224354917"
        }
        return bannerId
    }
}