package com.andreikslpv.filmfinder.di.modules

import com.andreikslpv.filmfinder.data.datasource.api.FilmsApiDataSource
import com.andreikslpv.filmfinder.data.datasource.api.tmdb.TmdbDataSource
import com.andreikslpv.filmfinder.data.datasource.local.FilmsLocalDataSource
import com.andreikslpv.filmfinder.data.datasource.local.JsonDataSource
import com.andreikslpv.filmfinder.data.repository.FilmsRepositoryImpl
import com.andreikslpv.filmfinder.domain.FilmsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindFilmsApiDataSource(tmdbDataSource: TmdbDataSource): FilmsApiDataSource

    @Binds
    @Singleton
    abstract fun bindFilmsLocalDataSource(jsonDataSource: JsonDataSource): FilmsLocalDataSource

    @Binds
    @Singleton
    abstract fun bindFilmsRepository(filmsRepositoryImpl: FilmsRepositoryImpl): FilmsRepository
}