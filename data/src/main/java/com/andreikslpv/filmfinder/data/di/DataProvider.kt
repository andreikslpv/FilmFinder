package com.andreikslpv.filmfinder.data.di

import com.andreikslpv.filmfinder.database_module.dao.CategoryDao
import com.andreikslpv.filmfinder.database_module.dao.CategoryFilmDao
import com.andreikslpv.filmfinder.database_module.dao.FilmDao
import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.SettingsRepository
import com.andreikslpv.filmfinder.remote_module.imdb.ImdbServiceFilmsByCategory
import com.andreikslpv.filmfinder.remote_module.imdb.ImdbServiceSearchResult
import com.andreikslpv.filmfinder.remote_module.tmdb.TmdbServiceFilmsByCategory
import com.andreikslpv.filmfinder.remote_module.tmdb.TmdbServiceSearchResult

interface DataProvider {

    fun provideFilmsRepository(): FilmsRepository
    fun provideSettingsRepository(): SettingsRepository

    fun provideImdbServiceFilmsByCategory(): ImdbServiceFilmsByCategory
    fun provideImdbServiceSearchResult(): ImdbServiceSearchResult
    fun provideTmdbServiceFilmsByCategory(): TmdbServiceFilmsByCategory
    fun provideTmdbServiceSearchResult(): TmdbServiceSearchResult

    fun provideFilmDao(): FilmDao
    fun provideCategoryDao(): CategoryDao
    fun provideCategoryFilmDao(): CategoryFilmDao
}