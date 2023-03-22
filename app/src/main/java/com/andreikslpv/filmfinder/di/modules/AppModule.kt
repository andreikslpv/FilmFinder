package com.andreikslpv.filmfinder.di.modules

import android.content.Context
import com.andreikslpv.filmfinder.presentation.vm.MainActivityViewModelFactory
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
}