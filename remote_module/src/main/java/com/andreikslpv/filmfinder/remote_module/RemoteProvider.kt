package com.andreikslpv.filmfinder.remote_module

import com.andreikslpv.filmfinder.remote_module.imdb.ImdbServiceFilmsByCategory
import com.andreikslpv.filmfinder.remote_module.imdb.ImdbServiceSearchResult
import com.andreikslpv.filmfinder.remote_module.tmdb.TmdbServiceFilmsByCategory
import com.andreikslpv.filmfinder.remote_module.tmdb.TmdbServiceSearchResult

interface RemoteProvider {

    fun provideImdbServiceFilmsByCategory(): ImdbServiceFilmsByCategory
    fun provideImdbServiceSearchResult(): ImdbServiceSearchResult
    fun provideTmdbServiceFilmsByCategory(): TmdbServiceFilmsByCategory
    fun provideTmdbServiceSearchResult(): TmdbServiceSearchResult

}