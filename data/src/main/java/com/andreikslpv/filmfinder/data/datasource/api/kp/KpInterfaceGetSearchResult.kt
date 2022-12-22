package com.andreikslpv.filmfinder.data.datasource.api.kp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KpInterfaceGetSearchResult {
    @GET("{path}")
    fun getFilms(
        @Path("path") path: String,
        @Query("search") search: String,
        @Query("field") field: String,
        @Query("isStrict") isStrict: Boolean,
        @Query("token") token: String
    ): Call<KpDtoResults>
}