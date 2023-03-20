package com.andreikslpv.filmfinder.data.di

import android.content.Context
import com.andreikslpv.filmfinder.data.datasource.api.FilmsApiDataSource
import com.andreikslpv.filmfinder.data.datasource.api.imdb.ImdbDataSource
import com.andreikslpv.filmfinder.data.datasource.api.tmdb.TmdbDataSource
import com.andreikslpv.filmfinder.data.datasource.cache.FilmsCacheDataSource
import com.andreikslpv.filmfinder.data.datasource.cache.RoomCacheDataSource
import com.andreikslpv.filmfinder.data.datasource.local.FilmsLocalDataSource
import com.andreikslpv.filmfinder.data.datasource.local.RoomLocalDataSource
import com.andreikslpv.filmfinder.data.repository.FilmsRepositoryImpl
import com.andreikslpv.filmfinder.data.repository.SettingsRepositoryImpl
import com.andreikslpv.filmfinder.database_module.dao.CategoryDao
import com.andreikslpv.filmfinder.database_module.dao.CategoryFilmDao
import com.andreikslpv.filmfinder.database_module.dao.FilmDao
import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.SettingsRepository
import com.andreikslpv.filmfinder.remote_module.imdb.ImdbServiceFilmsByCategory
import com.andreikslpv.filmfinder.remote_module.imdb.ImdbServiceSearchResult
import com.andreikslpv.filmfinder.remote_module.tmdb.TmdbServiceFilmsByCategory
import com.andreikslpv.filmfinder.remote_module.tmdb.TmdbServiceSearchResult
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
class DataModule(val context: Context) {

    @Provides
    fun provideContext() = context

    @Provides
    @Singleton
    fun provideFilmsLocalDataSource(filmDao: FilmDao): FilmsLocalDataSource {
        return RoomLocalDataSource(filmDao)
    }

    @Provides
    @Singleton
    fun provideFilmsCacheDataSource(
        filmDao: FilmDao,
        categoryDao: CategoryDao,
        categoryFilmDao: CategoryFilmDao,
    ): FilmsCacheDataSource {
        return RoomCacheDataSource(filmDao, categoryDao, categoryFilmDao)
    }

    @Provides
    @Singleton
    @ImdbSourceQualifier
    fun provideImdbDataSource(
        context: Context,
        serviceCategory: ImdbServiceFilmsByCategory,
        serviceSearch: ImdbServiceSearchResult,
    ): FilmsApiDataSource {
        return ImdbDataSource(context, serviceCategory, serviceSearch)
    }

    @Provides
    @Singleton
    @TmdbSourceQualifier
    fun provideTmdbDataSource(
        context: Context,
        serviceCategory: TmdbServiceFilmsByCategory,
        serviceSearch: TmdbServiceSearchResult,
    ): FilmsApiDataSource {
        return TmdbDataSource(context, serviceCategory, serviceSearch)
    }

    @Provides
    @Singleton
    fun provideFilmsRepository(
        filmsLocalDataSource: FilmsLocalDataSource,
        filmsCacheDataSource: FilmsCacheDataSource,
        @ImdbSourceQualifier imdbDataSource: FilmsApiDataSource,
        @TmdbSourceQualifier tmdbDataSource: FilmsApiDataSource,
    ): FilmsRepository {
        return FilmsRepositoryImpl(
            filmsLocalDataSource,
            filmsCacheDataSource,
            imdbDataSource,
            tmdbDataSource
        )
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TmdbSourceQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ImdbSourceQualifier