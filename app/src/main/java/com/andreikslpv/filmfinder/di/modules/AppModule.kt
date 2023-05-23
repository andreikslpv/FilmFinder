package com.andreikslpv.filmfinder.di.modules

import android.content.Context
import com.andreikslpv.filmfinder.presentation.ui.utils.AppConstants.MINIMUM_FETCH_INTERVAL
import com.andreikslpv.filmfinder.presentation.vm.MainActivityViewModelFactory
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val context: Context) {

    @Provides
    fun provideContext() = context

    @Provides
    @Singleton
    fun provideMainActivityViewModelFactory(context: Context): MainActivityViewModelFactory {
        return MainActivityViewModelFactory(context)
    }

    @Provides
    @Singleton
    fun provideRemoteConfigInstance(): FirebaseRemoteConfig {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val settings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = MINIMUM_FETCH_INTERVAL
        }
        remoteConfig.setConfigSettingsAsync(settings)
        return remoteConfig
    }
}