package com.bestDate.presentation.splashActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import com.bestDate.R
import com.bestDate.data.extension.createPendingIntent
import com.bestDate.data.extension.getPushType
import com.bestDate.data.extension.postDelayed
import com.bestDate.data.extension.safetyToInt
import com.bestDate.data.model.BackScreenType
import com.bestDate.data.model.ShortUserData
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.data.utils.Logger
import com.bestDate.data.utils.notifications.NotificationType
import com.bestDate.presentation.mainActivity.MainActivity
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var preferencesUtils: PreferencesUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        if (preferencesUtils.getBooleanWithDefault(Preferences.FIRST_ENTER, true)) {
            postDelayed({
                startWithStandardNavigation()
            }, 1600)
        } else {
            checkForDeeplink()
        }
    }

    private fun navigate() {
        val bundle = intent.extras
        if (bundle != null) {
            val type = bundle.getString("type")
            if (!type.isNullOrBlank()) notificationAction(type, bundle)
            else startWithStandardNavigation()
        } else {
            startWithStandardNavigation()
        }
    }

    private fun startWithStandardNavigation() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }

    private fun checkForDeeplink() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener {
                it?.let {
                    val userId = it.link?.toString()?.substring(22).safetyToInt()
                    val args = bundleOf(
                        "user" to ShortUserData(id = userId),
                        "backScreen" to BackScreenType.START
                    )
                    createPendingIntent(this, R.id.another_profile_nav_graph, args).send()
                } ?: navigate()
            }
            .addOnFailureListener {
                Logger.print("Deeplink read exception: ${it.message}")
                navigate()
            }
    }

    private fun notificationAction(type: String, bundle: Bundle) {
        val pushType = type.getPushType()
        var args: Bundle? = null
        if (pushType == NotificationType.MESSAGE) {
            args = bundleOf(
                "user" to getUserFromBundle(bundle),
                "backScreen" to BackScreenType.PROFILE
            )
        }
        createPendingIntent(this, pushType.destination, args).send()
    }

    private fun getUserFromBundle(bundle: Bundle): ShortUserData? {
        try {
            return Gson().fromJson(bundle.getString("user"), ShortUserData::class.java)
        } catch (e: Exception) {
            Logger.print("Parsing exception: ${e.message}")
        }
        return null
    }
}