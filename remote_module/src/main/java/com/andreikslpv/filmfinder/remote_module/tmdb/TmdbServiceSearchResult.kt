package com.andreikslpv.filmfinder.remote_module.tmdb

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbServiceSearchResult {
    @GET("{version}/{path_1}/{path_2}")
    fun getFilms(
        @Path("version") version: String = TmdbConstants.VERSION_API,
        @Path("path_1") path1: String = TmdbConstants.PATH_SEARCH,
        @Path("path_2") path2: String = TmdbConstants.PATH_MOVIE,
        @Query("api_key") apiKey: String = TmdbKey.KEY,
        @Query("query") query: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Single<TmdbDtoResults>
}