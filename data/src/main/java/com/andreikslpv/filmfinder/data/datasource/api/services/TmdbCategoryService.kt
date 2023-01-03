package com.andreikslpv.filmfinder.data.datasource.api.services

import com.andreikslpv.filmfinder.data.datasource.api.dto.TmdbDtoResults
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbCategoryService {
    @GET("{version}/{path_1}/{path_2}")
    suspend fun getFilms(
        @Path("version") version: String,
        @Path("path_1") path1: String,
        @Path("path_2") path2: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<TmdbDtoResults>
}