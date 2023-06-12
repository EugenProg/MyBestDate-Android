package com.bestDate.data.utils.ads

import android.os.Build
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.bestDate.BuildConfig
import com.bestDate.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

class BannerAdUtil {
    private var initialLayoutComplete = false

    fun setUpBanner(activity: FragmentActivity, container: FrameLayout) {
        val bannerId = setUpConfiguration(activity)

        val adView = AdView(activity)
        container.addView(adView)

        container.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true
                loadView(adView, bannerId, activity, container)
            }
        }
    }

    private fun setUpConfiguration(activity: FragmentActivity): String {
        var bannerId = activity.getString(R.string.ad_mob_duel_banner_id)
        if (BuildConfig.DEBUG) {
            val configurations = RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("25D641C228E3A8A73765F7B968E1F7AE"))
                .build()
            MobileAds.setRequestConfiguration(configurations)

            bannerId = "ca-app-pub-3940256099942544/6300978111"
        }
        return bannerId
    }

    private fun loadView(
        adView: AdView,
        bannerId: String,
        activity: FragmentActivity,
        container: FrameLayout
    ) {
        adView.adUnitId = bannerId
        adView.setAdSize(getAdSize(activity, container))

        adView.loadAd(
            AdRequest.Builder()
                .build()
        )
    }

    private fun getAdSize(activity: FragmentActivity, container: View): AdSize {
        var adWidthPixels = container.width.toFloat()

        if (adWidthPixels == 0f) {
            adWidthPixels = getWindowWidth(activity).toFloat()
        }

        val density = activity.resources.displayMetrics.density
        val adWidth = (adWidthPixels / density).toInt()

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
    }

    private fun getWindowWidth(activity: FragmentActivity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.windowManager.currentWindowMetrics.bounds.width()
        } else {
            activity.resources.displayMetrics.widthPixels
        }
    }
}