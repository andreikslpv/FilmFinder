package com.andreikslpv.filmfinder.data.datasource.api.tmdb

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbInterfaceGetPopular {
    @GET("{version}/{path_1}/{path_2}")
    fun getFilms(
        @Path("version") version: String,
        @Path("path_1") path1: String,
        @Path("path_2") path2: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TmdbDtoResults>
}