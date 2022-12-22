package com.andreikslpv.filmfinder.data.datasource.api.kp

import com.andreikslpv.filmfinder.data.datasource.api.tmdb.TmdbDtoResults
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KpInterfaceGetCategory {
    @GET("{path}")
    fun getFilms(
        @Path("path") path: String,
        @Query("search") search: String,
        @Query("field") field: String,
        @Query("token") token: String
    ): Call<KpDtoResults>
}