package com.andreikslpv.filmfinder.data.di

import com.andreikslpv.filmfinder.remote_module.RemoteProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [RemoteProvider::class],
    modules = [DataModule::class, DatabaseModule::class]
)
interface DataComponent : DataProvider