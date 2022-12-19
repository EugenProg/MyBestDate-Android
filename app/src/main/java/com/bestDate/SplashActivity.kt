package com.bestDate

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bestDate.data.extension.postDelayed
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
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

        if (preferencesUtils.getBooleanWithDefault(Preferences.FIRST_ENTER, true)) {
            postDelayed({
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }, 1600)
        } else {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }

    }

}