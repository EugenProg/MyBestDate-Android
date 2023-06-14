package com.bestDate.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.bestDate.data.utils.subscription.SubscriptionManager
import com.bestDate.data.utils.subscription.SubscriptionUtil
import com.bestDate.presentation.main.UserUseCase
import com.bestDate.view.alerts.BanningDialog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(Preferences.APP.name, AppCompatActivity.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providesPreferencesUtils(preferences: SharedPreferences): PreferencesUtils =
        PreferencesUtils(preferences)

    @Provides
    @Singleton
    fun provideSubscriptionManager(@ApplicationContext context: Context,
                                   preferencesUtils: PreferencesUtils): SubscriptionManager =
        SubscriptionManager(context, preferencesUtils)

    @Provides
    @Singleton
    fun provideSubscriptionUtils(preferencesUtils: PreferencesUtils): SubscriptionUtil =
        SubscriptionUtil(preferencesUtils)

    @Provides
    fun provideBanningDialog(subscriptionUtil: SubscriptionUtil, userUseCase: UserUseCase):
            BanningDialog = BanningDialog(subscriptionUtil, userUseCase)
}