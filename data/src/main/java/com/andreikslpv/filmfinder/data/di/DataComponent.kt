package com.andreikslpv.filmfinder.data.di

import com.andreikslpv.filmfinder.database_module.DatabaseProvider
import com.andreikslpv.filmfinder.remote_module.RemoteProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [RemoteProvider::class, DatabaseProvider::class],
    modules = [DataModule::class]
)
interface DataComponent : DataProvider