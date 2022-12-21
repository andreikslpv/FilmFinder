package com.andreikslpv.filmfinder.data.datasource.api

import com.andreikslpv.filmfinder.data.datasource.api.tmdb.TmdbDtoResults
import retrofit2.Call
import retrofit2.Response

interface FilmsRetrofitInterface {
    fun getFilms(
        apiKey: String,
        language: String,
        page: Int
    ): Call<TmdbDtoResults>
}