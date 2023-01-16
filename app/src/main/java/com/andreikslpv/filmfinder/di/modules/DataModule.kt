package com.andreikslpv.filmfinder.di.modules

import com.andreikslpv.filmfinder.data.datasource.local.FilmsLocalDataSource
import com.andreikslpv.filmfinder.data.datasource.local.JsonDataSource
import com.andreikslpv.filmfinder.data.repository.FilmsRepositoryImpl
import com.andreikslpv.filmfinder.data.repository.SettingsRepositoryImpl
import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.SettingsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindFilmsLocalDataSource(jsonDataSource: JsonDataSource): FilmsLocalDataSource

    @Binds
    @Singleton
    abstract fun bindFilmsRepository(filmsRepositoryImpl: FilmsRepositoryImpl): FilmsRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository
}