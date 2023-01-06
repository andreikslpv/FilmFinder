package com.andreikslpv.filmfinder.data.datasource.api.tmdb

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbServiceSearchFilm {
    @GET("{version}/{path_1}/{path_2}")
    suspend fun getFilms(
        @Path("version") version: String = TmdbConstants.VERSION_API,
        @Path("path_1") path1: String = TmdbConstants.PATH_SEARCH,
        @Path("path_2") path2: String = TmdbConstants.PATH_MOVIE,
        @Query("api_key") apiKey: String = TmdbKey.KEY,
        @Query("query") query: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<TmdbDtoResults>
}